package com.YK.social_media.services;

import com.YK.social_media.models.Friend;
import com.YK.social_media.models.Post;
import com.YK.social_media.models.User;
import com.YK.social_media.models.enums.FriendStatus;
import com.YK.social_media.repositories.FriendRepository;
import com.YK.social_media.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    FriendRepository friendRepository;
    UserRepository userRepository;

    public void saveFriend(User sender, User receiver, FriendStatus status) {
        Friend friend = new Friend();
        User first = sender;
        User second = receiver;

        if (!friendRepository.existsBySenderAndReceiver(first,second)) {
            friend.setSender(first);
            friend.setReceiver(second);
            friend.setStatus(status);
            friendRepository.save(friend);
        }
    }

    public void deleteFriend(User sender, User receiver) {
        User first = sender;
        User second = receiver;

        Friend myfriend = friendRepository.findBySenderAndReceiver(second, first);
        if (myfriend != null) {
            myfriend.setStatus(FriendStatus.DECLINED);
            friendRepository.save(myfriend);
        }
        Friend friend = friendRepository.findBySenderAndReceiver(first,second);
        if (friend != null) {
            friendRepository.delete(friend);
        }
    }

    public List<User> getFriends(User user) {
        List<Friend> friendsFirst = friendRepository.findBySender(user);
        List<Friend> friendsSecond = friendRepository.findByReceiver(user);
        List<User> friendUsers = new ArrayList<>();

        for (int i = friendsFirst.size() - 1; i >= 0; i--) {
            Friend friend = friendsFirst.get(i);
            if (friend.getStatus() == FriendStatus.ACCEPTED) {
                friendUsers.add(friend.getReceiver());
            }
        }

        for (int i = friendsSecond.size() - 1; i >= 0; i--) {
            Friend friend = friendsSecond.get(i);
            if (friend.getStatus() == FriendStatus.ACCEPTED) {
                friendUsers.add(friend.getSender());
            }
        }

        return friendUsers.stream().distinct().collect(Collectors.toList());
    }

    public List<User> getFollows(User user) {
        List<Friend> friendsFirst = friendRepository.findBySender(user);
        List<User> friendUsers = new ArrayList<>();

        for (int i = friendsFirst.size() - 1; i >= 0; i--) {
            Friend friend = friendsFirst.get(i);
            friendUsers.add(friend.getReceiver());
        }

        return friendUsers;
    }

    public List<User> getFollowers(User user) {
        List<Friend> friendsSecond = friendRepository.findByReceiver(user);
        List<User> friendUsers = new ArrayList<>();

        for (int i = friendsSecond.size() - 1; i >= 0; i--) {
            Friend friend = friendsSecond.get(i);
            friendUsers.add(friend.getSender());
        }

        return friendUsers;
    }

    public boolean isFriends(User sender, User receiver) {
        boolean is_friends = false;
        if (sender != null && receiver != null) {
            Friend friend = friendRepository.findBySenderAndReceiver(sender, receiver);
            if ((friend != null) && (friend.getStatus() == FriendStatus.ACCEPTED)) {
                is_friends = true;
            }
        }

        return  is_friends;
    }

    public boolean isFollower(User sender, User receiver) {
        boolean is_friends = false;
        if (sender != null && receiver != null) {
            Friend friend = friendRepository.findBySenderAndReceiver(sender, receiver);
            if (friend != null) {
                is_friends = true;
            }
        }

        return  is_friends;
    }

    public List<User> askedFriend(User receiver) {
        List<User> asked = new ArrayList<>();
        for (int i = friendRepository.findByReceiver(receiver).size() - 1; i >= 0; i--) {
            Friend friend = friendRepository.findByReceiver(receiver).get(i);
            if (friend.getStatus() == FriendStatus.REQUESTED) {
                asked.add(friend.getSender());
            }
        }

        return  asked;
    }
}
