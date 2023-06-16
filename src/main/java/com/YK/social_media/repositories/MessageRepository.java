package com.YK.social_media.repositories;

import com.YK.social_media.models.Friend;
import com.YK.social_media.models.Message;
import com.YK.social_media.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySender(User user);
    List<Message> findByReceiver(User user);
}
