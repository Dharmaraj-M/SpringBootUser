package com.example.springbootuser.controller;

import com.example.springbootuser.aop.InvalidHeaderException;
import com.example.springbootuser.dto.UserPatch;
import com.example.springbootuser.entity.UserEntity;
import com.example.springbootuser.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<Page<UserEntity>> getAllUsers(@RequestHeader(value = "Authorization") String auth,
                                                        @RequestParam Optional<Integer> page,
                                                        @RequestParam Optional<Integer> size) throws Exception {
        if (!auth.equals("true")) {
            throw new InvalidHeaderException("Unauthorized Access.");
        }
        return status(HttpStatus.OK).body(userService.getAllUsers(page, size));
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<Optional<UserEntity>> getUserById(@RequestHeader(value = "Authorization") String auth,
                                                            @PathVariable Long id) throws Exception {
        if (!auth.equals("true")) {
            throw new InvalidHeaderException("Unauthorized Access.");
        }
        return status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<UserEntity> createNewUser(@RequestHeader(value = "Authorization") String auth,
                                                    @RequestBody UserEntity user) throws Exception {
        if (!auth.equals("true")) {
            throw new InvalidHeaderException("Unauthorized Access.");
        }
        return status(HttpStatus.CREATED).body(userService.createNewUser(user));
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserEntity> updateUserById(@RequestHeader(value = "Authorization") String auth,
                                                     @RequestBody UserEntity user,
                                                     @PathVariable Long id) throws Exception {
        if (!auth.equals("true")) {
            throw new InvalidHeaderException("Unauthorized Access.");
        }
        return status(HttpStatus.ACCEPTED).body(userService.updateUserById(user, id));
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<String> userPatch(@RequestHeader(value = "Authorization") String auth,
                                            @RequestBody ArrayList<UserPatch> userPatch,
                                            @PathVariable Long id) throws Exception {
        if (!auth.equals("true")) {
            throw new InvalidHeaderException("Unauthorized Access.");
        }
        userService.updateUserByPatch(userPatch, id);
        return status(HttpStatus.OK).body("Successfully Updated using PATCH.");
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Optional<UserEntity>> deleteUserById(@RequestHeader(value = "Authorization") String auth,
                                                               @PathVariable Long id) throws Exception {
        if (!auth.equals("true")) {
            throw new InvalidHeaderException("Unauthorized Access.");
        }
        return status(HttpStatus.OK).body(userService.deleteUserByID(id));
    }
}
