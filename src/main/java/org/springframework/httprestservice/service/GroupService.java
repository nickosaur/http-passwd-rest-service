package org.springframework.httprestservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.httprestservice.model.Group;
import org.springframework.httprestservice.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository = new GroupRepository();

    public List<Group> getGroups(){
        return groupRepository.getGroups();
    }

    public Group getGroupByGid(String gid){
        return groupRepository.getGroupByGid(gid);
    }

    public List<Group> getGroupsByOptions(String[] options, String[] members){
        return groupRepository.getGroupsByOptions(options, members);
    }
}
