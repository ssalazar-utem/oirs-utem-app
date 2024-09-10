package cl.utem.oirs.rest.utils;

import cl.utem.oirs.rest.exception.AuthException;
import cl.utem.oirs.rest.exception.ValidationException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

public class GoogleAuthUtils implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final GoogleIdTokenVerifier VERIFIER = new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(),
            new GsonFactory()
    ).build();

    public static String getEmail(final String idTokenJwt) {
        String email = StringUtils.EMPTY;
        try {
            if (StringUtils.isBlank(idTokenJwt)) {
                throw new ValidationException("Se necesita un idToken");
            }

            GoogleIdToken google = VERIFIER.verify(idTokenJwt);
            if (google == null) {
                throw new ValidationException("No se pudo verificar el idToken");
            }

            if (!google.verifyExpirationTime(System.currentTimeMillis(), 17)) {
                throw new ValidationException("idToken expirado.");
            }

            final boolean emailVerified = google.getPayload().getEmailVerified();
            if (!emailVerified) {
                String name = (String) google.getPayload().get("name");
                throw new ValidationException(String.format("El correo electrónico de %s no está validado", name));
            }

            email = StringUtils.lowerCase(StringUtils.trimToEmpty(google.getPayload().getEmail()));
            if (!EmailValidator.getInstance().isValid(email)) {
                throw new ValidationException(String.format("El correo electrónico %s no es válido", email));
            }

            if (!StringUtils.containsIgnoreCase(email, "@utem.cl")) {
                throw new ValidationException(String.format("El correo electrónico %s no pertenece al dominio de la utem", email));
            }
        } catch (Exception e) {
            throw new AuthException("Error al decodificar y validar idToken de Google", e);
        }
        return email;
    }
}
