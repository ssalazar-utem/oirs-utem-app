package cl.utem.oirs.rest.manager;

import cl.utem.oirs.rest.domain.enums.IcsoStatus;
import cl.utem.oirs.rest.domain.enums.IcsoType;
import cl.utem.oirs.rest.domain.model.Attachment;
import cl.utem.oirs.rest.domain.model.Category;
import cl.utem.oirs.rest.domain.model.History;
import cl.utem.oirs.rest.domain.model.Ticket;
import cl.utem.oirs.rest.domain.model.User;
import cl.utem.oirs.rest.domain.repository.AttachmentRepository;
import cl.utem.oirs.rest.domain.repository.CategoryRepository;
import cl.utem.oirs.rest.domain.repository.HistoryRepository;
import cl.utem.oirs.rest.domain.repository.TicketRepository;
import cl.utem.oirs.rest.utils.IcsoUtils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private final transient AttachmentRepository attachmentRepository;
    private final transient CategoryRepository categoryRepository;
    private final transient HistoryRepository historyRepository;
    private final transient TicketRepository ticketRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketManager.class);

    @Autowired
    public TicketManager(AttachmentRepository attachmentRepository,
            CategoryRepository categoryRepository,
            HistoryRepository historyRepository,
            TicketRepository ticketRepository) {
        this.attachmentRepository = attachmentRepository;
        this.categoryRepository = categoryRepository;
        this.historyRepository = historyRepository;
        this.ticketRepository = ticketRepository;
    }

    public Attachment getAttachment(final String token) {
        Attachment attachment = null;
        if (StringUtils.isNotBlank(token)) {
            attachment = attachmentRepository.findByToken(token);
        }
        return attachment;
    }

    public List<String> getAttachmentsTokens(final Ticket ticket) {
        List<String> list = new ArrayList<>();
        if (ticket != null) {
            list = attachmentRepository.searchTokenByTicket(ticket);
        }
        return list;
    }

    @Transactional
    public String attach(Ticket ticket, File file) throws IOException {
        String token = StringUtils.EMPTY;
        if (ticket != null && file != null && file.exists()) {
            byte[] data = FileUtils.readFileToByteArray(file);
            if (data != null && data.length > 0) {
                // Obtener el tipo MIME del archivo
                String mime = Files.probeContentType(file.toPath());
                if (StringUtils.isBlank(mime)) {
                    // Tipo por defecto si no se detecta el MIME
                    mime = "application/octet-stream";
                }

                // Crear el adjunto
                Attachment attachment = new Attachment();
                attachment.setContent(data);
                attachment.setMime(mime);
                attachment.setTicket(ticket);
                attachment.setToken(IcsoUtils.getAttachmentToken(ticket.getToken()));

                // Guardar el adjunto
                Attachment saved = attachmentRepository.save(attachment);
                token = saved.getToken();

                // Eliminar el archivo solo si se guarda correctamente
                FileUtils.deleteQuietly(file);
            }
        }
        return token;
    }

    private void saveHistory(final Ticket ticket) {
        saveHistory(ticket, null);
    }

    private void saveHistory(final Ticket ticket, final User staff) {
        if (ticket != null) {
            History history = new History();
            history.setCreated(ticket.getCreated());
            history.setMessage(ticket.getMessage());
            history.setStatus(ticket.getStatus().name());
            history.setSubject(ticket.getSubject());
            history.setToken(ticket.getToken());
            history.setUpdated(ticket.getUpdated());
            history.setUserEmail(ticket.getUser().getEmail());

            if (staff != null) {
                history.setResponse(ticket.getResponse());
                history.setStaffEmail(staff.getEmail());
            }

            historyRepository.save(history);
        }
    }

    public Category getCategory(final String token) {
        Category category = null;
        if (StringUtils.isNotBlank(token)) {
            category = categoryRepository.findByToken(token);
        }
        return category;
    }

    public List<Category> getCatogories() {
        return categoryRepository.findAll();
    }

    public Ticket getTicket(final String token) {
        Ticket ticket = null;
        if (StringUtils.isNotBlank(token)) {
            ticket = ticketRepository.findByToken(token);
        }
        return ticket;
    }

    public List<Ticket> getTickets(Category category) {
        List<Ticket> tickets = new ArrayList<>();
        if (category != null) {
            tickets = ticketRepository.findByCategory(category);
        }
        return tickets;
    }

    @Transactional
    public Ticket create(final Category category, final User user, final IcsoType type, final String subject, final String message) {
        final OffsetDateTime now = OffsetDateTime.now();

        Ticket ticket = new Ticket();
        ticket.setCategory(category);
        ticket.setMessage(message);
        ticket.setResponse(StringUtils.EMPTY);
        ticket.setStatus(IcsoStatus.RECEIVED);
        ticket.setSubject(subject);
        ticket.setToken(IcsoUtils.randomAlphanumeric(17));
        ticket.setType(type);
        ticket.setUser(user);
        ticket.setCreated(now);
        ticket.setUpdated(now);

        Ticket created = ticketRepository.save(ticket);
        saveHistory(created);
        return created;
    }

    @Transactional
    public Ticket save(Ticket ticket) {
        Ticket saved = null;
        if (ticket != null) {
            saved = ticketRepository.save(ticket);
            saveHistory(saved);
        }
        return saved;
    }

    @Transactional
    public boolean deleteTicket(final String tokenToken) {
        boolean deleted = false;
        if (StringUtils.isNotBlank(tokenToken)) {
            Ticket ticket = ticketRepository.findByToken(tokenToken);
            if (ticket != null) {
                ticketRepository.delete(ticket);
                deleted = true;
            }
        }
        return deleted;
    }

    @Transactional
    public Ticket response(User staffUser, Ticket ticket) {
        Ticket responsed = null;
        if (staffUser != null && ticket != null) {
            responsed = ticketRepository.save(ticket);
            saveHistory(ticket, staffUser);
        }
        return responsed;
    }
}
