package systems.loreto.javaee.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JavaEEInvalidIPsFilter implements Filter {

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
