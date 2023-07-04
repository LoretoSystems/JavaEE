package systems.loreto.javaee.filters;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class JavaEEInvalidIPsFilterTest {

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private HttpServletResponse httpResponse;

    @Mock
    private FilterChain filterChain;

    private JavaEEInvalidIPsFilter javaEEInvalidIPsFilter;

    private AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        javaEEInvalidIPsFilter = new JavaEEInvalidIPsFilter();
    }

    @AfterEach
    void tearDown() {
        try {
            openMocks.close();
        } catch (Exception e) {
            fail("FAILED to close the open mocks.");
        }
    }

    @Test
    @DisplayName("Block requests from specific IP")
    void blockRequestFromSpecificIP() throws IOException, ServletException {
        when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        javaEEInvalidIPsFilter.doFilter(httpRequest, httpResponse, filterChain);

        verify(httpResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");

        verifyNoInteractions(filterChain);
    }

    @Test
    @DisplayName("Allow requests from different IPs")
    void allowRequestFromDifferentIP() throws IOException, ServletException {
        when(httpRequest.getRemoteAddr()).thenReturn("192.168.0.1");

        javaEEInvalidIPsFilter.doFilter(httpRequest, httpResponse, filterChain);

        verify(filterChain).doFilter(httpRequest, httpResponse);

        verifyNoInteractions(httpResponse);
    }
}
