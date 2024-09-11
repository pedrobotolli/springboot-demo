package br.com.pedrobotolli.demo.resources;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(HelloResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HelloResourceTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @Order(1)
    void whenCallsHello_shouldReturnHelloWorld() throws Exception {;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/")
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        ResultActions response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo("Hello World!")));
    }

}
