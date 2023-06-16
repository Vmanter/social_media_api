package com.YK.social_media.controllers;

import com.YK.social_media.models.Post;
import com.YK.social_media.models.User;
import com.YK.social_media.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name="Публикации")
public class PostController {
    private final PostService postService;

    @Operation(
            summary = "Просмотр ленты постов (публикации)",
            description = "По этому endpointу можно видеть публикации, на которых подписан пользователь, а также свежие публикации других пользователей, на которых можно подписаться в бдущем. Посты выводятся с учетом пагинации по 10 постов на страницу.",
            responses = {
                    @ApiResponse(
                            description = "Выводится список публикаций с указанием заголовка, фото, автора и даты публикации",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/")
    public String posts(@RequestParam(name = "searchWord", required = false) String title, Principal principal,
                        @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize, Model model) {

        List<Post> posts = postService.listPosts(title, principal, pageNumber, pageSize);
        int totalItems = posts.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        model.addAttribute("posts", posts);
        model.addAttribute("user", postService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        return "posts";
    }

    @Operation(
            summary = "Просмотр поста (публикации)",
            description = "Этот endpoint позволяет просмотреть публикацию по указанному ID записи",
            responses = {
                    @ApiResponse(
                            description = "Выводится Заголовок, Автор, Содержимое поста, Дата публикации",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/post/{id}")
    public String postInfo(@PathVariable Long id, Model model, Principal principal) {
        Post post = postService.getPostById(id);
        model.addAttribute("user", postService.getUserByPrincipal(principal));
        model.addAttribute("post", post);
        model.addAttribute("images", post.getImages());
        model.addAttribute("authorPost", post.getUser());
        return "post";
    }

    @Operation(
            summary = "Добавление поста (публикации)",
            description = "Этот endpoint позволяет добавить пост (публикацию) в систему",
            responses = {
                    @ApiResponse(
                            description = "Обновление страницы с успешным добавлением записи",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/post/create")
    public String createPost(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                             @RequestParam("file3") MultipartFile file3, Post post, Principal principal) throws IOException {
        postService.savePost(principal, post, file1, file2, file3);
        return "redirect:/my/posts";
    }

    @Operation(
            summary = "Удаление поста (публикации)",
            description = "Этот endpoint позволяет удалить пост (публикацию) из системы по заданному ID. Удалить публикацию может только ее автор",
            responses = {
                    @ApiResponse(
                            description = "Удаление записи из системы",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token / Ошибка удаления публикации, если удаляет не автор",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id, Principal principal) {
        postService.deletePost(postService.getUserByPrincipal(principal), id);
        return "redirect:/my/posts";
    }

    @Operation(
            summary = "Список постов (публикаций) пользователя",
            description = "Endpoint вывода всех публикаций пользователя",
            responses = {
                    @ApiResponse(
                            description = "Выводятся все публикации пользователя с обратной сортировкой по дате добавления",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token / Ошибка удаления публикации, если удаляет не автор",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/my/posts")
    public String userPosts(Principal principal, Model model) {
        User user = postService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("posts", user.getPosts());
        return "my-posts";
    }

}
