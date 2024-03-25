package net.singularity;

import net.singularity.system.*;
import net.singularity.system.rendering.RenderManager;
import net.singularity.utils.Const;
import net.singularity.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;


public class Singularity implements ILogic {

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final Window window;
    private final World world;

    private Camera camera;

    private Vector3f cameraInc;

    public Singularity() {
        renderer = new RenderManager();
        window = Main.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        world = new World(loader, camera);

        cameraInc = new Vector3f(0,0,0);
    }

    @Override
    public void init() throws Exception {
        renderer.init();
        world.init();
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);

        if(window.isKeyPressed(GLFW.GLFW_KEY_Z))
            cameraInc.z = -1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;

        if(window.isKeyPressed(GLFW.GLFW_KEY_Q))
            cameraInc.x = -1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;

        if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE))
            cameraInc.y = 1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
            cameraInc.y = -1;

        cameraInc.mul(Const.CAMERA_MOVE_SPEED);
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x, cameraInc.y, cameraInc.z);

        if(mouseInput.isLeftButtonPress()) {
            Vector2f rotVec = mouseInput.getDisplVec().mul(Const.MOUSE_SENSITIVITY);
            camera.moveRotation(rotVec.x, rotVec.y, 0);
        }

        world.update(interval, renderer);
    }

    @Override
    public void render() {
        window.setClearColor(0.2f, 0.2f, 1, 0);
        renderer.render(camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
