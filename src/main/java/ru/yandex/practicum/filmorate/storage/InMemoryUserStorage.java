package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friendsPerUser = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    @Override
    public List<Long> getFriends(Long id) {
        return new ArrayList<>(friendsPerUser.get(id));
    }

    @Override
    public void addFriend(Long id, Long friendID) {
        friendsPerUser.get(id).add(friendID);
        friendsPerUser.get(friendID).add(id);
    }

    @Override
    public void removeFriend(Long id, Long friend) {
        friendsPerUser.get(id).remove(friend);
        friendsPerUser.get(friend).remove(id);
    }

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        friendsPerUser.put(user.getId(), new HashSet<>());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ObjectNotFoundException("User");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("User");
        }
        for (Long friendsId : friendsPerUser.get(id)) {
            friendsPerUser.get(friendsId).remove(id); //Удаляем пользователя у всех его друзей из друзей
        }
        friendsPerUser.remove(id);
        users.remove(id);
    }

    @Override
    public boolean contains(User user) {
        return users.containsValue(user);
    }

    @Override
    public boolean contains(Long id) {
        return users.containsKey(id);
    }
}
