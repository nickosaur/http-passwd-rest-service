package org.springframework.httprestservice.model;

public class User {
    private String name;
    private Integer uid;
    private Integer gid;
    private String comment;
    private String home;
    private String shell;


    public User(){
    }

    public User(String name, Integer uid, Integer gid, String comment, String home, String shell) {
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
            Integer _uid = Integer.valueOf(options[1]);
            if(!this.getUid().equals(_uid) ) {
                return false;
            }
        }

        // gid comparison
        if (options[2].length() != 0){
            Integer _gid = Integer.valueOf(options[2]);
            if(!this.getGid().equals(_gid)) {
                return false;
            }
        }

        // comment comparison
        if (options[3].length() != 0){
            if(!this.getName().equals(options[0])) {
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

    public Integer getUid() {
        return this.uid;
    }

    public Integer getGid() {
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

    public void setUid(Integer uid){
        this.uid = uid;
    }

    public void setGid(Integer gid){
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