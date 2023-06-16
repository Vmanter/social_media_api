package com.YK.social_media.controllers;

import com.YK.social_media.models.User;
import com.YK.social_media.services.FriendService;
import com.YK.social_media.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Tag(name = "Аутентификация и авторизация")
public class UserController {
    private final UserService userService;
    private final FriendService friendService;

    @Operation(
            summary = "Авторизация пользователя",
            description = "Этот endpoint позволяет зарегистрированным пользователям войти в систему. Во избежания сканирование базы злоумышленниками на наличие пользователей или подбора паролей подробные сообщения о несущетсвующих пользователях или неправильных паролях не выводятся",
            responses = {
                    @ApiResponse(
                            description = "Переадресация пользователя на главную страницу с доступом авторизированного пользователя",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/login")
    public String login(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "login";
    }

    @Operation(
            summary = "Просмотр собственного профиля",
            description = "Этот endpoint позволяет просмотреть собственный профиль и все доступные функции профиля для авторизированного пользователя.",
            responses = {
                    @ApiResponse(
                            description = "Переадресация на страницу профиля пользователя",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "profile";
    }

    @Operation(
            summary = "Переход на страницу регистрации",
            description = "Endpoint перехода на страницу регистрации пользователя",
            responses = {
                    @ApiResponse(
                            description = "Переадресация посетителя на страницу регистрации нового пользователя",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/registration")
    public String registration(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "registration";
    }

    @Operation(
            summary = "Регистрация на сайте",
            description = "Этот endpoint позволяет зарегистрировать пользователя в системе. Нужно указать имя пользователя, электронную почту и пароль.",
            responses = {
                    @ApiResponse(
                            description = "Переадресация пользователя на страницу авторизации",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Ошибка регистрации пользователя, если уже такой существует в системе или введены некорректные данные",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с email: " + user.getEmail() + " уже существует!");
            return "registration";
        } else {
            return "redirect:/login";
        }
    }

    @Operation(
            summary = "Просмотр профиля пользователя",
            description = "Этот endpoint позволяет посмотреть профиль пользователя. Отображаются все публикации пользователя. Так же можно здесь предложить дружбу и подписаться на пользователя",
            responses = {
                    @ApiResponse(
                            description = "Переадресация на страницу просмотра профиля пользователя с доступом авторизированного пользователя",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model, Principal principal) {
        User myuser = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("userByPrincipal", myuser);
        model.addAttribute("posts", user.getPosts());
        model.addAttribute("is_friends", friendService.isFriends(myuser, user));
        model.addAttribute("is_follower", friendService.isFollower(myuser, user));
        return "user";
    }

    @Operation(
            summary = "Подружиться с пользователя (подписка)",
            description = "Этот endpoint позволяет отправить запрос на дружбу пользователю и подписаться на его публикации",
            responses = {
                    @ApiResponse(
                            description = "Отправка запроса на дружбу и подписка на публикации",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token / Нельзя подружиться с самим собой или отправить себе сообщение",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/user/{user}/follow")
    public String userFollow(@PathVariable("user") User user, Model model, Principal principal) {
        userService.followTo(user, principal);
        return userInfo(user, model, principal);
    }

    @Operation(
            summary = "Удалить из друзей (отписаться)",
            description = "Этот endpoint позволяет удалить пользователя из друзей или прекратить подписку на его публикации. Функции реализованы из формы просмотра собственного профиля.",
            responses = {
                    @ApiResponse(
                            description = "Смена статуса дружбы на \"Отказано\" или удаление связи дружбы, если отказ уже был ранее или пользователь сам был инициатором дружбы",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/user/{user}/unfriend")
    public String userUnFriend(User user, Model model, Principal principal) {
        userService.unFollow(user, principal);
        return userInfo(user, model, principal);
    }

    @Operation(
            summary = "Удалить из друзей (отписаться) при просмотре профиля пользователя",
            description = "Этот endpoint позволяет удалить пользователя из друзей или прекратить подписку на его публикации. Функции реализованы при просмотре профиля пользователя.",
            responses = {
                    @ApiResponse(
                            description = "Смена статуса дружбы на \"Отказано\" или удаление связи дружбы, если отказ уже был ранее или пользователь сам был инициатором дружбы",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/user/{user}/unfollow")
    public String userUnFollow(@PathVariable("user") User user, Model model, Principal principal) {
        userService.unFollow(user, principal);
        return userInfo(user, model, principal);
    }

    @Operation(
            summary = "Отправка сообщения пользователю при просмотре его профиля",
            description = "Этот endpoint позволяет отправить сообщение пользователю непосредственно из страницы просмотра его профиля. Функция аналогична, как при отправке из пункта \"Сообщения\" собственного профиля",
            responses = {
                    @ApiResponse(
                            description = "Отправленное сообщение добавляется в систему с меткой SENDED",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/user/{user}/sendmessage")
    public String sendMessage(@PathVariable("user") User user, Model model, Principal principal) {
        userService.sendMessage(user, principal);
        return userInfo(user, model, principal);
    }

}
