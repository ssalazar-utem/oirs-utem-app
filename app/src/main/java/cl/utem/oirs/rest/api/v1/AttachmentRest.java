package cl.utem.oirs.rest.api.v1;

import cl.utem.oirs.rest.api.vo.DataVO;
import cl.utem.oirs.rest.api.vo.ResponseVO;
import cl.utem.oirs.rest.domain.model.Attachment;
import cl.utem.oirs.rest.domain.model.Ticket;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.exception.AuthException;
import cl.utem.oirs.rest.exception.NoDataException;
import cl.utem.oirs.rest.exception.ValidationException;
import cl.utem.oirs.rest.manager.AuthManager;
import cl.utem.oirs.rest.manager.TicketManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/v1/attachments",
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@Tag(name = "Attachments", description = "API para manejar archivos adjuntos asociados a tickets")
public class AttachmentRest implements Serializable {

    private static final long serialVersionUID = 1;
    private final transient AuthManager authManager;
    private final transient TicketManager ticketManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentRest.class);

    @Autowired
    public AttachmentRest(AuthManager authManager, TicketManager ticketManager) {
        this.authManager = authManager;
        this.ticketManager = ticketManager;
    }

    private String upload(Ticket ticket, DataVO body) {
        String token = StringUtils.EMPTY;
        try {
            if (ticket != null && body != null) {
                File tmp = File.createTempFile(ticket.getToken(), body.getName());
                FileUtils.writeByteArrayToFile(tmp, Base64.decodeBase64(body.getData()));
                token = ticketManager.attach(ticket, tmp); // Eliminar el archivo solo si se guarda correctamente
                FileUtils.deleteQuietly(tmp);
            }
        } catch (Exception e) {
            LOGGER.error("Error al subir información al servidor: {}", e.getLocalizedMessage());
            LOGGER.debug("Error al subir información al servidor: {}", e.getMessage(), e);
        }
        return token;
    }

    /**
     * Sube un archivo adjunto asociado a un ticket.
     *
     * @param request la solicitud HTTP
     * @param authorization el token de autorización del usuario
     * @param ticketToken el token del ticket
     * @param body los datos del archivo adjunto (nombre, mime, contenido en
     * Base64)
     * @return un ResponseEntity con el token del archivo subido y un mensaje de
     * éxito
     */
    @Operation(summary = "Subir un archivo adjunto a un ticket", description = "Permite subir un archivo adjunto relacionado con un ticket especificado por su token.")
    @ApiResponse(responseCode = "200", description = "Archivo subido con éxito",
            content = @Content(schema = @Schema(implementation = ResponseVO.class)))
    @ApiResponse(responseCode = "404", description = "Ticket no encontrado o usuario no autorizado")
    @ApiResponse(responseCode = "400", description = "Error de validación al subir el archivo")
    @PostMapping(value = "/{ticketToken}/upload",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ResponseVO> ticketAttachment(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "ticketToken") String ticketToken,
            @RequestBody DataVO body) {

        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        Ticket ticket = ticketManager.getTicket(ticketToken);
        if (ticket == null) {
            throw new NoDataException(String.format("No se ha encontrado el ticket con token %s", ticketToken));
        }

        if (!Objects.equals(user, ticket.getUser())) {
            throw new ValidationException("El usuario que sube el archivo no corresponde con el usuario que creo el ticket");
        }

        final String attToken = upload(ticket, body);
        if (StringUtils.isBlank(attToken)) {
            throw new ValidationException("No fue posible subir el archivo al servidor");
        }

        ResponseVO vo = new ResponseVO(attToken, String.format("Se ha subido el adjunto asociado al ticket %s al servidor", ticketToken));
        return ResponseEntity.ok(vo);
    }

    /**
     * Obtiene un archivo adjunto asociado a un ticket.
     *
     * @param request la solicitud HTTP
     * @param authorization el token de autorización del usuario
     * @param ticketToken el token del ticket
     * @param attToken el token del archivo adjunto
     * @return un ResponseEntity con los datos del archivo adjunto en formato
     * Base64
     */
    @Operation(summary = "Obtener un archivo adjunto", description = "Recupera un archivo adjunto asociado a un ticket especificado por su token.")
    @ApiResponse(responseCode = "200", description = "Archivo recuperado con éxito",
            content = @Content(schema = @Schema(implementation = DataVO.class)))
    @ApiResponse(responseCode = "404", description = "Adjunto o ticket no encontrado o usuario no autorizado")
    @ApiResponse(responseCode = "400", description = "Error de validación al recuperar el archivo")
    @GetMapping(value = "/{ticketToken}/{attToken}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<DataVO> getAttachment(HttpServletRequest request,
            @RequestHeader(name = "Authorization", required = true) String authorization,
            @PathVariable(name = "ticketToken") String ticketToken,
            @PathVariable(name = "attToken") String attToken) {

        final User user = authManager.authenticate(request, authorization);
        if (user == null) {
            throw new AuthException();
        }

        Ticket ticket = ticketManager.getTicket(ticketToken);
        if (ticket == null) {
            throw new NoDataException(String.format("No se ha encontrado el ticket con token %s", ticketToken));
        }

        if (!Objects.equals(user, ticket.getUser())) {
            throw new ValidationException("El usuario que sube el archivo no corresponde con el usuario que creo el ticket");
        }

        Attachment attachment = ticketManager.getAttachment(attToken);
        if (attachment == null) {
            throw new NoDataException(String.format("No se ha encontrado el adjunto con token %s", attToken));
        }

        if (!Objects.equals(ticket, attachment.getTicket())) {
            throw new ValidationException("El ticket no corresponde con el ticket del adjunto");
        }

        DataVO vo = new DataVO();
        vo.setData(Base64.encodeBase64String(attachment.getContent()));
        vo.setMime(attachment.getMime());
        vo.setName(attToken);
        return ResponseEntity.ok(vo);
    }
}
