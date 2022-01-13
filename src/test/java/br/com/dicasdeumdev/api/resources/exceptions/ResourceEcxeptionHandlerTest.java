package br.com.dicasdeumdev.api.resources.exceptions;

import br.com.dicasdeumdev.api.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ResourceEcxeptionHandlerTest {

    public static final String OBJETO_NÃO_ENCONTRADO = "Objeto não encontrado";
    @InjectMocks
    private ResourceEcxeptionHandler ecxeptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarResponseEntity_quuandoObjetoNaoForEncontrao() {
        ResponseEntity<StandardError> response = ecxeptionHandler
                .objectNotFound(new ObjectNotFoundException(OBJETO_NÃO_ENCONTRADO), new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertTrue(response.getBody().getError().contains(OBJETO_NÃO_ENCONTRADO));
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void dataIntegrityViolationException() {
    }
}