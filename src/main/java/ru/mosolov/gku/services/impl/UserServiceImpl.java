package ru.mosolov.gku.services.impl;

import org.springframework.stereotype.Service;
import ru.mosolov.gku.models.User;
import ru.mosolov.gku.repository.UserRepository;
import ru.mosolov.gku.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(final String name) {

        return userRepository.findByName(name).orElseGet(() -> {
            final User errorUser = new User();
            errorUser.setSuccess(false);
            errorUser.setErrorMessage(String.format("User with name %s not found", name));
            return errorUser;
        });
    }
}
