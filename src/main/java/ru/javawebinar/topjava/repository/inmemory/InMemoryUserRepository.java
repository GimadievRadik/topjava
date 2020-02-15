package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public InMemoryUserRepository() {
        repository.put(counter.incrementAndGet(), new User(counter.get(), "Vanya", "vanya@mail.ru", "vanyapass", Role.ROLE_USER));
        repository.put(counter.incrementAndGet(), new User(counter.get(), "Petya", "petya@yandex.ru", "petyapass", Role.ROLE_USER));
        repository.put(counter.incrementAndGet(), new User(counter.get(), "Misha", "misha@gmail.com", "mikepass", Role.ROLE_ADMIN));
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> allUsers = new ArrayList<>(repository.values());
        Comparator<User> comparator = new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                if (u1.getName().compareTo(u2.getName()) == 0) {
                    return u1.getEmail().compareTo(u2.getEmail());
                }
                return u1.getName().compareTo(u2.getName();
            }
        };
        allUsers.sort(comparator);
        return allUsers;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        List<User> users = repository.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
        return users.isEmpty() ? null : users.get(0);
    }
}
