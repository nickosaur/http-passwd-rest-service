package org.springframework.httprestservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.httprestservice.exception.GroupNotFoundException;
import org.springframework.httprestservice.model.Group;
import org.springframework.httprestservice.service.GroupService;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService = new GroupService();

    @GetMapping
    public List<Group> getGroups(){
        return groupService.getGroups();
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public List<Group> getGroupsByOptions(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "gid", required = false, defaultValue = "") String gid,
            @RequestParam(value = "member", required = false) String[] members)
    {

        String[] options = {name, gid};
        if (members == null){
            members = new String[0];
        }
        return groupService.getGroupsByOptions(options, members);
    }

    @RequestMapping(value = "/{gid}", method = RequestMethod.GET)
    public Group getGroupByGid(@PathVariable("gid") String gid){
        Group grp = groupService.getGroupByGid(gid);
        if (grp != null){
            return grp;
        }
        throw new GroupNotFoundException();
    }
}
