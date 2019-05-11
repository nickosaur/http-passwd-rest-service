package org.springframework.httprestservice.model;

import java.math.BigInteger;

public class User {
    private String name;
    private BigInteger uid;
    private BigInteger gid;
    private String comment;
    private String home;
    private String shell;


    public User(){
    }

    public User(String name, BigInteger uid, BigInteger gid, String comment, String home, String shell) {
        this.name = name;
        this.uid = uid;
        this.gid = gid;
        this.comment = comment;
        this.home = home;
        this.shell = shell;
    }

    public boolean compareWithOptions(String[] options){

        // name comparison
        if (options[0].length() != 0){
            if(!this.getName().equals(options[0])) {
                return false;
            }
        }

        // uid comparison
        if (options[1].length() != 0){
            BigInteger _uid;
            try{
                _uid = new BigInteger(options[1]);
            } catch (Exception e){
                System.out.println("Uid is not in numbers");
                return false;
            }
            if(!this.getUid().equals(_uid) ) {
                return false;
            }
        }

        // gid comparison
        if (options[2].length() != 0){
            BigInteger _gid;
            try{
                _gid = new BigInteger(options[2]);
            } catch (Exception e){
                System.out.println("Gid is not in numbers");
                return false;
            }
            if(!this.getGid().equals(_gid)) {
                return false;
            }
        }

        // comment comparison
        if (options[3].length() != 0){
            if(!this.getComment().equals(options[3])) {
                return false;
            }
        }

        // home comparison
        if (options[4].length() != 0){
            if(!this.getHome().equals(options[4])) {
                return false;
            }
        }

        // shell
        if (options[5].length() != 0){
            if(!this.getShell().equals(options[5])) {
                return false;
            }
        }

        return true;
    }

    public String getName() {
        return this.name;
    }

    public BigInteger getUid() {
        return this.uid;
    }

    public BigInteger getGid() {
        return this.gid;
    }

    public String getComment() {
        return this.comment;
    }

    public String getHome() {
        return this.home;
    }

    public String getShell() {
        return this.shell;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setUid(BigInteger uid){
        this.uid = uid;
    }

    public void setGid(BigInteger gid){
        this.gid = gid;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public void setHome(String home){
        this.home = home;
    }

    public void setShell(String shell){
        this.shell = shell;
    }
}