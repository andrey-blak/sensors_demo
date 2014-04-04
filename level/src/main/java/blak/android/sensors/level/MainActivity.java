package blak.android.sensors.level;

import blak.android.sensors.OrientationListener;
import blak.android.sensors.OrientationManager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    private static final int MAX_LEVEL = 100;
    private static final int ZERO_LEVEL = MAX_LEVEL / 2;
    private static final double TWO_PI = Math.PI * 2;
    private static final double PITCH_RANGE = Math.PI;
    private static final double ROLL_RANGE = TWO_PI;

    private final OrientationManager mOrientationManager = new OrientationManager();

    private VerticalProgressBar mVerticalLevel;
    private ProgressBar mHorizontalLevel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ls__main_activity);

        findViews();
        initLevels();
    }

    private void findViews() {
        mVerticalLevel = (VerticalProgressBar) findViewById(R.id.ls__main_vertical_level);
        mHorizontalLevel = (ProgressBar) findViewById(R.id.ls__main_horizontal_level);
    }

    private void initLevels() {
        mVerticalLevel.setProgress(mVerticalLevel.getMax() / 2);
        mHorizontalLevel.setMax(MAX_LEVEL);
        mHorizontalLevel.setProgress(ZERO_LEVEL);
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

    private void updateLevels(float azimuth, float pitch, float roll) {
        int verticalMax = mVerticalLevel.getMax();
        int verticalLevel = (int) (-pitch / PITCH_RANGE * verticalMax + verticalMax / 2);
        mVerticalLevel.setProgress(verticalLevel);

        int horizontalMax = mHorizontalLevel.getMax();
        int horizontalLevel = (int) (-roll / ROLL_RANGE * horizontalMax + horizontalMax / 2);
        mHorizontalLevel.setProgress(horizontalLevel);
    }

    private final OrientationListener mOrientationListener = new OrientationListener() {
        @Override
        public void onOrientationEvent(float azimuth, float pitch, float roll) {
            updateLevels(azimuth, pitch, roll);
        }
    };
}
