package com.YK.social_media.controllers;

import com.YK.social_media.models.Message;
import com.YK.social_media.models.User;
import com.YK.social_media.services.FriendService;
import com.YK.social_media.services.MessageService;
import com.YK.social_media.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Tag(name = "Сообщения")
public class MessageController {
    @Autowired
    UserService userService;
    @Autowired
    FriendService friendService;
    @Autowired
    MessageService messageService;

    @Operation(
            summary = "Список сообщений пользователя",
            description = "Endpoint вывода всех сообщений пользователя. Входящие + список друзей, которым можно отправить сообщения",
            responses = {
                    @ApiResponse(
                            description = "Выводятся все сообщения пользователя с обратной сортировкой по последним сообщениям",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/my/messages")
    public String userMessages(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("friends", friendService.getFriends(user));
        model.addAttribute("messages", messageService.receivedMessages(user));
        return "my-messages";
    }

    @Operation(
            summary = "Отправка сообщения пользователю",
            description = "Endpoint отправки сообщения \"Hello, { Имя пользователя }. How are you?\" пользователю.",
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
    @PostMapping("/user/{user}/send")
    public String send(User user, Model model, Principal principal) {
        userService.sendMessage(user, principal);
        return userMessages(principal, model);
    }

    @Operation(
            summary = "Смена статуса сообщения на \"Прочитано\"",
            description = "Endpoint меняеют статус выбранного входящего сообщения на \"Прочитано\"",
            responses = {
                    @ApiResponse(
                            description = "Статус входящего сообщения меняется на READED",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/message/{message}/read")
    public String readMessage(@PathVariable("message") Message message, Model model, Principal principal) {
        User receiver = userService.getUserByPrincipal(principal);
        messageService.readMessage(message, receiver);
        return userMessages(principal, model);
    }

    @Operation(
            summary = "Удаление входящего сообщения",
            description = "Endpoint удаления выбранного входящего сообщения",
            responses = {
                    @ApiResponse(
                            description = "Статус входящего сообщения меняется на DELETED",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token / Нельзя удалить сообщение, если пользователь не является адресатом этого сообщения",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/message/{message}/delete")
    public String deleteMessage(@PathVariable("message") Message message, Model model, Principal principal) {
        User receiver = userService.getUserByPrincipal(principal);
        messageService.deleteMessage(message, receiver);
        return userMessages(principal, model);
    }

}
