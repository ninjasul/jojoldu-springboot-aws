package ninjasul.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostsRepositoryTest {

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
}