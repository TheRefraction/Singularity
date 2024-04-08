package net.singularity.rendering;

import net.singularity.world.Chunk;
import net.singularity.world.World;

public class WorldRenderer {
    private World world;
    private Chunk[] chunks;
    private int xChunks;
    private int yChunks;
    private int zChunks;

    public WorldRenderer(World world) {
        this.world = world;
        this.xChunks = world.width / 16;
        this.yChunks = world.depth / 16;
        this.zChunks = world.height / 16;
        this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];

        for(int x = 0; x < this.xChunks; ++x) {
            for(int y = 0; y < this.yChunks; ++y) {
                for(int z = 0; z < this.zChunks; ++z) {
                    int x0 = x * 16;
                    int y0 = y * 16;
                    int z0 = z * 16;
                    int x1 = (x + 1) * 16;
                    int y1 = (y + 1) * 16;
                    int z1 = (z + 1) * 16;

                    if(x1 > world.width) x1 = world.width;
                    if(y1 > world.depth) y1 = world.depth;
                    if(z1 > world.height) z1 = world.height;

                    this.chunks[(x + y * this.xChunks) * this.zChunks + z] = new Chunk(world, x0, y0, z0, x1, y1, z1);
                }
            }
        }
    }

    public void render(Renderer renderer, int layer) {
        for (Chunk chunk : this.chunks) {
            if(this.world.getCamera().getFrustumFilter().insideFrustum(chunk.aabb)) {
                chunk.render(renderer, layer);
            }
        }
    }
}
