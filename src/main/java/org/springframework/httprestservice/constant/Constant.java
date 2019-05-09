package org.springframework.httprestservice.constant;

public final class Constant {
    private Constant(){}

    // default passwd and group files are located in /etc/ for unix machines
    public static String systemPasswd = "/etc/passwd";
    public static String systemGroup = "/etc/group";
    public static boolean defaultFile = true;
    public static String defaultPasswd = "./data/passwd";
    public static String defaultGroup = "./data/group";
}
