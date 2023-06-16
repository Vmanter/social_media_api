package com.YK.social_media.repositories;

import com.YK.social_media.models.Friend;
import com.YK.social_media.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findBySenderAndReceiver(User sender, User receiver);
    boolean existsBySenderAndReceiver(User sender, User receiver);

    List<Friend> findBySender(User user);
    List<Friend> findByReceiver(User user);
}
