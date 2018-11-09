//package comp354.concordia.endopro.Earl;
//
//import android.util.Log;
//
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.UUID;
//
//import comp354.concordia.endopro.Common.EndoProWorkout;
//import comp354.concordia.endopro.Common.User;
//import comp354.concordia.endopro.endo2java.EndomondoSession;
//import comp354.concordia.endopro.endo2java.error.LoginException;
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.HttpUrl;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
///***
// * This class is made to make moomen's EndomondoSession usable on Android
// */
//public class CyclingFetcher extends EndomondoSession {
//   /*
//            Changes of superclass stuff visibility to allow reuse
//            private -> protected:
//                parseLoginResponse()
//                checkLoginSuccess()
//                target()
//
//                email
//                password
//                authToken
//                URL
//                AUTH_PATH
//                WORKOUTS_PATH
//                SINGLE_WORKOUT_PATH
//                ACCOUNT_PATH
//                WORKOUTS_FIELDS
//                SINGLE_WORKOUT_FIELDS
//
//
//    */
//
//    private static final  String URL = "api.mobile.endomondo.com";
//    private static final String MOBILE_PATH ="mobile";
//    private static final String ENDOPRO_FIELDS = "simple,basic";
//    private static final String API = "api";
//    private static final String WORKOUTS = "workouts";
//    private static User user;
//
//    private static CyclingFetcher fetcher;
//    private OkHttpClient client;
//    private JSONArray workouts;
//    private boolean workoutsSet = false;
//    public CyclingFetcher(String email, String pass) {
//        super(email, pass);
//    }
//    private static final String BAD_AUTH = "bad_auth";
//    public CyclingFetcher(){
//        super(User.getInstance().getEndo_username(),User.getInstance().getEndo_password());
//    }
//
//    public static boolean done = false;
//    public void generateWorkoutList()  {
//        done = false;
//        FetchActivity.stat = "";
//        user = User.getInstance();
//        user.clearWorkouts();
//        try {
//            login();
//
//        } catch (LoginException e) {
//            // e.printStackTrace();
//            authToken = BAD_AUTH;
//            updateStatus( "Could not establish connection, please try again...");
//            FetchActivity.statusTV.post(new Runnable() {
//                @Override
//                public void run() {
//                    FetchActivity.statusTV.setText(FetchActivity.stat);
//                }
//            });
//        }
//
//        while(authToken== null);
//        if(authToken.equals(BAD_AUTH))
//        {
//            updateStatus( "Skipping workout requests, please try again...");
//            FetchActivity.statusTV.post(new Runnable() {
//                @Override
//                public void run() {
//                    FetchActivity.statusTV.setText(FetchActivity.stat);
//                }
//            });
//        }
//        else {
//            updateStatus( "Login Successful...");
//            FetchActivity.statusTV.post(new Runnable() {
//                @Override
//                public void run() {
//                    FetchActivity.statusTV.setText(FetchActivity.stat);
//                }
//            });
//            fetchWorkouts();
//            //while(!workoutsSet);
//
//            try {
//                storeWorkouts();
//                done = true;
//            } catch (JSONException e) {
//                // e.printStackTrace();
//                updateStatus( "Could not store workouts, please try again...");
//            }
//        }
//    }
//
//    @Override
//    public void login() throws LoginException {
//        updateStatus( "Attempting to log in...");
//        FetchActivity.statusTV.post(new Runnable() {
//            @Override
//            public void run() {
//                FetchActivity.statusTV.setText(FetchActivity.stat);
//            }
//        });
//        client = new OkHttpClient();
//
//
//        HttpUrl httpurl = new HttpUrl.Builder()
//                .scheme("https")
//                .host(URL)
//                .addPathSegment(MOBILE_PATH)
//                .addPathSegment(AUTH_PATH)
//                .addQueryParameter("deviceId", UUID.randomUUID().toString())
//                .addQueryParameter("country", "pl")
//                .addQueryParameter("action", "pair")
//                .addQueryParameter("email", email)
//                .addQueryParameter("password", password)
//                .build();
//        String request_url = httpurl.toString();
//
//        Log.d("WebTarget", request_url);
//
//        Request request = new Request.Builder()
//                .url(request_url)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //e.printStackTrace();
//
//                updateStatus( "Login Request Failed...");
//                FetchActivity.statusTV.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        FetchActivity.statusTV.setText(FetchActivity.stat);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String myResponse = response.body().string();
//                    try {
//                        checkLoginSuccess(myResponse);
//                        Map<String, String> responseMap = parseLoginResponse(myResponse);
//                        authToken = responseMap.get("authToken");
//                    } catch (LoginException e) {
//                        authToken = BAD_AUTH;
//                        updateStatus( "Response acquired, but bad authToken...");
//                        FetchActivity.statusTV.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                FetchActivity.statusTV.setText(FetchActivity.stat);
//                            }
//                        });
//                    }
//
//
//
//
//
//                }
//            }
//        });
//    }
//
//
//    private void storeWorkouts() throws JSONException {
//
//
//        if(workoutsSet) {
//            updateStatus( "Storing workouts to user...");
//            for (int i = 0; i < workouts.length(); ++i) {
//                JSONObject currentWorkout = workouts.getJSONObject(i);
//                if (currentWorkout.getInt("sport") == 2) //2 stands for cycling sport
//                {
//                    double avg = -1;
//                    double max = -1;
//                    double dist = -1;
//                    double dur = -1;
//                    String start = "";
//                    if (currentWorkout.has("speed_avg"))
//                        avg = currentWorkout.getDouble("speed_avg");
//                    if (currentWorkout.has("speed_max"))
//                        max = currentWorkout.getDouble("speed_max");
//                    if (currentWorkout.has("distance")) dist = currentWorkout.getDouble("distance");
//                    if (currentWorkout.has("duration")) dur = currentWorkout.getDouble("duration");
//                    if (currentWorkout.has("start_time"))
//                        start = currentWorkout.getString("start_time");
//
//                    // Log.d("CURRENT WORKOUT ", String.valueOf(avg) +"\t" + String.valueOf(max) + "\t" + String.valueOf(dist) + " \t" +String.valueOf(dur) + "\t" + start);
//                    //EndoProWorkout a = new EndoProWorkout();
//
//                    user.addWorkout(new EndoProWorkout(avg, max, dist, dur, start));
//                }
//
//            }
//
//        }
//
//    }
//    private void fetchWorkouts(){
//        updateStatus( "Attempting to retrieve workouts...");
//        FetchActivity.statusTV.post(new Runnable() {
//            @Override
//            public void run() {
//                FetchActivity.statusTV.setText(FetchActivity.stat);
//            }
//        });
//        HttpUrl httpurl = new HttpUrl.Builder()
//                .scheme("https")
//                .host(URL)
//                .addPathSegment(MOBILE_PATH)
//                .addPathSegment(API)
//                .addPathSegment(WORKOUTS)
//                .addQueryParameter("authToken",authToken)
//                .addQueryParameter("fields",ENDOPRO_FIELDS)
//                .addQueryParameter("maxResults","100")
//                .build();
//        String request_url = httpurl.toString();
//        Log.d("WebTarget", request_url);
//
//        Request request = new Request.Builder()
//                .url(request_url)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //e.printStackTrace();
//                updateStatus( "No request from server, please try again...");
//                FetchActivity.statusTV.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        FetchActivity.statusTV.setText(FetchActivity.stat);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String myResponse = response.body().string();
//                    JSONObject warkout = null;
//                    FetchActivity.statusTV.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            FetchActivity.statusTV.setText(FetchActivity.stat);
//                        }
//                    });
//                    try {
//                        warkout = new JSONObject(myResponse);
//                        FetchActivity.statusTV.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                FetchActivity.statusTV.setText(FetchActivity.stat);
//                            }
//                        });
//                    } catch (JSONException e) {
//                        // e.printStackTrace();
//                        updateStatus( "Unable to save data into JSON file, please try again...");
//                        FetchActivity.statusTV.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                FetchActivity.statusTV.setText(FetchActivity.stat);
//                            }
//                        });
//                    }
//
//                    if(warkout!=null) {
//                        try {
//
//                            workouts = warkout.getJSONArray("data");
//                            workoutsSet = true;
//
//                        } catch (JSONException e) {
//                            // e.printStackTrace();
//                            updateStatus( "Unable to parse JSON file, please try again...");
//
//                        }
//                    }
//
//
//                }
//            }
//        });
//    }
//
//
//    private void updateStatus(String s){
//        FetchActivity.stat += s+"\n";
//
//        FetchActivity.statusTV.post(new Runnable() {
//            @Override
//            public void run() {
//                FetchActivity.statusTV.setText(FetchActivity.stat);
//            }
//        });
//    }
//
//}
