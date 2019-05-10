package org.springframework.httprestservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.httprestservice.model.Group;
import org.springframework.httprestservice.model.User;
import org.springframework.httprestservice.service.UserService;
import org.springframework.httprestservice.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @RequestMapping(value="/query", method = RequestMethod.GET)
    public List<User> getUsersWithOptions(
            @RequestParam(value="name", required = false, defaultValue = "") String name,
            @RequestParam(value="uid", required = false, defaultValue = "") String uid,
            @RequestParam(value="gid", required = false, defaultValue = "") String gid,
            @RequestParam(value="comment", required = false, defaultValue = "") String comment,
            @RequestParam(value="home", required = false, defaultValue = "") String home,
            @RequestParam(value="shell", required = false, defaultValue = "") String shell)
    {
        String[] options = {name, uid, gid, comment, home, shell};
        return userService.getUsersByOptions(options);
    }


    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public User getUser(@PathVariable("uid") String uid) {
        User user = userService.getUserByUid(uid);
        if (user != null){
            return user;
        } else {
            throw new UserNotFoundException();
        }
    }

    @RequestMapping(value = "/{uid}/groups", method = RequestMethod.GET)
    public List<Group> getGroupsByUid(@PathVariable(value = "uid") String uid){
        return userService.getGroupsByUid(uid);
    }
}