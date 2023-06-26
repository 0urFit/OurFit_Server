package project1.OurFit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project1.OurFit.repository.PostRepository;
import project1.OurFit.response.GetPostDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<GetPostDto> getAllPost() {
        return postRepository.findAll().stream()
                .map(post -> new GetPostDto(
                        post.getId(),
                        post.getCategory(),
                        post.getGender(),
                        post.getTitle(),
                        post.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
