package net.singularity.world;

import net.singularity.entity.Block;
import net.singularity.system.rendering.RenderManager;
import net.singularity.utils.Const;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    private static int id;
    private static World world;
    private static Vector2i position;
    private static boolean loaded = false;

    private static List<Block> blocks;

    public Chunk(int id, World world, Vector2i position) {
        this.id = id;
        this.world = world;
        this.position = new Vector2i(position).mul(Const.CHUNK_BASE_SIZE);
    }

    public void init() throws Exception {
        blocks = new ArrayList<>();
        for(int i=0; i < Const.MAX_BLOCKS_PER_CHUNK; i++) {
            float x, y, z;
            x = position.x + 2 * (i % Const.CHUNK_BASE_SIZE);
            y = 2 * (i / (Const.CHUNK_BASE_SIZE * Const.CHUNK_BASE_SIZE));
            z = position.y + 2 * ((i / Const.CHUNK_BASE_SIZE) % Const.CHUNK_BASE_SIZE);

            Block block = new Block(0, i, this, new Vector3f(x, y, z));
            blocks.add(block);
        }

        loaded = true;
    }

    public void update(float interval, RenderManager renderer) {
        if(!loaded)
            return;

        for(Block block : blocks) {
            block.update(interval);
            renderer.processBlock(block);
        }
    }

    public void cleanup() {
        blocks.clear();
    }

    public static int getId() {
        return id;
    }

    public static World getWorld() {
        return world;
    }

    public static List<Block> getBlocks() {
        return blocks;
    }

    public static Vector2i getPosition() {
        return position;
    }
}
