package org.springframework.httprestservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.httprestservice.model.Group;
import org.springframework.httprestservice.model.User;
import org.springframework.httprestservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository = new UserRepository();

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserByUid(String uid) {
        return userRepository.getUserByUid(uid);
    }

    public List<User> getUsersByOptions(String[] options){
        return userRepository.getUsersByOptions(options);
    }

    public List<Group> getGroupsByUid(String uid){
        return userRepository.getGroupsByUid(uid);
    }
}