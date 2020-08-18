package ninjasul.web;

import ninjasul.domain.posts.Posts;
import ninjasul.domain.posts.PostsRepository;
import ninjasul.web.dto.PostsSaveRequestDto;
import ninjasul.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("Posts 등록 테스트")
    public void postPosts() {
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
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(
            url,
            requestDto,
            Long.class
        );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(1L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    @DisplayName("Posts 수정 테스트")
    public void updatePosts() {
        // given
        Posts savedPosts = postsRepository.save(buildPost());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = buildUpdateRequestDto(expectedTitle, expectedContent);

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            requestEntity,
            Long.class
        );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(1L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    private PostsUpdateRequestDto buildUpdateRequestDto(String expectedTitle, String expectedContent) {
        return PostsUpdateRequestDto.builder()
            .title(expectedTitle)
            .content(expectedContent)
            .build();
    }

    private Posts buildPost() {
        return Posts.builder()
            .title("title")
            .content("content")
            .author("author")
            .build();
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
}