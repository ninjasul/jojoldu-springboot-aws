package ninjasul.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ninjasul.domain.posts.Posts;
import ninjasul.domain.posts.PostsRepository;
import ninjasul.web.dto.PostsSaveRequestDto;
import ninjasul.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {
    @LocalServerPort
    private int port;

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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Posts 등록 테스트")
    public void postPosts() throws Exception {
        // given
        String expectedTitle = "title";
        String expectedContent = "content";

        PostsSaveRequestDto requestDto = buildSaveRequestDto(
            expectedTitle,
            expectedContent,
            "author"
        );

        String url = "http://localhost:" + port + "/api/v1/posts";

        // when
        mockMvc.perform(setPostSaveRequest(url, requestDto))
            .andExpect(status().isOk());


        // then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    private PostsSaveRequestDto buildSaveRequestDto(
        String title,
        String content,
        String author
    ) {
        return PostsSaveRequestDto.builder()
            .title(title)
            .content(content)
            .author(author)
            .build();
    }

    private MockHttpServletRequestBuilder setPostSaveRequest(String url, PostsSaveRequestDto requestDto) throws JsonProcessingException {
        return post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(requestDto));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Posts 수정 테스트")
    public void updatePosts() throws Exception {
        // given
        Posts savedPosts = postsRepository.save(buildPost());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = buildUpdateRequestDto(expectedTitle, expectedContent);

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        // when
        mockMvc.perform(setPostUpdateRequest(url, requestDto))
            .andExpect(status().isOk());

        // then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    private Posts buildPost() {
        return Posts.builder()
            .title("title")
            .content("content")
            .author("author")
            .build();
    }

    private PostsUpdateRequestDto buildUpdateRequestDto(String expectedTitle, String expectedContent) {
        return PostsUpdateRequestDto.builder()
            .title(expectedTitle)
            .content(expectedContent)
            .build();
    }

    private RequestBuilder setPostUpdateRequest(String url, PostsUpdateRequestDto requestDto) throws JsonProcessingException {
        return put(url)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(objectMapper.writeValueAsString(requestDto));
    }
}