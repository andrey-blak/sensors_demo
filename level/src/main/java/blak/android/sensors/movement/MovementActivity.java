package blak.android.sensors.movement;

import blak.android.sensors.level.R;
import blak.android.sensors.level.VerticalProgressBar;
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

    private int mHighPassCount;

    private MovementView mCompass;
    private VerticalProgressBar mLevelBar;
    private TextView mAccelerationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ls__movement_activity);

        mCompass = (MovementView) findViewById(R.id.ls__movement_compass);
        mLevelBar = (VerticalProgressBar) findViewById(R.id.ls__movement_level);
        mAccelerationTextView = (TextView) findViewById(R.id.ls__movement_acceleration);
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

        int verticalMax = mLevelBar.getMax();
        int verticalLevel = (int) (-accZ / 20 * verticalMax + verticalMax / 2);
        mLevelBar.setProgress(verticalLevel);

        mCompass.updateDirection(accX, accY);

        mAccelerationTextView.setText(String.format("%.2f", acceleration));
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
