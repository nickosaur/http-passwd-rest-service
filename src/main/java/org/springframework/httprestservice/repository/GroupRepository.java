package org.springframework.httprestservice.repository;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.httprestservice.model.Group;
import org.springframework.stereotype.Repository;

@Repository
public class GroupRepository {
    private List<Group> groups;
    private String groupFile;
    private Properties prop;
    private Date lastLoad = new Date(0L);

    public GroupRepository(){

        prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("./app.properties");

            // load the properties file
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            // if fails to load properties file will hard load the default passwd file
            this.groupFile = "./data/group";
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (prop.getProperty("custom").equals("true")){
            this.groupFile = prop.getProperty("custom_group");
        } else {
            this.groupFile = prop.getProperty("default_group");
        }

        groups = new ArrayList<Group>();
        //TODO check if valid and loadGroup
        if(!checkIfValidGroup(this.groupFile)){
            this.groupFile = prop.getProperty("default_group");
        }
        loadGroup(this.groupFile);
    }

    public List<Group> getGroups(){
        if (checkForUpdates(this.groupFile)){
            if (checkIfValidGroup(this.groupFile)){
                this.groupFile = prop.getProperty("default_group");
            }
            loadGroup(this.groupFile);
        }
        return this.groups;
    }

    public Group getGroupByGid(String gid){
        if (checkForUpdates(this.groupFile)){
            if (checkIfValidGroup(this.groupFile)){
                this.groupFile = prop.getProperty("default_group");
            }
            loadGroup(this.groupFile);
        }
        BigInteger _gid;
        try{
            _gid = new BigInteger(gid);
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
                this.groupFile = prop.getProperty("default_group");
            }
            loadGroup(this.groupFile);
        }
        return groups.stream().filter(g -> (g.compareWithOptions(options, members) == true)).collect(Collectors.toList());
    }

    public List<Group> getGroupsByUserName(String userName){
        System.out.println("GroupRepo " + userName);
        List<Group> filtered = new ArrayList<Group>();
        for (Group grp : this.groups){
            List<String> members = grp.getMembers();
            for (String member : members){
                if (member.equals(userName)){
                    filtered.add(grp);
                    break;
                }
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
                if (readLine.length() == 0){
                    continue;
                }
                String[] fields = readLine.split(":", -1);
                Group grp = new Group();
                grp.setName(fields[0].trim());
                BigInteger gid = new BigInteger(fields[2].trim());
                grp.setGid(gid);
                String[] members = fields[3].split(",");
                List<String> memberList = new ArrayList<>(members.length);

                for (String member : members){
                    memberList.add(member.trim());
                }
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
                    BigInteger gid = new BigInteger(fields[2].trim());
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
