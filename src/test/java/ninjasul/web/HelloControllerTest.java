package ninjasul.web;

import ninjasul.config.auth.SecurityConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = HelloController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    }
)
class HelloControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("/hello 호출 테스트")
    void hello() throws Exception {
        String expected = "hello";

        mockMvc.perform(get("/hello"))
            .andExpect(status().isOk())
            .andExpect(content().string(expected));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("/hello/dto 호출 테스트")
    void helloDto() throws Exception {
        String expectedName = "hello";
        int expectedAmount = 1000;

        mockMvc.perform(get("/hello/dto")
                            .param("name", expectedName)
                            .param("amount", String.valueOf(expectedAmount)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", Matchers.is(expectedName)))
            .andExpect(jsonPath("$.amount", Matchers.is(expectedAmount)));
    }
}