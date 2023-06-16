package com.YK.social_media.controllers;

import com.YK.social_media.models.User;
import com.YK.social_media.services.FriendService;
import com.YK.social_media.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Tag(name = "Взаимодействие пользователей")
public class FriendController {
    @Autowired
    FriendService friendService;
    @Autowired
    UserService userService;

    @Operation(
            summary = "Просмотр друзей авторизированного пользователя",
            description = "Endpoint просмотра друзей авторизированного пользователя, а также отображение входящих заявок в друзья.",
            responses = {
                    @ApiResponse(
                            description = "Вывод списка пользователей",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/my/friends")
    public String userFriends(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("friends", friendService.getFriends(user));
        model.addAttribute("askeds", friendService.askedFriend(user));
        return "my-friends";
    }

    @Operation(
            summary = "Просмотр подписок пользователя",
            description = "Endpoint просмотра подписок авторизированного пользователя. Дружба и подписка отличаются по своей сути. Дружба может быть отклонена другим пользователем, но при этом подписка остается.",
            responses = {
                    @ApiResponse(
                            description = "Вывод списка подписок",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/my/follows")
    public String userFollows(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("friends", friendService.getFollows(user));
        return "my-follows";
    }

    @Operation(
            summary = "Просмотр подписчиков пользователя",
            description = "Endpoint просмотра подписчиков авторизированного пользователя.",
            responses = {
                    @ApiResponse(
                            description = "Вывод списка подписчиков",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/my/followers")
    public String userFollowers(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("friends", friendService.getFollowers(user));
        return "my-followers";
    }

}
