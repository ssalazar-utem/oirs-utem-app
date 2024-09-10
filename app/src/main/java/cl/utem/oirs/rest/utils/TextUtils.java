package cl.utem.oirs.rest.utils;

import java.io.Serializable;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

public class TextUtils implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String ONLY_TEXT_PATTERN = "^[\\p{L}\\s]+$";
    public static final String VALID_DESCRIPTION_PATTERN = "^[\\p{L}0-9\\s]+$";

    private TextUtils() {
        throw new IllegalStateException();
    }

    /**
     *
     * @param text Texto de entrada
     * @return Una cadena no nula con los espacios normalizados
     */
    public static String normalize(final String text) {
        String normal = StringUtils.EMPTY;

        if (StringUtils.isNotBlank(text)) {
            normal = StringUtils.normalizeSpace(StringUtils.trimToEmpty(text));
        }

        return normal;
    }

    /**
     *
     * @param firstName Nombres
     * @param lastName Apellidos
     * @return El nombre compuesto desde los apellidos
     */
    public static String getName(final String firstName, final String lastName) {
        return StringUtils.upperCase(normalize(String.format("%s %s", firstName, lastName)));
    }

    public static String upper(final String text) {
        return StringUtils.upperCase(normalize(text));
    }

    public static String queryUpper(final String text) {
        return StringUtils.replace(StringUtils.upperCase(normalize(text)), StringUtils.SPACE, "%");
    }

    public static String lower(final String text) {
        return StringUtils.lowerCase(normalize(text));
    }

    public static String upperWithoutSpace(final String text) {
        return StringUtils.upperCase(StringUtils.remove(StringUtils.trimToEmpty(text), StringUtils.SPACE));
    }

    public static String upperWithoutAccents(final String text) {
        return StringUtils.stripAccents(normalize(text));
    }

    public static String escape(final String text) {
        return upper(StringEscapeUtils.escapeJson(text));
    }

    /**
     * Intenta adivinar el nombre
     *
     * @param fullname El nombre completo
     * @return obtiene el texto hasta el primer espacio
     */
    public static String getFirstName(final String fullname) {
        String firstname = StringUtils.EMPTY;

        // Normalizo los espacio
        final String text = normalize(fullname);
        if (StringUtils.isNotBlank(text)) {
            String[] array = StringUtils.split(text, StringUtils.SPACE);
            if (array != null && array.length > 0) {
                firstname = StringUtils.trimToEmpty(array[0]);
            }
        }

        return firstname;
    }

    /**
     * Intenta adivinar el apellido
     *
     * @param fullname El nombre completo
     * @return obtiene el texto desde el primer espacio
     */
    public static String getLastName(final String fullname) {
        String lastname = StringUtils.EMPTY;

        // Normalizo los espacio
        final String text = normalize(fullname);
        if (StringUtils.isNotBlank(text)) {
            String[] array = StringUtils.split(text, StringUtils.SPACE);
            if (array != null && array.length > 1) {
                StringBuilder sb = new StringBuilder();
                int index = 0;
                for (String one : array) {
                    if (index > 0) {
                        sb.append(StringUtils.trimToEmpty(one));
                        sb.append(StringUtils.SPACE);
                    }
                    index += 1;
                }
                lastname = StringUtils.trimToEmpty(sb.toString());
            }
        }

        return lastname;
    }

    public static String cybText(final String text) {
        String result = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(text)) {
            final String regex = RegExUtils.removePattern(text, "[^A-Za-z0-9\\s]");
            result = upper(StringUtils.substring(regex, 0, 47));
        }
        return result;
    }

    public static String parseNumber(final String text) {
        String number = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(text)) {
            if (StringUtils.contains(text, ".")) {
                number = StringUtils.trimToEmpty(StringUtils.split(text, ".")[0]);
            } else if (StringUtils.contains(text, ",")) {
                number = StringUtils.trimToEmpty(StringUtils.split(text, ",")[0]);
            } else {
                number = StringUtils.trimToEmpty(text);
            }
        }
        return number;
    }
}
