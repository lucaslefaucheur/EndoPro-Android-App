package comp354.concordia.endopro.Hong;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import comp354.concordia.endopro.Common.User;

public class App_Data implements Serializable {
    private Map<String,User> users;
    private User cached;

    public App_Data(){
        users=new HashMap<String, User>();
    }

    public User getUser(String username){
        if(users.size()>0){
            User u = users.get(username);
            if(u!=null) return u;
        }
        return null;
    }

    public int getCount(){
        return users.size();
    }

    public void addUser(User user){
        users.put(user.getApp_username(),user);
    }

    public User getCached(){
        return cached;
    }

    public void setCached(User user){
        cached=user;
    }
}
