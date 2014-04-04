package blak.android.sensors;

import blak.android.sensors.level.R;
import blak.android.utils.ContextUtils;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MovementActivity extends Activity {
    private static final int DELAY = SensorManager.SENSOR_DELAY_UI;
    private static final int HIGH_PASS_MINIMUM = 10;
    private static final float ALPHA = 0.8f;
    private static final String FORMAT = "X: %.3f\nY: %.3f\nZ: %.3f\nTotal: %.3f";

    private int mHighPassCount;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ls__text_activity);

        mTextView = (TextView) findViewById(R.id.ls__text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SensorManager sensorManager = ContextUtils.getSensorManager(this);

        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(mListener, accelerometerSensor, DELAY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SensorManager manager = ContextUtils.getSensorManager(this);
        manager.unregisterListener(mListener);
    }

    private static void highPass(float[] oldValues, float[] newValues) {
        for (int i = 0; i < oldValues.length; i++) {
            oldValues[i] = ALPHA * oldValues[i] + (1 - ALPHA) * newValues[i];
            newValues[i] -= oldValues[i];
        }
    }

    private void updateAccelerations(float accX, float accY, float accZ) {
        double sumOfSquares = accX * accX + (accY * accY) + (accZ * accZ);
        double acceleration = Math.sqrt(sumOfSquares);

        String message = String.format(FORMAT, accX, accY, accZ, acceleration);
        mTextView.setText(message);
    }

    private final SensorEventListener mListener = new SensorEventListener() {
        private final float[] mGravity = new float[3];

        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values.clone();

            highPass(mGravity, values);

            // Ignore data if the high-pass filter is enabled, has not yet received some data to set it
            ++mHighPassCount;
            if (mHighPassCount >= HIGH_PASS_MINIMUM) {
                updateAccelerations(values[0], values[1], values[2]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
