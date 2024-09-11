package br.com.pedrobotolli.demo.resources;

import java.util.UUID;
import java.util.List;
import java.util.Objects;
import java.util.Arrays;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pedrobotolli.demo.entities.User;
import br.com.pedrobotolli.demo.repositories.UserRepository;

@WebMvcTest(UserResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserRepository userRepository;

    @Test
    @Order(1)
    void givenAValidUser_whenCallsCreateUser_shouldReturnCreatedUser() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User("Pedro", "pedro@email.com", "senha");
        user.setId(id);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(user));

        ResultActions response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(user.getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.equalTo(user.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.equalTo(user.getPassword())));

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.argThat(u -> Objects.equals(user.getName(), u.getName())
                        && Objects.equals(user.getUsername(), u.getUsername())
                        && Objects.equals(user.getPassword(), u.getPassword())
                        && Objects.equals(user.getId(), u.getId())));
    }

    @Test
    @Order(2)
    void givenUsersExist_whenCallsGetAllUsers_shouldReturnListOfUsers() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User user1 = new User("Pedro", "pedro@email.com", "senha");
        user1.setId(id1);

        User user2 = new User("Ana", "ana@email.com", "senha123");
        user2.setId(id2);

        List<User> userList = Arrays.asList(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(userList);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        ResultActions response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.equalTo(userList.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.equalTo(user1.getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.equalTo(user1.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username", Matchers.equalTo(user1.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.equalTo(user2.getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.equalTo(user2.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username", Matchers.equalTo(user2.getUsername())));

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    @Order(3)
    void givenAUserExist_whenCallsGetUserById_shouldReturnAUser() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User("Pedro", "pedro@email.com", "senha");
        user.setId(id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        ResultActions response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(user.getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.equalTo(user.getUsername())));

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
    }

    @Test
    @Order(4)
    void givenAUserExist_whenCallsUpdateUser_shouldReturnTheUpdatedUser() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User("Pedro", "pedro@email.com", "senha");
        user.setId(id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(user));

        ResultActions response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(user.getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.equalTo(user.getUsername())));

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    @Order(4)
    void givenAUserExist_whenCallsDeleteUser_shouldReturnTheDeletedUser() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User("Pedro", "pedro@email.com", "senha");
        user.setId(id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        ResultActions response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(user.getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(user.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.equalTo(user.getUsername())));

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }
}
