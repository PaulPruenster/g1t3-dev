package at.qe.skeleton.configs;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

class CustomServletContextInitializerTest {

    @Test
    void testOnStartup() throws ServletException {
        ServletContext sc = Mockito.mock(ServletContext.class);

        CustomServletContextInitializer initializer = new CustomServletContextInitializer();
        initializer.onStartup(sc);

        verify(sc, times(1)).setInitParameter(("com.sun.faces.forceLoadConfiguration"), ("true"));
        verify(sc, times(1)).setInitParameter(("javax.faces.DEFAULT_SUFFIX"), (".xhtml"));
        verify(sc, times(1)).setInitParameter(("javax.faces.PROJECT_STAGE"), ("Development"));
        verify(sc, times(1)).setInitParameter(("javax.faces.STATE_SAVING_METHOD"), ("server"));
        verify(sc, times(1)).setInitParameter(("javax.faces.FACELETS_SKIP_COMMENTS"), ("true"));
        verify(sc, times(1)).setInitParameter(("javax.faces.ENABLE_CDI_RESOLVER_CHAIN"), ("true"));
        verify(sc, times(1)).setInitParameter(("org.omnifaces.SOCKET_ENDPOINT_ENABLED"), ("true"));
    }
}



