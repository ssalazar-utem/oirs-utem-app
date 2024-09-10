package cl.utem.oirs.rest.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author seba
 */
public class TikaUtils implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Tika ENGINE = new Tika();
    private static final MimeTypes ALL_MIME_TYPES = MimeTypes.getDefaultMimeTypes();
    private static final Logger LOGGER = LoggerFactory.getLogger(TikaUtils.class);

    // Método estático para detectar el tipo MIME desde un array de bytes
    public static String detectMimeType(byte[] data) {
        String mime = StringUtils.EMPTY;
        try {
            if (data != null && data.length > 0) {
                mime = ENGINE.detect(data);
            }
        } catch (Exception e) {
            LOGGER.error("Error al determinar mime: {}", e.getLocalizedMessage());
            LOGGER.debug("Error al determinar mime: {}", e.getMessage(), e);
        }
        return mime;
    }

    // Método estático para detectar la extensión del archivo desde un array de bytes
    public static String detectExtension(byte[] data) {
        String ext = StringUtils.EMPTY;
        try {
            String mimeType = detectMimeType(data);
            if (StringUtils.isNotBlank(mimeType)) {
                MimeType type = ALL_MIME_TYPES.forName(mimeType);
                ext = type.getExtension();
            }
        } catch (Exception e) {
            LOGGER.error("Error al determinar extensión: {}", e.getLocalizedMessage());
            LOGGER.debug("Error al determinar extensión: {}", e.getMessage(), e);
        }
        return ext;
    }

    public static File makeFile(final String token, final byte[] data) throws IOException {
        File file = null;
        final String ext = detectExtension(data);
        if (StringUtils.isNotBlank(ext)) {
            file = File.createTempFile(token, ext);
            FileUtils.writeByteArrayToFile(file, data);
        }
        return file;
    }

    public static boolean isImage(String mimeType) {
        return StringUtils.isNotBlank(mimeType) && mimeType.startsWith("image/");
    }

    public static boolean isText(String mimeType) {
        return StringUtils.isNotBlank(mimeType) && mimeType.startsWith("text/");
    }

    public static boolean isPDF(String mimeType) {
        return "application/pdf".equals(mimeType);
    }

    // Verifica si es un documento de procesador de texto
    public static boolean isWordProcessorDocument(String mimeType) {
        return StringUtils.isNotBlank(mimeType) && (mimeType.equals("application/msword")
                || mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                || mimeType.equals("application/vnd.oasis.opendocument.text")
                || mimeType.equals("application/rtf"));
    }

    // Verifica si es una hoja de cálculo
    public static boolean isSpreadsheet(String mimeType) {
        return StringUtils.isNotBlank(mimeType) && (mimeType.equals("application/vnd.ms-excel")
                || mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                || mimeType.equals("application/vnd.oasis.opendocument.spreadsheet"));
    }

    // Verifica si es una presentación
    public static boolean isPresentation(String mimeType) {
        return StringUtils.isNotBlank(mimeType) && (mimeType.equals("application/vnd.ms-powerpoint")
                || mimeType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")
                || mimeType.equals("application/vnd.oasis.opendocument.presentation"));
    }

    public static boolean isValid(final String mimeType) {
        boolean ok = false;
        if (StringUtils.isNotBlank(mimeType)) {
            ok = isImage(mimeType) || isPDF(mimeType) || isText(mimeType)
                    || isPresentation(mimeType) || isSpreadsheet(mimeType) || isWordProcessorDocument(mimeType);
        }
        return ok;
    }
}
