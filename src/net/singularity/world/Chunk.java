package net.singularity.world;

import net.singularity.entity.Block;
import net.singularity.system.ObjectLoader;
import net.singularity.system.rendering.RenderManager;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    private final int CHUNK_BASE_SIZE = 16;
    private final int CHUNK_HEIGHT = 2;
    private final int MAX_BLOCKS_PER_CHUNK = CHUNK_BASE_SIZE * CHUNK_BASE_SIZE * CHUNK_HEIGHT;

    private static int id;
    private static Vector2i position;
    private static boolean loaded = false;

    private static List<Block> blocks;

    public Chunk(int id, Vector2i position) {
        this.id = id;
        this.position = new Vector2i(position).mul(CHUNK_BASE_SIZE);
    }

    public void init(ObjectLoader loader) throws Exception {
        blocks = new ArrayList<>();
        for(int i=0; i < MAX_BLOCKS_PER_CHUNK; i++) {
            float x, y, z;
            x = 2 * (i % CHUNK_BASE_SIZE);
            y = 2 * (i / (CHUNK_BASE_SIZE * CHUNK_BASE_SIZE));
            z = 2 * ((i / CHUNK_BASE_SIZE) % CHUNK_BASE_SIZE);

            Block block = new Block(0, id, loader, new Vector3f(x, y, z));
            blocks.add(block);
        }
        /*for(int x=0; x < CHUNK_BASE_SIZE; x++) {
            for(int y=0; y < CHUNK_BASE_SIZE; y++) {
                for(int z=0; z < CHUNK_HEIGHT; z++) {
                    Block block = new Block(0, id, loader, new Vector3f(x, z, y).mul(2).add(position));
                    blocks.add(block);
                }
            }
        }*/
        loaded = true;
    }

    public void update(float interval, RenderManager renderer) {
        if(!loaded)
            return;

        for(Block block : blocks)
            renderer.processBlock(block);
    }

    public void cleanup() {
        blocks.clear();
    }

    public static int getId() {
        return id;
    }

    public static Vector2i getPosition() {
        return position;
    }
}
