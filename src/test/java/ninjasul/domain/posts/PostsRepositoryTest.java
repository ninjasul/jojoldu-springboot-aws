package ninjasul.domain.posts;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostsRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(PostsRepositoryTest.class);

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 저장 후 불러오기")
    void saveAndGet() {
        String expectedTitle = "테스트 게시글";
        String expectedContent = "테스트 본문";

        postsRepository.save(
            buildPost(expectedTitle, expectedContent)
        );

        List<Posts> postsList = postsRepository.findAll();

        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(expectedTitle);
        assertThat(posts.getContent()).isEqualTo(expectedContent);
    }

    private Posts buildPost(String expectedTitle, String expectedContent) {
        return Posts.builder()
            .title(expectedTitle)
            .content(expectedContent)
            .author("ninjasul@gmail.com")
            .build();
    }

    @Test
    @DisplayName("BaseTimeEntity 등록 테스트")
    void baseTimeEntity() {
        // given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        postsRepository.save(buildPosts());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);

        log.info(">>>>>>>>>> createDate = {}, modifiedDate = {}", posts.getCreatedDate(), posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

    private Posts buildPosts() {
        return Posts.builder()
            .title("title")
            .content("content")
            .author("author")
            .build();
    }
}