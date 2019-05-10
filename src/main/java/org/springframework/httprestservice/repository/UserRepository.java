package org.springframework.httprestservice.repository;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.httprestservice.model.Group;
import org.springframework.httprestservice.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private List<User> users;
    private String passwd;
    private Properties prop;
    private Date lastLoad = new Date(0L);

    @Autowired
    private GroupRepository groupRepository;

    public UserRepository() {

        prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("./app.properties");

            // load the properties file
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            // if fails to load properties file will hard load the default passwd file
            this.passwd = "./data/passwd";
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
            this.passwd = prop.getProperty("custom_passwd");
        } else {
            this.passwd = prop.getProperty("default_passwd");
        }

        System.out.println("passwd is " + passwd);
        users = new ArrayList<User>();

        if(!checkIfValidPasswd(this.passwd)){
            // we will use our default passwd file
            this.passwd = prop.getProperty("default_passwd");
        }
        loadPasswd(this.passwd);
    }

    public List<User> getUsers() {

        if (checkForUpdates(this.passwd)){
            if (checkIfValidPasswd(this.passwd)){
                this.passwd = prop.getProperty("default_passwd");
            }
            loadPasswd(this.passwd);
        }
        return this.users;
    }

    public User getUserByUid(String uid) {

        if (checkForUpdates(this.passwd)){
            if (checkIfValidPasswd(this.passwd)){
                this.passwd = prop.getProperty("default_passwd");
            }
            loadPasswd(this.passwd);
        }

        BigInteger id;
        try {
            id = new BigInteger(uid);
        } catch (Exception e){
            System.err.println(uid + " is not in numbers");
            return null;
        }

        List<User> result = users.stream().filter(u -> (u.getUid().equals(id) == true)).collect(Collectors.toList());
        if (result.size() > 0){
            return result.get(0);
        }
        else{
            return null;
        }
    }

    public List<User> getUsersByOptions(String[] options){

        if (checkForUpdates(this.passwd)){
            if (checkIfValidPasswd(this.passwd)){
                this.passwd = prop.getProperty("default_passwd");
            }
            loadPasswd(this.passwd);
        }

        return users.stream().filter(u -> (u.compareWithOptions(options) == true)).collect(Collectors.toList());
    }

    private void loadPasswd(String passwd){
        File file = new File(passwd);
        if (file.exists() == false){ // guaranteed to succeed;
            System.out.println("Error : Passwd does not exist!");
        }
        // load time here
        lastLoad = new Date(file.lastModified());
        this.users = new ArrayList<User>();
        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
        } catch (Exception e) {

            throw new IllegalArgumentException("Error : FileReader can't read " + passwd);
        }
        BufferedReader buffer = new BufferedReader(fileReader);

        String readLine = "";
        try {
            while ((readLine = buffer.readLine()) != null) {

                if (readLine.length() == 0){
                    continue;
                }
                String[] fields = readLine.split(":", -1);
                User user = new User();
                user.setName(fields[0]);
                BigInteger uid = new BigInteger(fields[2].trim());
                user.setUid(uid);
                BigInteger gid = new BigInteger(fields[3].trim());
                user.setGid(gid);
                user.setComment(fields[4]);
                user.setHome(fields[5].trim());
                user.setShell(fields[6].trim());
                boolean result = this.users.add(user);
            }
        } catch (Exception e){ // will not be reached
            System.out.println("Error : Unable to read passwd!");
        }
    }

    private boolean checkForUpdates(String passwd){
        File file = new File(passwd);
        Date passwdLastModified = new Date(file.lastModified());
        if (users.size() == 0 || lastLoad.compareTo(passwdLastModified) < 0){
            return true; // that means passwd has been modified and needs to be loaded again
        }
        return false;
    }

    private boolean checkIfValidPasswd(String passwd){
        File file = new File(passwd);
        if (file.exists() == false){
            System.out.println("Error : Passwd does not exist!");
            return false;
        }

        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
        } catch (Exception e) {
            System.out.println("Error : Unable to read file. Please check permissions of " + passwd);
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
                if (fields.length != 7){
                    System.out.println("Error : Passwd file not in the right format");
                    System.out.println("The expected format of passwd file should be:");
                    System.out.println("name:password:uid:gid:comment:home:shell");
                    return false;
                }
                try {
                    BigInteger uid = new BigInteger(fields[2].trim());
                }
                catch (Exception e){
                    System.out.println("Error : uid must be in numbers");
                    return false;
                }

                try {
                    BigInteger gid = new BigInteger(fields[3].trim());
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

    public List<Group> getGroupsByUid(String uid){
        System.out.println("getgroupbyuid " + uid);
        User user = this.getUserByUid(uid);
        String userName = user.getName();
        System.out.println("userName is " + userName);
        //groupRepository = new Gr
        return groupRepository.getGroupsByUserName(userName);
    }


}