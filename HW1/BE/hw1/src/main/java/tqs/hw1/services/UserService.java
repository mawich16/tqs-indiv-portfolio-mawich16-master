package tqs.hw1.services;

import tqs.hw1.data.User;

public interface UserService {
    User createUser(String name, String password, boolean isStaff);
    User getUser(String name, String password);
}
