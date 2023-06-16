package com.YK.social_media.services;

import com.YK.social_media.models.Friend;
import com.YK.social_media.models.User;
import com.YK.social_media.models.enums.FriendStatus;
import com.YK.social_media.models.enums.Role;
import com.YK.social_media.repositories.FriendRepository;
import com.YK.social_media.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final FriendService friendService;
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.getRoles().add(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Сохранение нового пользователя с email: {}", email);
        userRepository.save(user);
        return true;
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public void userBan(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Пользователь id {}; с email {} заблокирован", user.getId(), user.getEmail());
            } else {
                user.setActive(true);
                log.info("Пользователь id {}; с email {} разблокирован", user.getId(), user.getEmail());
            }
        }
        userRepository.save(user);
    }

    public void changeUserRoles(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    public void followTo(User user, Principal principal) {
        User sender = getUserByPrincipal(principal);
        User receiver = user;
        if (!sender.equals(receiver)) {
            Friend friend = friendRepository.findBySenderAndReceiver(receiver, sender);
            if ((friend != null) && (friend.getStatus() != FriendStatus.ACCEPTED)) {
                friend.setStatus(FriendStatus.ACCEPTED);
                friendRepository.save(friend);
                friendService.saveFriend(sender, receiver, FriendStatus.ACCEPTED);
            } else {
                friendService.saveFriend(sender, receiver, FriendStatus.REQUESTED);
            }

        }
    }

    public void unFollow(User user, Principal principal) {
        User sender = getUserByPrincipal(principal);
        User receiver = user;
        if (!sender.equals(receiver)) {
            friendService.deleteFriend(sender, receiver);
        }
    }

    public void sendMessage(User user, Principal principal) {
        User sender = getUserByPrincipal(principal);
        User receiver = user;
        if (!sender.equals(receiver)) {
            messageService.sendMessage(sender, receiver);
        }
    }
}
