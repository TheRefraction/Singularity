package net.singularity.system.rendering;

import net.singularity.entity.Block;
import net.singularity.Main;
import net.singularity.entity.Entity;
import net.singularity.system.Camera;
import net.singularity.system.Window;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
public class RenderManager {

    private final Window window;
    private EntityRenderer entityRenderer;
    private BlockRenderer blockRenderer;

    public RenderManager() {
        window = Main.getWindow();
    }

    public void init() throws Exception {
        entityRenderer = new EntityRenderer();
        entityRenderer.init();
        blockRenderer = new BlockRenderer();
        blockRenderer.init();
    }

    public void render(Camera camera) {
        clear();

        if(window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        entityRenderer.render(camera);
        blockRenderer.render(camera);
    }

    public void processEntity(Entity entity) {
        List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
        if(entityList != null)
            entityList.add(entity);
        else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRenderer.getEntities().put(entity.getModel(), newEntityList);
        }
    }

    public void processBlock(Block block) {
        blockRenderer.getBlocks().add(block);
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        entityRenderer.cleanup();
        blockRenderer.cleanup();
    }
}
