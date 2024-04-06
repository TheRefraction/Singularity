package net.singularity.utils;

public class Const {
    public static final String TITLE = "sg-04062024-b";
    public static final long NANOSECOND = 1000000000L;

    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;
    public static final float RENDER_DISTANCE = 32f;

    public static final float MOUSE_SENSITIVITY = 0.2f;
    public static final float CAMERA_MOVE_SPEED = 0.4f;
    public static final float COLLISION_GRANULARITY = 0f;

    public static final int CHUNK_WIDTH = 32;
    public static final int CHUNK_HEIGHT = 32;
    public static final int CHUNK_DEPTH = 32;
    public static final int MAX_BLOCKS_PER_CHUNK = CHUNK_WIDTH * CHUNK_HEIGHT * CHUNK_DEPTH;

    public static final int REGION_BASE_SIZE = 256;

    public static final int WORLD_WIDTH = 128;
    public static final int WORLD_HEIGHT = 128;
    public static final int WORLD_DEPTH = 64;
}
