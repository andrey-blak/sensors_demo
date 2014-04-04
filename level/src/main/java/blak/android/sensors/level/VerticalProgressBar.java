package blak.android.sensors.level;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class VerticalProgressBar extends ImageView {
    private static final int MAX = 10000;

    public VerticalProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setImageResource(R.drawable.ls__verticat_progress);
    }

    public void setProgress(int progress) {
        setImageLevel(progress);
        invalidate();
    }

    public int getMax() {
        return MAX;
    }
}
