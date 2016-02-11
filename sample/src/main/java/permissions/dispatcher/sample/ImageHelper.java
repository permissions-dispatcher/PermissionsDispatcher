package permissions.dispatcher.sample;

import android.net.Uri;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mue on 11.02.16.
 */
public class ImageHelper {

    public static Uri getUriToTakeAPhoto(){
        String imageFileName = ImageHelper.getImageFileNameWithTimeStamp();
        File file = new File(App.getAppFolderCheckAndCreate(), imageFileName);
        Uri createImageUri = Uri.fromFile(file);
        return createImageUri;
    }

    public static String getImageFileNameWithTimeStamp() {
        return "img_" + getTimeStamp();
    }

    public static String getTimeStamp() {
        final long timestamp = new Date().getTime();
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        final String timeString = new SimpleDateFormat("yyyy_MM_DD_HH_mm_ss_SSS").format(
                cal.getTime());
        return timeString;
    }
}

