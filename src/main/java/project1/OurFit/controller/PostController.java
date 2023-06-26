package project1.OurFit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project1.OurFit.response.GetPostDto;
import project1.OurFit.service.PostService;
import project1.constant.response.JsonResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @GetMapping("/post")
    @ResponseBody
    public JsonResponse<List<GetPostDto>> post() {
        return new JsonResponse<>(postService.getAllPost());
    }
}
