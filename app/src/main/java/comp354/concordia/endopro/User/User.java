package comp354.concordia.endopro.User;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String app_username;
    private String app_password;
    private String endo_username;
    private String endo_password;

    private ArrayList<EndoProWorkout> workouts = new ArrayList<EndoProWorkout>();

    public User(String app_username, String app_password, String endo_username, String endo_password) {
        this.app_username = app_username;
        this.app_password = app_password;
        this.endo_username = endo_username;
        this.endo_password = endo_password;
    }

    public ArrayList<EndoProWorkout> getWorkouts() {return workouts; }

    //Getters and Setters
    public String getEndo_username() {
        return  endo_username;
    }
    public String getEndo_password() {
        return  endo_password;
    }

    public String getApp_username(){return app_username;}
    public String getApp_password(){return app_password;}

    protected void setApp_password(String password){app_password=password;}
}
