package net.singularity.rendering;

import net.singularity.block.Block;
import net.singularity.graphics.Textures;
import net.singularity.system.Camera;
import net.singularity.system.Window;
import net.singularity.text.Font;
import net.singularity.world.World;

public class Renderer {

    private final Window window;
    private final Textures textures;
    private BlockRenderer blockRenderer;
    private TextRenderer textRenderer;

    public Renderer(Window window, Textures textures) {
        this.window = window;
        this.textures = textures;
    }

    public void init() throws Exception {
        blockRenderer = new BlockRenderer(this);
        blockRenderer.init();

        textRenderer = new TextRenderer(this);
        textRenderer.init();
    }

    public void renderChunk(Camera camera, byte[] blocks, int width, int height, int x0, int y0, int z0) {
        blockRenderer.render(camera, blocks, width, height, x0, y0, z0);
    }

    public void render(Camera camera, Font font) {
        textRenderer.render(font);
    }

    public void processBlock(World world, int tileId, int x, int y, int z) {
        int flags = 0;
        if(tileId > 0) {
            flags = Block.blocks[tileId].getFacesToRender(world, x, y, z);
        }
        blockRenderer.getData().set((y * world.height + z) * world.width + x, flags);
    }

    public void destroy() {
        blockRenderer.destroy();
        textRenderer.destroy();
    }

    public Window getWindow() {
        return window;
    }

    public Textures getTextures() {
        return textures;
    }
}
