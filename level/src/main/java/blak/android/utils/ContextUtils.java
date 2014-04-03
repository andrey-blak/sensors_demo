package blak.android.utils;

import android.content.Context;
import android.hardware.SensorManager;

public class ContextUtils {
    public static SensorManager getSensorManager(Context context) {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }
}
