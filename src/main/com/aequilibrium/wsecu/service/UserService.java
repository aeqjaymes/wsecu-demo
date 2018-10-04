package com.aequilibrium.wsecu.service;

import com.aequilibrium.wsecu.data.entity.QUser;
import com.aequilibrium.wsecu.data.entity.User;
import com.aequilibrium.wsecu.data.repository.UserRepository;
import com.aequilibrium.wsecu.dto.UserCreate;
import com.aequilibrium.wsecu.dto.UserUpdate;
import com.aequilibrium.wsecu.exception.EntityNotFoundException;
import com.aequilibrium.wsecu.exception.EntityValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        Assert.hasLength(username, "Supplied username cannot be empty");

        // Note: Can extract this into specification sets; kept here for simplicity
        return userRepository.findOne(QUser.user.email.equalsIgnoreCase(username));
    }

    public User createUser(UserCreate user) {
        Assert.notNull(user, "Supplied user cannot be null");

        User entity = user.toUser();

        // Note: At this point, we would encrypt (ex: bcrypt) the user's password. Skipping for simplicity

        return userRepository.save(entity);
    }

    public User updateUser(UserUpdate userUpdate) {
        Assert.notNull(userUpdate, "Supplied userUpdate cannot be null");
        Assert.notNull(userUpdate.getId(), "Supplied userUpdate id cannot be null");

        User user = userRepository.findById(userUpdate.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Note: There's a few ways to accomplish this, but we'll go for the straight forward property check-and-copy
        if (StringUtils.hasLength(userUpdate.getFirstName())) {
            user.setFirstName(userUpdate.getFirstName());
        }

        // Note: It's known that as written, there's no way to clear the user's last name -- we can change the UserUpdate to accept optionals
        // and do more sophisticated checks to determine when to update a user's last name to nothing, but we'll keep it simple
        if (StringUtils.hasLength(userUpdate.getLastName())) {
            user.setLastName(userUpdate.getLastName());
        }

        // Ensure that the username is not already taken if we are updating the user's username.
        // Note: We could let this exception propagate from the Hibernate/JPA exception after the UNIQUE constraint fails (but the message is ugly)
        if (StringUtils.hasLength(userUpdate.getUsername()) && !userUpdate.getUsername().equalsIgnoreCase(user.getUsername())) {
            if (isExistingUser(userUpdate.getUsername())) {
                throw new EntityValidationException("Username '" + userUpdate.getUsername() + "' already exists");
            }

            user.setUsername(userUpdate.getUsername());
        }

        return userRepository.save(user);
    }

    private boolean isExistingUser(String username) {
        return userRepository.exists(QUser.user.username.equalsIgnoreCase(username));
    }

    public void deleteUserById(Long id) {
        Assert.notNull(id, "Supplied id cannot be null");

        userRepository.deleteById(id);
    }
}
