package blak.android.sensors.level;

import blak.android.utils.ContextUtils;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int DELAY = SensorManager.SENSOR_DELAY_UI;

    private final float[] mRotationMatrixR = new float[16];
    private final float[] mAccelerometerData = new float[3];
    private final float[] mMagneticFieldData = new float[3];
    private final float[] mOrientationData = new float[3];

    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ls__main_activity);

        mTextView = (TextView) findViewById(R.id.ls__main_text);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SensorManager sensorManager = ContextUtils.getSensorManager(this);

        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(mAccelerometerListener, accelerometerSensor, DELAY);

        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(mMagneticListener, magneticSensor, DELAY);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SensorManager sensorManager = ContextUtils.getSensorManager(this);
        sensorManager.unregisterListener(mAccelerometerListener);
        sensorManager.unregisterListener(mMagneticListener);
    }

    public void onSensorsEvent() {
        SensorManager.getRotationMatrix(mRotationMatrixR, null, mAccelerometerData, mMagneticFieldData);
        SensorManager.getOrientation(mRotationMatrixR, mOrientationData);

        String message = formatOrientation();
        mTextView.setText(message);
    }

    private String formatOrientation() {
        String format = "around Z:%d\n" + "around X:%d\n" + "around Y:%d\n";
        int azimuth = (int) Math.toDegrees(mOrientationData[0]);
        int pitch = (int) Math.toDegrees(mOrientationData[1]);
        int roll = (int) Math.toDegrees(mOrientationData[2]);

        return String.format(format, azimuth, pitch, roll);
    }

    private final SensorEventListener mAccelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            System.arraycopy(event.values, 0, mAccelerometerData, 0, mAccelerometerData.length);
            onSensorsEvent();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private final SensorEventListener mMagneticListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            System.arraycopy(event.values, 0, mMagneticFieldData, 0, mMagneticFieldData.length);
            onSensorsEvent();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
