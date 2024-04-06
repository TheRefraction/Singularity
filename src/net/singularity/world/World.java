package net.singularity.world;

import net.singularity.block.Block;
import net.singularity.entity.Entity;
import net.singularity.entity.Player;
import net.singularity.system.Camera;
import net.singularity.system.ObjectLoader;
import net.singularity.system.rendering.RenderManager;
import net.singularity.utils.Const;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private final ObjectLoader loader;
    private final Camera camera;
    private final Player player;
    public byte[] blocks;
    private List<Entity> entities;
    private Chunk chunk;
    private final Random random = new Random();
    int unprocessed = 0;

    public World(ObjectLoader loader, Camera camera) {
        this.loader = loader;
        this.camera = camera;
        this.player = new Player(this, new Vector3f(-1,0,-1), new Vector3f(0,0,0));
        this.blocks = genTest();
        this.chunk = new Chunk(this, new Vector3i(0,0,0), new Vector3i(Const.CHUNK_WIDTH, Const.CHUNK_DEPTH, Const.CHUNK_HEIGHT));
    }

    public void init() throws Exception {
        entities = new ArrayList<>();
    }

    public void update(float interval, RenderManager renderer) {
        tick();
        player.update(interval);

        chunk.render(renderer);

        for(Entity entity : entities)
            renderer.processEntity(entity);
    }

    public void cleanup() {
    }

    public byte[] genTest() {
        Random rand = new Random();
        int w = Const.CHUNK_WIDTH;
        int h = Const.CHUNK_HEIGHT;
        int d = Const.CHUNK_DEPTH;
        byte[] blocks = new byte[w*h*d];
        for(int x = 0; x < w; ++x) {
            for(int y = 0; y < d; ++y) {
                for(int z = 0; z < h; ++z) {
                    int i = (y * h + z) * w + x;
                    int id = rand.nextInt(4);
                    blocks[i] = (byte)id;
                }
            }
        }
        return blocks;
    }

    public ObjectLoader getLoader() {
        return loader;
    }

    public Camera getCamera() {
        return camera;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTile(int x, int y, int z) {
        int w = Const.CHUNK_WIDTH;
        int h = Const.CHUNK_HEIGHT;
        int d = Const.CHUNK_DEPTH;
        return x >= 0 && y >= 0 && z >= 0 && x < w && y < d && z < h ? this.blocks[(y * h + z) * w + x] : 0;
    }

    public boolean isSolidTile(int x, int y, int z) {
        Block block = Block.blocks[this.getTile(x, y, z)];
        return block != null && block.isSolid();
    }

    public void tick() {
        this.unprocessed += 16*16*16;
        int ticks = this.unprocessed / 400;
        this.unprocessed -= ticks * 400;

        for(int i = 0; i < ticks; ++i) {
            int x = this.random.nextInt(16);
            int y = this.random.nextInt(16);
            int z = this.random.nextInt(16);
            Block block = Block.blocks[this.getTile(x, y, z)];
            if (block != null) {
                block.tick(this, x, y, z, this.random);
            }
        }

    }
}
