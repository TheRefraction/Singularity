package net.singularity.utils;

public class Const {
    public static final String TITLE = "Singularity";

    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    public static final float MOUSE_SENSITIVITY = 0.2f;
    public static final float CAMERA_MOVE_SPEED = 0.1f;

    public static final int CHUNK_BASE_SIZE = 16;
    public static final int CHUNK_HEIGHT = 16;
    public static final int MAX_BLOCKS_PER_CHUNK = CHUNK_BASE_SIZE * CHUNK_BASE_SIZE * CHUNK_HEIGHT;
}
