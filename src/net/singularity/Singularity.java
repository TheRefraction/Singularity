package net.singularity;

import net.singularity.system.*;
import net.singularity.system.rendering.RenderManager;
import net.singularity.utils.Transformation;
import net.singularity.world.World;

public class Singularity implements ILogic {
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final Window window;
    private final World world;
    private final Camera camera;

    public Singularity() {
        loader = new ObjectLoader();
        renderer = new RenderManager(loader);
        window = Main.getWindow();
        camera = new Camera();
        world = new World(loader, camera);
    }

    @Override
    public void init() throws Exception {
        loader.loadBlockTexture();
        loader.initBlockModels();
        renderer.init();
        world.init();
    }

    @Override
    public void input(MouseInput mouseInput) {
        world.getPlayer().updateInput(mouseInput);
    }

    @Override
    public void update(float interval) {
        camera.getFrustumFilter().updateFrustum(window.getProjectionMatrix(), Transformation.getViewMatrix(camera));
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
