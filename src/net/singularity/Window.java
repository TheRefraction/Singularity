package net.singularity;

public class Window {
    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    private final String title;

    private int width, length;
    private long window;

    private boolean resize, vsync;

    private final Matrix4f projectionMatrix;
}
