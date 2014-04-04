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

    private void updatetextView(float azimuth, float pitch, float roll) {
        String message = formatOrientation(azimuth, pitch, roll);
        mTextView.setText(message);
    }

    private String formatOrientation(float azimuth, float pitch, float roll) {
        String format = "around Z:%d\n" + "around X:%d\n" + "around Y:%d\n";
        int azimuthDegrees = (int) Math.toDegrees(azimuth);
        int pitchDegrees = (int) Math.toDegrees(pitch);
        int rollDegrees = (int) Math.toDegrees(roll);
        return String.format(format, azimuth, pitch, roll);
    }

    private final OrientationListener mOrientationListener = new OrientationListener() {
        @Override
        public void onOrientationEvent(float azimuth, float pitch, float roll) {
            updatetextView(azimuth, pitch, roll);
        }
    };

}
