package com.example.springbootuser.service;

import com.example.springbootuser.aop.InvalidInputException;
import com.example.springbootuser.dto.UserPatch;
import com.example.springbootuser.entity.UserEntity;
import com.example.springbootuser.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<UserEntity> getAllUsers(Optional<Integer> Page, Optional<Integer> Size) {
        return userRepository.findAll(PageRequest.of(Page.orElse(0), Size.orElse(5)));
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity createNewUser(UserEntity user) throws Exception {
        if (user.getUsername() == null || user.getEmail() == null) {
            throw new InvalidInputException("Input is not Sufficient.");
        }
        return userRepository.save(user);
    }

    public UserEntity updateUserById(UserEntity user, long id) throws Exception {
        if (user.getUsername() == null || user.getEmail() == null) {
            throw new InvalidInputException("Input is not Sufficient.");
        }
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        if (userEntityOptional.isEmpty()) {
            throw new InvalidInputException("User not Found.");
        }
        UserEntity userUpdatedEntity = userEntityOptional.get();
        userUpdatedEntity.setUsername(user.getUsername());
        userUpdatedEntity.setEmail(user.getEmail());
        return userRepository.save(userUpdatedEntity);
    }

    public Optional<UserEntity> deleteUserByID(Long id) throws Exception {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        if (userEntityOptional.isEmpty()) {
            throw new InvalidInputException("User not Found.");
        }
        userRepository.deleteById(id);
        return userEntityOptional;
    }

    public void patchAction(String action, UserPatch user, UserEntity userUpdate) throws Exception {
        if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("replace")) {
            if (user.getFieldName().equals("username")) {
                if (user.getValue().isEmpty()) {
                    throw new InvalidInputException("Input is invalid.");
                }
                userUpdate.setUsername(user.getValue());
            } else if (user.getFieldName().equals("email")) {
                if (user.getValue().isEmpty()) {
                    throw new InvalidInputException("Input is invalid.");
                }
                userUpdate.setEmail(user.getValue());
            } else {
                throw new InvalidInputException("Input is invalid.");
            }
        } else if (action.equalsIgnoreCase("delete")) {
            if (user.getFieldName().equals("username")) {
                userUpdate.setUsername("");
            } else if (user.getFieldName().equals("email")) {
                userUpdate.setEmail("");
            } else {
                throw new InvalidInputException("Input is invalid.");
            }
        }
        userRepository.save(userUpdate);
    }

    public void updateUserByPatch(ArrayList<UserPatch> userPatch, Long id) throws Exception {
        if (userPatch.isEmpty()) {
            throw new InvalidInputException("Input is not Sufficient.");
        }
        Optional<UserEntity> userUpdate = userRepository.findById(id);
        if (userUpdate.isEmpty()) {
            throw new InvalidInputException("User not Found.");
        }
        for (UserPatch user : userPatch) {
            if (user.getAction().equals("add") || user.getAction().equals("replace") || user.getAction().equals("delete")) {
                patchAction(user.getAction(), user, userUpdate.get());
            } else {
                throw new InvalidInputException("Input is invalid");
            }
        }
    }
}
