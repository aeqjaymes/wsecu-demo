package com.aequilibrium.wsecu.controller;

import com.aequilibrium.wsecu.data.entity.User;
import com.aequilibrium.wsecu.dto.UserCreate;
import com.aequilibrium.wsecu.dto.UserUpdate;
import com.aequilibrium.wsecu.exception.EntityNotFoundException;
import com.aequilibrium.wsecu.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Supplier<EntityNotFoundException> USER_NOT_FOUND = () -> new EntityNotFoundException("User not found");

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Return a user by their username.
     *
     * Note: This is just an example -- in the real world we would use a json property filter, json view, dto, partial select,
     * entity mapping, etc. We would also do additional security checks to determine if the user has permission to view and/or
     * return different variations of the request profile
     */
    @GetMapping(path = "", params = "username")
    @ApiOperation(value = "Find a user by user name")
    public User getUserByUsername(@RequestParam("username") String username) {
        // Note: We can use filter binding to bind this to a repository query in the method (querydsl) -- kept this way for simplicity
        Optional<User> user = userService.findByUsername(username);
        return user.orElseThrow(USER_NOT_FOUND);
    }

    /**
     * Return a user by their id.
     *
     * Note: As with #getUserByUsername, same assumptions apply
     */
    @GetMapping(path = "/{userId}")
    @ApiOperation(value = "Find a user by user id")
    public User getUserById(@PathVariable("userId") Optional<User> entity) {
        return entity.orElseThrow(USER_NOT_FOUND);
    }

    /**
     * Update an existing user's details.
     *
     * Note: This would have some additional security checks
     */
    @PutMapping(path = "/{userId}")
    @ApiOperation(value = "Update a user")
    public User postUser(@PathVariable("userId") Long userId, @RequestBody @Validated UserUpdate userUpdate) {
        userUpdate.setId(userId);
        return userService.updateUser(userUpdate);
    }

    /**
     * Create a new user.
     *
     * Note: This would have some additional security checks
     */
    @PostMapping(path = "/")
    @ApiOperation(value = "Create a new user")
    @ResponseStatus(HttpStatus.CREATED)
    public User postUser(@RequestBody @Validated UserCreate user) {
        return userService.createUser(user);
    }

    /**
     * Delete the supplied user.
     *
     * Note: This would have some additional security checks and would ideally be idempotent
     */
    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a user by user id")
    public void deleteUserById(@PathVariable("userId") Optional<User> entity) {
        User user = entity.orElseThrow(USER_NOT_FOUND);

        userService.deleteUserById(user.getId());
    }
}
