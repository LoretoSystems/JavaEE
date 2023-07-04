package systems.loreto.jakartaee.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JakartaEEInvalidIPsFilter implements Filter {

    private static final String LOCAL_HOST_IP = "127.0.0.1";

    private static boolean isLocalHost(HttpServletRequest httpRequest) {
        var ipAddress = httpRequest.getRemoteAddr();
        return LOCAL_HOST_IP.equals(ipAddress);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        if (isLocalHost(httpRequest)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        chain.doFilter(request, response);
    }
}
