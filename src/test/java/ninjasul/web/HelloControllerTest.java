package ninjasul.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/hello 호출 테스트")
    void hello() throws Exception {
        String expected = "hello";

        mockMvc.perform(get("/hello"))
            .andExpect(status().isOk())
            .andExpect(content().string(expected));
    }

    @Test
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