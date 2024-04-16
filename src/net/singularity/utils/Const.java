package net.singularity.utils;

public class Const {
    public static final String TITLE = "Singularity";
    public static final String GAME_VERSION = "0.0.1a";

    public static final int TARGET_UPS = 40;
    public static final int TARGET_FPS = 60;

    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000.0f;

    public static final float MOUSE_SENSITIVITY = 0.2f;
    public static final float COLLISION_GRANULARITY = 0f;

    public static final float BLOCK_TEX_UV_STEP = 0.0625f;
    public static final float FONT_TEX_UV_STEP = 0.0625f;

    public static final int BLOCK_UPDATE_INTERVAL = 800;

    public static final int CHUNK_SIZE = 16;
}
