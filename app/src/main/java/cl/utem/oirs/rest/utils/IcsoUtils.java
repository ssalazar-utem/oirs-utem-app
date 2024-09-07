package cl.utem.oirs.rest.utils;

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
}
