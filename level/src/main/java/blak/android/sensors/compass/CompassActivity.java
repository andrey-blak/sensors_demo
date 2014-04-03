package blak.android.sensors.compass;

import blak.android.sensors.level.R;
import blak.android.utils.ContextUtils;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class CompassActivity extends Activity {

    private static final int DELAY = SensorManager.SENSOR_DELAY_UI;

    private final float[] mRotationMatrixR = new float[16];
    private final float[] mAccelerometerData = new float[3];
    private final float[] mMagneticFieldData = new float[3];
    private final float[] mOrientationData = new float[3];

    private CompassView mCompassView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ls__compass_activity);

        mCompassView = (CompassView) findViewById(R.id.ls__compass);
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

        double degrees = Math.toDegrees(mOrientationData[0]);
        mCompassView.updateDirection(degrees);
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
