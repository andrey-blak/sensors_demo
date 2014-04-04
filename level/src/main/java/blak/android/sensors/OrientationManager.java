package blak.android.sensors;

import blak.android.utils.ContextUtils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationManager {
    private static final int DELAY = SensorManager.SENSOR_DELAY_UI;
    private static final int DATA_SIZE = 3;

    private final float[] mRotationMatrixR = new float[16];
    private final float[] mAccelerometerData = new float[DATA_SIZE];
    private final float[] mMagneticFieldData = new float[DATA_SIZE];
    private final float[] mOrientationData = new float[DATA_SIZE];

    private OrientationListener mListener;

    public void onStart(Context context, OrientationListener listener) {
        mListener = listener;

        SensorManager sensorManager = ContextUtils.getSensorManager(context);

        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(mAccelerometerListener, accelerometerSensor, DELAY);

        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(mMagneticListener, magneticSensor, DELAY);
    }

    public void onStop(Context context) {
        mListener = null;

        SensorManager sensorManager = ContextUtils.getSensorManager(context);
        sensorManager.unregisterListener(mAccelerometerListener);
        sensorManager.unregisterListener(mMagneticListener);
    }

    public void onSensorsEvent() {
        if (mListener == null) {
            return;
        }

        boolean failure = !SensorManager.getRotationMatrix(mRotationMatrixR, null, mAccelerometerData, mMagneticFieldData);
        if (failure) {
            return;
        }
        SensorManager.getOrientation(mRotationMatrixR, mOrientationData);

        float[] values = new float[DATA_SIZE];
        System.arraycopy(mOrientationData, 0, values, 0, values.length);
        mListener.onOrientationEvent(values);
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
