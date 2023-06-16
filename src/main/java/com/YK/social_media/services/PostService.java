package com.YK.social_media.services;

import com.YK.social_media.models.Image;
import com.YK.social_media.models.Post;
import com.YK.social_media.models.User;
import com.YK.social_media.repositories.ImageRepository;
import com.YK.social_media.repositories.PostRepository;
import com.YK.social_media.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final FriendService friendService;

    public List<Post> listPosts(String title, Principal principal, int pageNumber, int pageSize) {
        Sort sort = Sort.by("dateOfCreated").descending();
        List<Post> posts = new ArrayList<>();

        if (title != null) {
            posts = postRepository.findByTitle(title);
        } else if (principal != null) {
            User user = getUserByPrincipal(principal);
            List<User> followedUsers = friendService.getFollows(user);
            for (User followedUser : followedUsers) {
                List<Post> followedUserPosts = postRepository.findByUser(followedUser, sort);
                followedUserPosts.removeAll(posts);
                posts.addAll(followedUserPosts);
            }
            List<Post> allPosts = postRepository.findAll(sort);
            allPosts.removeAll(posts);
            posts.addAll(allPosts);
            return posts;
        } else {
            posts = postRepository.findAll(sort);
        }

        List<Post> pagedPosts = posts.stream()
                .skip((pageNumber - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return pagedPosts;
    }

    public void savePost(Principal principal, Post post, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        post.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;

        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            post.addImageToPost(image1);
        }

        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            post.addImageToPost(image2);
        }

        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            post.addImageToPost(image3);
        }

        log.info("Публикация сохранена. Название: {}; Автор: {}", post.getTitle(), post.getUser().getNickname());
        Post postFromDb = postRepository.save(post);
        postFromDb.setPreviewImageId(postFromDb.getImages().get(0).getId());
        postRepository.save(post);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deletePost(User user, Long id) {
        Post post = postRepository.findById(id)
                .orElse(null);
        if (post != null) {
            if (post.getUser().getId().equals(user.getId())) {
                for (Image image : post.getImages()) {
                    imageRepository.delete(image);
                }
                postRepository.delete(post);
                log.info("Пост с id = {} удален", id);
            } else {
                log.error("Пользователь: {} не имеет публикации с id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Публикация id = {} не найдена", id);
        }
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }
}
