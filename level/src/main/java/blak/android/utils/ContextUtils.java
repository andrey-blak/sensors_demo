package blak.android.utils;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.WindowManager;

public class ContextUtils {
    public static SensorManager getSensorManager(Context context) {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }
}
