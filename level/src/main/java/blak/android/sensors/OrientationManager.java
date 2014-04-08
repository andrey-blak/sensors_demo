package blak.android.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;

import java.lang.ref.WeakReference;

import blak.android.utils.ContextUtils;

public class OrientationManager {
    private static final int DELAY = SensorManager.SENSOR_DELAY_UI;
    private static final int DATA_SIZE = 3;

    private final float[] mRotationMatrixR = new float[16];
    private final float[] mAccelerometerData = new float[DATA_SIZE];
    private final float[] mMagneticFieldData = new float[DATA_SIZE];
    private final float[] mOrientationData = new float[DATA_SIZE];

    private OrientationListener mListener;
    private WeakReference<Context> mContext;

    public void onStart(Context context, OrientationListener listener) {
        mListener = listener;
        mContext = new WeakReference<>(context);

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
        remapRotationMatrix();
        SensorManager.getOrientation(mRotationMatrixR, mOrientationData);

        float azimuth = mOrientationData[0];
        float pitch = mOrientationData[1];
        float roll = mOrientationData[2];
        mListener.onOrientationEvent(azimuth, pitch, roll);
    }

    private void remapRotationMatrix() {
        Context context = mContext.get();
        if (context == null) {
            return;
        }
        Display display = ContextUtils.getWindowManager(context).getDefaultDisplay();
        int rotation = display.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                break;

            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(mRotationMatrixR, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, mRotationMatrixR);
                break;

            case Surface.ROTATION_180:
                SensorManager.remapCoordinateSystem(mRotationMatrixR, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, mRotationMatrixR);
                break;

            case Surface.ROTATION_270:
                SensorManager.remapCoordinateSystem(mRotationMatrixR, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, mRotationMatrixR);
                break;
        }
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
