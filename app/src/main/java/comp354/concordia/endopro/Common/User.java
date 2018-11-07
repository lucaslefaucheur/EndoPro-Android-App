package comp354.concordia.endopro.Common;

import java.io.Serializable;
import java.util.ArrayList;

import comp354.concordia.endopro.Hong.App_Data;

public class User implements Serializable {
    private static User m_user;
    private static App_Data app_data;

    private String app_username;
    private String app_password;
    private String endo_username;
    private String endo_password;

    private ArrayList<EndoProWorkout> workouts = new ArrayList<EndoProWorkout>();
    private ArrayList<EndoProWorkout> workouts_filtered = new ArrayList<EndoProWorkout>();

    private User(String app_username, String app_password, String endo_username, String endo_password) {
        this.app_username = app_username;
        this.app_password = app_password;
        this.endo_username = endo_username;
        this.endo_password = endo_password;
    }

    static public User getInstance(){
        return m_user;
    }

    static public void setInstance(String app_username , String app_password, String endo_username, String endo_password){
        if(m_user == null) {
            m_user = new User(app_username,app_password,endo_username,endo_password);
        }
        app_data.addUser(m_user);
    }

    static public void setInstance(User user){
        m_user=user;
    }

    static public App_Data getApp_data(){
        return app_data;
    }

    static public void setApp_data(App_Data data){
        app_data =data;
    }

    static public void rememberUser(){
        app_data.setCached(m_user);
    }

    static public void signOut(){
        app_data.setCached(null);
        User.setInstance(null);
    }

    public void addWorkout(EndoProWorkout workout) {
        workouts.add(workout);
    }

    public void clearWorkouts(){
        workouts=new ArrayList<EndoProWorkout>();
    }

    public ArrayList<EndoProWorkout> getWorkouts() {
        return  workouts;
    }

    public ArrayList<EndoProWorkout> getWorkouts_filtered() {
        if(workouts_filtered.size()==0)
            return workouts;
        return  workouts_filtered;
    }

    public void setFilteredWorkout(ArrayList<EndoProWorkout> filtered){
        workouts_filtered=filtered;
    }

    public boolean Authenticate(String username, String password){
        if(username.equals(app_username) && password.equals(app_password))
            return true;
        return false;
    }

    public boolean updateEndoInfo(String app_password, String endo_username, String endo_password){
        if(Authenticate(app_username,app_password)){
            if(endo_username!="") m_user.endo_username=endo_username;
            if(endo_password!="") m_user.endo_password=endo_password;
            return true;
        }
        return false;
    }

    public String getEndo_username() {
        return  endo_username;
    }

    public String getEndo_password() {
        return  endo_password;
    }

    public String getApp_username(){return app_username;}


}
