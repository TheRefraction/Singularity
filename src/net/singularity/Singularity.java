package net.singularity;

import net.singularity.graphics.Textures;
import net.singularity.rendering.Renderer;
import net.singularity.rendering.WorldRenderer;
import net.singularity.system.Camera;
import net.singularity.system.Timer;
import net.singularity.system.Window;
import net.singularity.text.Font;
import net.singularity.utils.Const;
import net.singularity.utils.Transformation;
import net.singularity.world.Chunk;
import net.singularity.world.World;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Singularity implements Runnable {
    public Thread game;
    public Window window;
    public Timer timer;
    public Renderer renderer;
    public Camera camera;
    public Font font;
    public static World world;
    private WorldRenderer worldRenderer;
    public static Textures textures;

    public void start() {
        game = new Thread(this, "game");
        game.start();
    }

    public void init()  {
        this.window = new Window(Const.TITLE, 800, 600, true);
        this.window.setClearColor(0.3f, 0.6f, 0.8f);
        this.window.init();

        System.out.println("Creating Timer");
        this.timer = new Timer();
        this.timer.init();

        System.out.println("Creating Textures and Renderer");
        textures = new Textures();

        this.renderer = new Renderer(this.window, textures);
        try {
            this.renderer.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Creating Font");
        this.font = new Font();

        System.out.println("Creating Camera");
        this.camera = new Camera();

        System.out.println("Creating World");
        world = new World(this.camera, 256,64,256);
        this.worldRenderer = new WorldRenderer(world);

        System.out.println("Done!");
    }

    public void run() {
        try {
            init();
        } catch (Exception ex) {
            Logger.getLogger(Singularity.class.getName()).log(Level.SEVERE, null, ex);
        }

        float delta;
        float acc = 0f;
        float interval = 1f / Const.TARGET_UPS;

        while(!window.shouldClose()) {
            delta = timer.getDelta();
            acc += delta;

            if(acc >= interval) {
                update();
                timer.updateUPS();
                Chunk.updates = 0;
                acc -= interval;
            }

            render();
            timer.updateFPS();

            window.update();
            timer.update();

            if (!window.isVsync()) {
                sync(Const.TARGET_FPS);
            }

            System.out.println("FPS: " + timer.getFPS() + " UPS: " + timer.getUPS() + " Chunk updates: " + Chunk.updates);
        }

        world.save();
        close();
    }

    private void update() {
        if(window.getMouseState()) {
            camera.getFrustumFilter().updateFrustum(window.getProjectionMatrix(), Transformation.getViewMatrix(camera));
            camera.pick(world);
            world.update();
        }
    }

    private void render() {
        worldRenderer.updateDirtyChunks(renderer, world.getPlayer());
        worldRenderer.render(renderer);

        renderer.render(camera, font);
        window.swapBuffers();
    }

    private void close() {
        renderer.destroy();
        window.destroy();
    }

    public void sync(int fps) {
        double lastLoopTime = timer.getLastLoopTime();
        double now = timer.getTime();
        float targetTime = 1f / fps;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();

            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Singularity.class.getName()).log(Level.SEVERE, null, ex);
            }

            now = timer.getTime();
        }
    }
}
