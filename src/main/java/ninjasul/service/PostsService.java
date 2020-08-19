package ninjasul.service;

import lombok.RequiredArgsConstructor;
import ninjasul.domain.posts.Posts;
import ninjasul.domain.posts.PostsRepository;
import ninjasul.web.dto.PostsListResponseDto;
import ninjasul.web.dto.PostsResponseDto;
import ninjasul.web.dto.PostsSaveRequestDto;
import ninjasul.web.dto.PostsUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = findPostById(id);
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
            .map(PostsListResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = findPostById(id);
        postsRepository.delete(posts);
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = findPostById(id);
        return new PostsResponseDto(entity);
    }

    private Posts findPostById(Long id) {
        return postsRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + id));
    }
}
