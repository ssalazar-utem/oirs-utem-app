package cl.utem.oirs.rest.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author seba
 */
public class IpUtils implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String UNKNOWN = "unknown";

    /**
     * Retorna la IP más probable del cliente a partir de una solicitud HTTP.
     * Prioriza los encabezados que podrían ser agregados por proxies o
     * balanceadores de carga.
     *
     * @param request la solicitud HttpServletRequest
     * @return la IP más probable del cliente
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            // X-Forwarded-For puede contener una lista de IPs, la primera es la del cliente real
            return StringUtils.trimToEmpty(ip.split(",")[0]);
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return StringUtils.trimToEmpty(ip);
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return StringUtils.trimToEmpty(ip);
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return StringUtils.trimToEmpty(ip);
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return StringUtils.trimToEmpty(ip);
        }

        // Si ninguno de los encabezados anteriores contiene una IP válida, obtenemos la IP remota directa
        return StringUtils.trimToEmpty(request.getRemoteAddr());
    }
}
