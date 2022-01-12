package br.com.dicasdeumdev.api.services.impl;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.domain.dto.UserDTO;
import br.com.dicasdeumdev.api.repositories.UserRepository;
import br.com.dicasdeumdev.api.services.exceptions.DataIntegratyViolationException;
import br.com.dicasdeumdev.api.services.exceptions.ObjectNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    public static final int ID = 1;
    public static final String NAME = "wenner";
    public static final String EMAIL = "wenner@gmail.com";
    public static final String PASSWORD = "123";
    public static final String OBJETO_NÃO_ENCONTRADO = "Objeto não encontrado";
    public static final int INDEX = 0;
    public static final String E_MAIL_JÁ_CADASTRADO_NO_SISTEMA = "E-mail já cadastrado no sistema";

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    @Mock
    private ModelMapper mapper;

    private User user;
    private UserDTO userDTO;
    private Optional<User> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void deveRetornarUmaInstaciaDeUsuario_aofazerBuscaPorID() {
        when(repository.findById(anyInt())).thenReturn(optionalUser);

        User response = service.findById(ID);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());

        verificaAtributos(response);
    }

    @Test
    void deveRetonarObjectNotFoundException_aoBuscarPorID_eUsuarioNaoExistir() {
        when(repository.findById(ID)).thenThrow(new ObjectNotFoundException(OBJETO_NÃO_ENCONTRADO));

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(ID));

        assertThrows(ObjectNotFoundException.class, () -> service.findById(ID));
        assertTrue(exception.getMessage().contains(OBJETO_NÃO_ENCONTRADO));
        assertEquals(ObjectNotFoundException.class, exception.getClass());
    }

    @Test
    void deveRetornarUmaListadeUsuario_aoBuscarTodos() {
        when(repository.findAll()).thenReturn(List.of(user));

        List<User> response = service.findAll();

        assertNotNull(response);
        assertEquals( 1, response.size());
        assertEquals(User.class, response.get(INDEX).getClass());

        verificaAtributos(response.get(INDEX));
    }

    @Test
    void deveCriarUmUsuarioComSucesso() {
        when(repository.save(any())).thenReturn(user);

        User response = service.create(userDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());

        verificaAtributos(response);
    }

    @Test
    void deveLancarDataIntegrityViolationException_aoCriarUmUsuario() {
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);
        optionalUser.get().setId(2);

        Exception ex = assertThrows(DataIntegratyViolationException.class, () -> service.create(userDTO));

        assertEquals(DataIntegratyViolationException.class, ex.getClass());
        assertTrue(ex.getMessage().contains(E_MAIL_JÁ_CADASTRADO_NO_SISTEMA));
    }

    private void verificaAtributos(User response) {
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(PASSWORD, response.getPassword());
        assertEquals(EMAIL, response.getEmail());
    }

    private void startUser() {
        user = new User(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
        optionalUser = Optional.of(new User(ID, NAME, EMAIL, PASSWORD));
    }
}