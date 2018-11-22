package comp354.concordia.endopro.User;

import java.util.ArrayList;

/*
*   This class implements the Singleton design pattern
*/
public class UserController {
    private static UserController instance; //Private instance
    private User user; //Stores reference to the active user
    private ArrayList<EndoProWorkout> workouts_filtered; //Temporary storage for filtered workouts
    private App_Data app_data;

    UserController(){
        workouts_filtered=new ArrayList<EndoProWorkout>();
    }

    //Static public method to retrieve the private instance
    public static UserController getInstance(){
        if(instance==null)
            instance = new UserController();
        return instance;
    }

    //Getter and Setter for the active user property
    public User getCurrentUser(){return user;}
    public void setCurrentUser(User user){this.user=user;}

    //Getter and Setter for app_data
    public void setApp_data(App_Data app_data) {
        this.app_data = app_data;
        if(app_data.getCachedUser()!=null)
            user=app_data.getCachedUser();
    }
    public App_Data getApp_data(){return app_data;}

    public ArrayList<EndoProWorkout> getWorkouts(){
        if(workouts_filtered.isEmpty())
            return user.getWorkouts();
        return workouts_filtered;
    }

    //Methods to manipulate workouts
    public void addWorkout(EndoProWorkout workout) {
        if(user!=null){
            user.getWorkouts().add(workout);
        }
    }
    public void clearWorkouts(){ user.getWorkouts().clear(); }

    public void setFilteredWorkout(ArrayList<EndoProWorkout> filtered){
        workouts_filtered=filtered;
    }

    public User Authenticate(String username, String password){
        if(app_data!=null){
            User user =app_data.getAllUsers().get(username);
            if(user==null)return null;
            if(user.getApp_password().equals(password)){
                setCurrentUser(user);
                return user;
            }
        }
        return null;
    }

    public void updateEndoInfo(String endo_username, String endo_password){
        if(user!=null){
            if(endo_username!="") endo_username=user.getEndo_username();
            if(endo_password!="") endo_password=user.getEndo_password();
            user=new User(user.getApp_username(),user.getApp_password(),endo_username,endo_password);
        }
    }

    public boolean updateAppPassword(String oldpass, String newPass){
        if (Authenticate(user.getApp_username(),oldpass)!=null){
            user.setApp_password(newPass);
            return true;
        }
        return false;
    }

    public void signOut(){
        user=null;
        app_data.setCachedUser(null);
    }

    public void signUp(User user){
        app_data.getAllUsers().put(user.getApp_username(),user);
        setCurrentUser(user);
    }

    public void autoSignIn(){
        app_data.setCachedUser(user);
    }
}
