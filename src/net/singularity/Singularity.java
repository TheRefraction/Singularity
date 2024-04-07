package net.singularity;

import net.singularity.system.*;
import net.singularity.rendering.RenderManager;
import net.singularity.rendering.WorldRenderer;
import net.singularity.utils.Transformation;
import net.singularity.world.World;

public class Singularity implements ILogic {
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final Window window;
    private final World world;
    private final WorldRenderer worldRenderer;
    private final Camera camera;

    public Singularity() {
        loader = new ObjectLoader();
        renderer = new RenderManager(loader);
        window = Main.getWindow();
        camera = new Camera();
        world = new World(loader, camera, 32, 16, 32);
        worldRenderer = new WorldRenderer(world);
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
        world.getPlayer().updateMouseInput(mouseInput);
    }

    @Override
    public void update(float interval) {
        world.update(renderer);
        camera.getFrustumFilter().updateFrustum(window.getProjectionMatrix(), Transformation.getViewMatrix(camera));
        worldRenderer.render(renderer, 0);
        worldRenderer.render(renderer, 1);
    }

    @Override
    public void render() {
        window.setClearColor(0.2f, 0.5f, 0.8f, 0);
        renderer.render(camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
