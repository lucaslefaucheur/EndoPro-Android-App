package comp354.concordia.endopro.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import comp354.concordia.endopro.User.User;

public class App_Data implements Serializable {
    private Map<String,User> users; //Database of all Users
    private User cached; //If last user checked "remember me" checkbox when signed in

    public App_Data(){
        users=new HashMap<String, User>();
    }

    public Map<String,User> getAllUsers(){ return users; }

    public User getCachedUser(){
        return cached;
    }
    public void setCachedUser(User user){
        cached=user;
    }
}
