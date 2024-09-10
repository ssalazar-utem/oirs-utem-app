package cl.utem.oirs.rest.utils;

import cl.utem.oirs.rest.domain.enums.IcsoStatus;
import cl.utem.oirs.rest.domain.enums.IcsoType;
import java.io.Serializable;
import java.security.SecureRandom;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IcsoUtils implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Dígitos válidos
     */
    private static final String NUMCHARS = "9876543210";

    /**
     * Letras que usamos para hacer strings aleatoreos
     */
    private static final String PRINTABLE_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Logger LOGGER = LoggerFactory.getLogger(IcsoUtils.class);

    /**
     *
     * @param size Tamaño del resultado
     * @return Un número aleatoreo (como string)
     */
    public static String randomNumber(final int size) {
        String random = StringUtils.EMPTY;
        if (size > 0) {
            final Integer availableChars = StringUtils.length(NUMCHARS);
            SecureRandom secureRandom = new SecureRandom();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb.append(NUMCHARS.charAt(secureRandom.nextInt(availableChars)));
            }
            random = StringUtils.trimToEmpty(sb.toString());
        }
        return random;
    }

    /**
     * SecureRandom strong
     *
     * @param size Tamaño del texto
     * @return Un string aleatoreo
     */
    public static String randomAlphanumeric(final int size) {
        String random = StringUtils.EMPTY;
        if (size > 0) {
            random = new SecureRandom()
                    .ints(size, 0, PRINTABLE_CHARS.length())
                    .mapToObj(PRINTABLE_CHARS::charAt)
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        } else {
            LOGGER.error("Error en el largo del tamaño ({}) para string aleatoreo", size);
        }
        return random;
    }

    public static IcsoType getType(final String txt) {
        final String typeStr = StringUtils.upperCase(StringUtils.trimToEmpty(txt));
        return IcsoType.valueOf(typeStr);
    }

    public static IcsoStatus getStatus(final String txt) {
        final String statusStr = StringUtils.upperCase(StringUtils.trimToEmpty(txt));
        return IcsoStatus.valueOf(statusStr);
    }

    public static boolean canChangeStatus(IcsoStatus from, IcsoStatus to) {
        if (from == null || to == null) {
            return false;
        }

        // ERROR, RECEIVED, UNDER_REVIEW, IN_PROGRESS, PENDING_INFORMATION, RESOLVED, CLOSED, REJECTED, CANCELLED
        switch (from) {
            case RECEIVED:
                return IcsoStatus.UNDER_REVIEW.equals(to) || IcsoStatus.IN_PROGRESS.equals(to) || IcsoStatus.CANCELLED.equals(to);

            case UNDER_REVIEW:
                return IcsoStatus.IN_PROGRESS.equals(to) || IcsoStatus.CANCELLED.equals(to);

            case IN_PROGRESS:
                return IcsoStatus.PENDING_INFORMATION.equals(to) || IcsoStatus.RESOLVED.equals(to) || IcsoStatus.REJECTED.equals(to) || IcsoStatus.CANCELLED.equals(to);

            case PENDING_INFORMATION:
                return IcsoStatus.RESOLVED.equals(to) || IcsoStatus.REJECTED.equals(to) || IcsoStatus.CANCELLED.equals(to);

            case RESOLVED:
                return IcsoStatus.CLOSED.equals(to);

            default:
                return false;
        }
    }

    public static String getAttachmentToken(final String ticketToken) {
        String attTkn = randomAlphanumeric(47);
        if (StringUtils.isNotBlank(attTkn)) {
            attTkn = StringUtils.substring(String.format("%s-%s", ticketToken, randomAlphanumeric(47)), 0, 47);
        }
        return attTkn;
    }
}
