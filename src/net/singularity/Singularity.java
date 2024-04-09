package net.singularity;

import net.singularity.graphics.Textures;
import net.singularity.rendering.Renderer;
import net.singularity.rendering.WorldRenderer;
import net.singularity.system.Camera;
import net.singularity.system.Input;
import net.singularity.system.Window;
import net.singularity.utils.Const;
import net.singularity.utils.Transformation;
import net.singularity.world.World;
import org.lwjgl.glfw.GLFW;

public class Singularity implements Runnable {
    public Thread game;
    public Window window;
    public Renderer renderer;
    public Camera camera;
    public static World world;
    private WorldRenderer worldRenderer;
    public static Textures textures;

    public void start() {
        game = new Thread(this, "game");
        game.start();
    }

    public void init()  {
        this.window = new Window(Const.TITLE, 800, 600);
        this.window.setClearColor(0.2f, 0.2f, 0.8f);
        this.window.init();

        System.out.println("Creating Textures and Renderer");
        textures = new Textures();
        this.renderer = new Renderer(this.window, textures);
        try {
            this.renderer.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Creating Camera");
        this.camera = new Camera();

        System.out.println("Creating World");
        world = new World(this.camera, 256,256,256);
        this.worldRenderer = new WorldRenderer(world);

        world.init();
        System.out.println("Done!");
    }

    public void run() {
        init();
        while(!window.shouldClose()) {
            update();
            render();
            if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
            if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) window.mouseState(true);
            else if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) window.mouseState(false);
        }
        close();
    }

    private void update() {
        window.update();
        if(window.getMouseState()) {
            camera.getFrustumFilter().updateFrustum(window.getProjectionMatrix(), Transformation.getViewMatrix(camera));
            world.update(renderer);
        }
    }

    private void render() {
        worldRenderer.render(renderer, 0);
        worldRenderer.render(renderer, 1);

        renderer.render(camera);
        window.swapBuffers();
    }

    private void close() {
        renderer.destroy();
        window.destroy();
    }
}
