package net.singularity.rendering;

import net.singularity.block.Block;
import net.singularity.graphics.Textures;
import net.singularity.system.Camera;
import net.singularity.system.Window;
import net.singularity.world.World;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;
public class Renderer {

    private Window window;
    private Textures textures;
    private BlockRenderer blockRenderer;

    public Renderer(Window window, Textures textures) {
        this.window = window;
        this.textures = textures;
    }

    public void init() throws Exception {
        blockRenderer = new BlockRenderer(this);
        blockRenderer.init();
    }

    public void render(Camera camera) {
        blockRenderer.render(camera);
    }

    public void processBlock(World world, int tileId, int x, int y, int z, int layer) {
        List<Integer> facesToRender = Block.blocks[tileId].getFacesToRender(world, x, y, z, layer);
        for(int face : facesToRender) {
            Vector3i key = new Vector3i(tileId, face, layer);
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

    public void destroy() {
        blockRenderer.destroy();
    }

    public Window getWindow() {
        return window;
    }

    public Textures getTextures() {
        return textures;
    }
}
