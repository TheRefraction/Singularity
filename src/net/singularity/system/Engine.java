package net.singularity.system;

import net.singularity.Main;
import net.singularity.utils.Const;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class Engine {

    private static int fps;
    private final float FRAMERATE = 60;
    private final float frameTime = 1.0f / FRAMERATE;
    public static float currentFrameTime = 0;
    private boolean isRunning;

    private Window windowManager;
    private MouseInput mouseInput;
    private ILogic gameLogic;
    private GLFWErrorCallback errorCallback;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        windowManager = Main.getWindow();
        gameLogic = Main.getGame();
        mouseInput = new MouseInput();
        windowManager.init();
        gameLogic.init();
        mouseInput.init();
    }

    public void start() throws Exception {
        init();
        if(isRunning)
            return;
        run();
    }

    public void run() {
        this.isRunning = true;

        int frames = 0;
        long frameCounter = 0;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while(isRunning) {
            boolean render = false;

            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) Const.NANOSECOND;
            frameCounter += passedTime;

            //input();

            while(unprocessedTime > frameTime) {
                render = true;
                unprocessedTime -= frameTime;

                if(windowManager.windowShouldClose())
                    stop();

                if(frameCounter >= Const.NANOSECOND) {
                    setFps(frames);
                    currentFrameTime = 1.0f / fps;
                    windowManager.setTitle(Const.TITLE+"("+getFps()+")");
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render) {
                input();
                update(frameTime);
                render();
                frames++;

            }
        }
        cleanup();
    }

    private void stop() {
        if(!isRunning)
            return;
        isRunning = false;
    }

    private void input() {
        mouseInput.input();
        gameLogic.input(mouseInput);
    }

    private void render() {
        gameLogic.render();
        windowManager.update();
    }

    private void update(float interval) {
        gameLogic.update(interval);
    }

    private void cleanup() {
        windowManager.cleanup();
        gameLogic.cleanup();

        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        Engine.fps = fps;
    }
}
