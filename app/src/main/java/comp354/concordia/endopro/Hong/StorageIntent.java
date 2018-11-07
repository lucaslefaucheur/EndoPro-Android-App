package comp354.concordia.endopro.Hong;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import comp354.concordia.endopro.Common.User;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class StorageIntent extends IntentService {
    private static final String TAG="endopro_logi_db";

    public StorageIntent() {
        super("StorageIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent: Intent received");
        FileOutputStream outputStream=null;
        try {
            outputStream = openFileOutput("endoData.txt", Context.MODE_PRIVATE);
            ObjectOutputStream object = new ObjectOutputStream(outputStream);
            object.writeObject(User.getApp_data());
            outputStream.close();
            object.close();
            Log.i(TAG, "saveToDB: "+User.getApp_data().getCount()+" users in database");
        } catch (Exception e1) {
            e1.printStackTrace();
            Log.e(TAG, "saveToDB: ", e1);
        }
    }
}
