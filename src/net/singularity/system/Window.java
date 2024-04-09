package net.singularity.system;

import net.singularity.utils.Const;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {
    private int width, height;
    private String title;
    private long window;
    private int frames;
    private static long time;
    private Input input;
    private GLFWWindowSizeCallback sizeCallback;
    private boolean isResized;
    private boolean isFullscreen;
    private int[] windowPosX = new int[1], windowPosY = new int[1];
    private float r = 0, g = 0, b = 0;
    private Matrix4f projectionMatrix;

    public Window(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.projectionMatrix = new Matrix4f();
    }

    public void init() {
        System.out.println("Initializing Window");
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()) {
            System.err.println("ERROR: GLFW wasn't initialized!");
            return;
        }

        System.out.println("GLFW initialized!");

        /*GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);*/

        input = new Input();
        window = GLFW.glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);

        if(window == 0) {
            System.err.println("ERROR: Window wasn't created!");
            return;
        }

        System.out.println("Window created!");

        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if(vidMode == null) {
            System.err.println("ERROR: Could not get video mode!");
            return;
        }

        windowPosX[0] = (vidMode.width() - width) / 2;
        windowPosY[0] = (vidMode.height() - height) / 2;
        GLFW.glfwSetWindowPos(window, windowPosX[0], windowPosY[0]);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        System.out.println("OpenGL context created!");

        createCallbacks();
        System.out.println("Window callbacks created!");

        GLFW.glfwShowWindow(window);

        GLFW.glfwSwapInterval(1);

        updateProjectionMatrix();

        time = System.currentTimeMillis();
    }

    private void createCallbacks() {
        sizeCallback = new GLFWWindowSizeCallback() {
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                isResized = true;
            }
        };

        GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
        GLFW.glfwSetScrollCallback(window, input.getMouseScrollCallback());
        GLFW.glfwSetWindowSizeCallback(window, sizeCallback);
    }

    public void update() {
        if(isResized) {
            GL11.glViewport(0, 0, width, height);
            updateProjectionMatrix();
            isResized = false;
        }
        GL11.glClearColor(r, g, b, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GLFW.glfwPollEvents();

        frames++;
        if(System.currentTimeMillis() - time > 1000) {
            GLFW.glfwSetWindowTitle(window, title + " | FPS: " + frames);
            time = System.currentTimeMillis();
            frames = 0;
        }
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(window);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public void destroy() {
        input.destroy();
        sizeCallback.free();
        GLFW.glfwWindowShouldClose(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public void mouseState(boolean lock) {
        GLFW.glfwSetInputMode(this.window, GLFW.GLFW_CURSOR, lock ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
    }

    public boolean getMouseState() {
        return GLFW.glfwGetInputMode(this.window, GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED;
    }

    public void setClearColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void setFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        isResized = true;
        if(isFullscreen) {
            GLFW.glfwGetWindowPos(window, windowPosX, windowPosY);
            GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
        } else {
            GLFW.glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public long getWindow() {
        return window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void updateProjectionMatrix() {
        float aspectRatio = (float) width / height;
        this.projectionMatrix.setPerspective(Const.FOV, aspectRatio, Const.Z_NEAR, Const.Z_FAR);
    }
}
