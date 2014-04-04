package blak.android.sensors;

public interface OrientationListener {
    /**
     * @param azimuth rotation around the Z axis
     * @param pitch rotation around the X axis
     * @param roll rotation around the Y axis
     */
    public void onOrientationEvent(float azimuth, float pitch, float roll);
}
