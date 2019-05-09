package org.springframework.httprestservice.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.httprestservice.model.Group;
import org.springframework.httprestservice.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.httprestservice.constant.Constant;

@Repository
public class GroupRepository {
    private List<Group> groups;
    private String groupFile;
    private Date lastLoad = new Date(0L);

    public GroupRepository(){
        groups = new ArrayList<Group>();
        if (Constant.defaultFile == true){
            this.groupFile = Constant.defaultGroup;
        } else {
            this.groupFile = Constant.systemGroup;
        }
        //TODO check if valid and loadGroup
        if(!checkIfValidGroup(this.groupFile)){
            this.groupFile = Constant.defaultGroup;
        }
        loadGroup(this.groupFile);
    }

    public List<Group> getGroups(){
        if (checkForUpdates(this.groupFile)){
            if (checkIfValidGroup(this.groupFile)){
                this.groupFile = Constant.defaultPasswd;
            }
            loadGroup(this.groupFile);
        }
        return this.groups;
    }

    public Group getGroupByGid(String gid){
        if (checkForUpdates(this.groupFile)){
            if (checkIfValidGroup(this.groupFile)){
                this.groupFile = Constant.defaultPasswd;
            }
            loadGroup(this.groupFile);
        }
        Integer _gid;
        try{
            _gid = Integer.valueOf(gid);
        } catch (Exception e){
            System.err.println(gid + " is not in numbers");
            return null;
        }
        List<Group> result = groups.stream().filter(g -> (g.getGid().equals(_gid) == true)).collect(Collectors.toList());
        if (result.size() > 0){
            return result.get(0);
        }
        else{
            return null;
        }
    }

    public List<Group> getGroupsByOptions(String[] options, String[] members){
        if (checkForUpdates(this.groupFile)){
            if (checkIfValidGroup(this.groupFile)){
                this.groupFile = Constant.defaultPasswd;
            }
            loadGroup(this.groupFile);
        }
        return groups.stream().filter(g -> (g.compareWithOptions(options, members) == true)).collect(Collectors.toList());
    }

    public List<Group> getGroupsByUserName(String userName){
        List<Group> filtered = new ArrayList<Group>();
        for (Group grp : this.groups){
            if(grp.getMembers().contains(userName)){
                filtered.add(grp);
            }
        }
        return filtered;
    }

    private void loadGroup(String groupFile){
        File file = new File(groupFile);
        if (file.exists() == false){ // guaranteed to succeed;
            System.out.println("Error : Group file does not exist!");
        }
        // load time here
        lastLoad = new Date(file.lastModified());
        this.groups = new ArrayList<Group>();
        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
        } catch (Exception e) {

            throw new IllegalArgumentException("Error : FileReader can't read " + groupFile);
        }
        BufferedReader buffer = new BufferedReader(fileReader);

        String readLine = "";
        try {
            while ((readLine = buffer.readLine()) != null) {
                System.out.println(readLine);
                if (readLine.length() == 0){
                    continue;
                }
                String[] fields = readLine.split(":", -1);
                Group grp = new Group();
                grp.setName(fields[0]);
                Integer gid = Integer.valueOf(fields[2]);
                grp.setGid(gid);
                String[] members = fields[3].split(",");
                List<String> memberList = new ArrayList<>(members.length);
                Collections.addAll(memberList, members);
                grp.setMembers(memberList);

                boolean result = this.groups.add(grp);
            }
        } catch (Exception e){ // will not be reached
            System.out.println("Error : Unable to read group file!");
        }
    }

    private boolean checkForUpdates(String groupFile){
        File file = new File(groupFile);
        Date passwdLastModified = new Date(file.lastModified());
        if (groups.size() == 0 || lastLoad.compareTo(passwdLastModified) < 0){
            return true; // that means group file has been modified and needs to be loaded again
        }
        return false;
    }

    private boolean checkIfValidGroup(String groupFile){
        File file = new File(groupFile);
        if (file.exists() == false){
            System.out.println("Error : Group file does not exist!");
            return false;
        }

        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
        } catch (Exception e) {
            System.out.println("Error : Unable to read file. Please check permissions of " + groupFile);
            return false;
        }
        BufferedReader buffer = new BufferedReader(fileReader);

        String readLine = "";
        try {
            while ((readLine = buffer.readLine()) != null) {
                if (readLine.length() == 0){
                    continue;
                }
                String[] fields = readLine.split(":", -1);
                if (fields.length != 4){
                    System.out.println("Error : Group file not in the right format");
                    System.out.println("The expected format of group file should be:");
                    System.out.println("name:password:gid:members" );
                    System.out.println("where members = member 1,member 2,...,member n");
                    return false;
                }
                try {
                    Integer uid = Integer.valueOf(fields[2]);
                }
                catch (Exception e){
                    System.out.println("Error : gid must be in numbers");
                    return false;
                }
            }
        } catch (Exception e){
            System.out.println("Error : Passwd is not in the right format");
            return false;
        }
        return true;
    }
}
