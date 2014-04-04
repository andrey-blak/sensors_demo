package blak.android.sensors.level;

import blak.android.sensors.OrientationListener;
import blak.android.sensors.OrientationManager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final OrientationManager mOrientationManager = new OrientationManager();

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

        mOrientationManager.onStart(this, mOrientationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mOrientationManager.onStop(this);
    }

    private String formatOrientation(float [] orientation) {
        String format = "around Z:%d\n" + "around X:%d\n" + "around Y:%d\n";
        int azimuth = (int) Math.toDegrees(orientation[0]);
        int pitch = (int) Math.toDegrees(orientation[1]);
        int roll = (int) Math.toDegrees(orientation[2]);

        return String.format(format, azimuth, pitch, roll);
    }

    private final OrientationListener mOrientationListener = new OrientationListener() {
        @Override
        public void onOrientationEvent(float[] orientation) {
            String message = formatOrientation(orientation);
            mTextView.setText(message);
        }
    };
}
