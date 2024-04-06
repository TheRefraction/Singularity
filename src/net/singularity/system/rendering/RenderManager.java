package net.singularity.system.rendering;

import net.singularity.Main;
import net.singularity.block.Block;
import net.singularity.entity.Entity;
import net.singularity.system.Camera;
import net.singularity.system.ObjectLoader;
import net.singularity.system.Window;
import net.singularity.world.World;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
public class RenderManager {

    private final Window window;
    private final ObjectLoader loader;
    private EntityRenderer entityRenderer;
    private BlockRenderer blockRenderer;

    public RenderManager(ObjectLoader loader) {
        window = Main.getWindow();
        this.loader = loader;
    }

    public void init() throws Exception {
        entityRenderer = new EntityRenderer(this);
        entityRenderer.init();

        blockRenderer = new BlockRenderer(this);
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

    public void processBlock(World world, int tileId, int x, int y, int z) {
        List<Integer> facesToRender = Block.blocks[tileId].getFacesToRender(world, x, y, z);
        for(int face : facesToRender) {
            Vector2i key = new Vector2i(tileId, face);
            List<Vector3f> blockList = blockRenderer.getBlocks().get(key);
            if (blockList != null)
                blockList.add(new Vector3f(x, y, z));
            else {
                List<Vector3f> newPosList = new ArrayList<>();
                newPosList.add(new Vector3f(x, y, z));
                blockRenderer.getBlocks().put(key, newPosList);
            }
        }
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        entityRenderer.cleanup();
        blockRenderer.cleanup();
    }

    public ObjectLoader getLoader() {
        return this.loader;
    }
}
