package permissions.dispatcher.sample;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by mue on 11.02.16.
 */
public class App extends Application {
    public static String getAppFolderCheckAndCreate() {
        String appFolderPath = "";
        File externalStorage = Environment.getExternalStorageDirectory();
        if (externalStorage.canWrite()) {
            appFolderPath = externalStorage.getAbsolutePath() + "/" + BuildConfig.APPLICATION_ID;
            File dir = new File(appFolderPath);

            // create folder if it does not exist
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } else {
            Log.e(App.class.getName(), "Storage media not found or is full!");
        }

        return appFolderPath;
    }
}
