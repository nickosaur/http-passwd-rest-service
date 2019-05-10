package org.springframework.httprestservice.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group {
    private String name;
    private BigInteger gid;
    private List<String> members;

    public Group(){}

    public Group(String name, BigInteger gid, List<String> members){
        this.name = name;
        this.gid = gid;
        this.members = members;
    }

    public String getName(){
        return this.name;
    }

    public BigInteger getGid(){
        return this.gid;
    }

    public List<String> getMembers(){
        return this.members;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setGid(BigInteger gid){
        this.gid = gid;
    }

    public void setMembers(List<String> members){
        this.members = members;
    }

    public boolean compareWithOptions(String[] options, String[] memberOptions){
        // name comparison
        if (options[0].length() != 0){
            if(!this.getName().equals(options[0])) {
                return false;
            }
        }

        // gid comparison
        if (options[1].length() != 0){
            BigInteger _gid;
            try{
                _gid = new BigInteger(options[1]);
            } catch (Exception e){
                System.out.println("Gid is not in numbers");
                return false;
            }
            if(!this.getGid().equals(_gid)) {
                return false;
            }
        }

        //subset comparison for members
        if (memberOptions.length != 0) {
            List<String> memberOps = new ArrayList<>(memberOptions.length);
            Collections.addAll(memberOps, memberOptions);
            if (!((this.members).containsAll(memberOps))) {
                return false;
            }
        }

        return true;
    }
}
