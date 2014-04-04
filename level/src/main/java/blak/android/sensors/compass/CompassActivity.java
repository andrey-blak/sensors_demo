package blak.android.sensors.compass;

import blak.android.sensors.OrientationListener;
import blak.android.sensors.OrientationManager;
import blak.android.sensors.level.R;

import android.app.Activity;
import android.os.Bundle;

public class CompassActivity extends Activity {
    private final OrientationManager mOrientationManager = new OrientationManager();

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

        mOrientationManager.onStart(this, mOrientationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mOrientationManager.onStop(this);
    }

    public void onSensorsEvent(float[] orientation) {
        double degrees = Math.toDegrees(orientation[0]);
        mCompassView.updateDirection(degrees);
    }

    private final OrientationListener mOrientationListener = new OrientationListener() {
        @Override
        public void onOrientationEvent(float[] orientation) {
            onSensorsEvent(orientation);
        }
    };
}
