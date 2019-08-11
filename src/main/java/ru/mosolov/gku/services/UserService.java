package ru.mosolov.gku.services;

import ru.mosolov.gku.models.User;

public interface UserService {

    User getUser(final String name);
}
