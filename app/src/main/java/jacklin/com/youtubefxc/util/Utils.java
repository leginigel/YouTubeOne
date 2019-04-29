package jacklin.com.youtubefxc.util;

import android.animation.TimeAnimator;
import android.support.v17.leanback.graphics.ColorOverlayDimmer;
import android.support.v17.leanback.widget.ShadowOverlayContainer;
import android.support.v17.leanback.widget.ShadowOverlayHelper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static final String DurationConverter(String duration) {
        return null;
    }

    public static final String CountConverter(int count) {
        DecimalFormat df = new DecimalFormat("#.#");
        float fCount;
        if(count > 1000000000) {
            fCount = (float) count / 1000000000;
            if(fCount > 10)
                return String.format("%d", (int) fCount) + "B";
            return df.format(fCount) + "B";
        } else if(count > 1000000) {
            fCount = (float) count / 1000000;
            if(fCount > 10)
                return String.format("%d", (int) fCount) + "M";
            return df.format(fCount) + "M";
        } else if(count > 1000) {
            fCount = (float) count / 1000;
            if(fCount > 10)
                return String.format("%d", (int) fCount) + "K";
            return df.format(fCount) + "K";
        } else {
            return String.valueOf(count);
        }
    }

    public static final String TimeConverter(String time) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String convTime = null;
        try {
            Date videoDate = format.parse(time);
            Date now = new Date();
            long dateDiff = now.getTime() - videoDate.getTime();
            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);
            if(second < 60) {
                convTime = second + " seconds ";
            } else if(minute < 60) {
                convTime = minute + " minutes ";
            } else if(hour < 24) {
                convTime = hour + " hours ";
                if(hour == 1)
                    convTime = hour + " hour ";
            } else if(day >= 7) {
                if(day > 360) {
                    convTime = (day / 360) + " years ";
                    if((day / 360) == 1)
                        convTime = (day / 360) + " year ";
                }
                else if(day > 30) {
                    convTime = (day / 30) + " months ";
                    if((day / 30) == 1)
                        convTime = (day / 30) + " month ";
                } else {
                    convTime = (day / 7) + " weeks ";
                    if((day / 7) == 1)
                        convTime = (day / 7) + " week ";
                }
            } else if(day < 7) {
                convTime = day + " days ";
                if(day == 1)
                    convTime = day + " day ";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convTime;
    }


}
