package com.YK.social_media.services;

import com.YK.social_media.models.Friend;
import com.YK.social_media.models.Message;
import com.YK.social_media.models.User;
import com.YK.social_media.models.enums.FriendStatus;
import com.YK.social_media.models.enums.MessageStatus;
import com.YK.social_media.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    public void sendMessage(User sender, User receiver) {
        Message message = new Message();

        message.setSender(sender);
        message.setReceiver(receiver);
        message.setStatus(MessageStatus.SENDED);
        message.setMessage("Hello, " + receiver.getNickname() + ". How are you?");
        messageRepository.save(message);
    }

    public List<Message> receivedMessages(User receiver) {
        List<Message> received = new ArrayList<>();
        for (int i = messageRepository.findByReceiver(receiver).size() - 1; i >= 0; i--) {
            Message message = messageRepository.findByReceiver(receiver).get(i);
            if (message.getStatus() == MessageStatus.SENDED) {
                received.add(message);
            }
        }

        return received;
    }

    public void readMessage(Message message, User receiver) {
        if (message.getReceiver() == receiver) {
            message.setStatus(MessageStatus.READED);
            messageRepository.save(message);
        }
    }

    public void deleteMessage(Message message, User receiver) {
        if (message.getReceiver() == receiver) {
            message.setStatus(MessageStatus.DELETED);
            messageRepository.save(message);
        }
    }
}
