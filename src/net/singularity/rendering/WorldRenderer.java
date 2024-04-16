package net.singularity.rendering;

import net.singularity.entity.Player;
import net.singularity.utils.Const;
import net.singularity.utils.Utils;
import net.singularity.world.Chunk;
import net.singularity.world.DirtyChunkSorter;
import net.singularity.world.World;
import net.singularity.world.WorldListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldRenderer implements WorldListener {
    private final World world;
    private final Chunk[] chunks;
    private final int xChunks;
    private final int yChunks;
    private final int zChunks;

    public WorldRenderer(World world) {
        this.world = world;
        world.addListener(this);

        this.xChunks = world.width / Const.CHUNK_SIZE;
        this.yChunks = world.depth / Const.CHUNK_SIZE;
        this.zChunks = world.height / Const.CHUNK_SIZE;
        this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];

        for(int x = 0; x < this.xChunks; ++x) {
            for(int y = 0; y < this.yChunks; ++y) {
                for(int z = 0; z < this.zChunks; ++z) {
                    int x0 = x * Const.CHUNK_SIZE;
                    int y0 = y * Const.CHUNK_SIZE;
                    int z0 = z * Const.CHUNK_SIZE;
                    int x1 = (x + 1) * Const.CHUNK_SIZE;
                    int y1 = (y + 1) * Const.CHUNK_SIZE;
                    int z1 = (z + 1) * Const.CHUNK_SIZE;

                    if(x1 > world.width) x1 = world.width;
                    if(y1 > world.depth) y1 = world.depth;
                    if(z1 > world.height) z1 = world.height;

                    this.chunks[(x + y * this.xChunks) * this.zChunks + z] = new Chunk(world, x0, y0, z0, x1, y1, z1);
                }
            }
        }
    }

    public void render(Renderer renderer) {
        for (Chunk chunk : this.chunks) {
            if(this.world.getCamera().getFrustumFilter().insideFrustum(chunk.aabb) && (Utils.getDistance(chunk.x, chunk.y, chunk.z, world.getPlayer().pos.x, world.getPlayer().pos.y, world.getPlayer().pos.z) <= 48f)) {
                renderer.renderChunk(world.getCamera(), world.blocks, world.width, world.height, chunk.x0, chunk.y0, chunk.z0);
            }
        }
    }

    public void updateDirtyChunks(Renderer renderer, Player player) {
        List<Chunk> dirty = this.getAllDirtyChunks();
        if (dirty != null) {
            Collections.sort(dirty, new DirtyChunkSorter(player, this.world.getCamera().getFrustumFilter()));

            for(int i = 0; i < 8 && i < dirty.size(); ++i) {
                dirty.get(i).rebuild(renderer);
            }
        }
    }

    public List<Chunk> getAllDirtyChunks() {
        ArrayList dirty = null;

        for (Chunk chunk : this.chunks) {
            if (chunk.isDirty()) {
                if (dirty == null) {
                    dirty = new ArrayList();
                }

                dirty.add(chunk);
            }
        }

        return dirty;
    }

    public void setDirty(int x0, int y0, int z0, int x1, int y1, int z1) {
        x0 /= Const.CHUNK_SIZE;
        x1 /= Const.CHUNK_SIZE;
        y0 /= Const.CHUNK_SIZE;
        y1 /= Const.CHUNK_SIZE;
        z0 /= Const.CHUNK_SIZE;
        z1 /= Const.CHUNK_SIZE;
        if(x0 < 0) {
            x0 = 0;
        }

        if(y0 < 0) {
            y0 = 0;
        }

        if(z0 < 0) {
            z0 = 0;
        }

        if(x1 >= this.xChunks) {
            x1 = this.xChunks - 1;
        }

        if(y1 >= this.yChunks) {
            y1 = this.yChunks - 1;
        }

        if(z1 >= this.zChunks) {
            z1 = this.zChunks - 1;
        }

        for(int x = x0; x <= x1; ++x) {
            for(int y = y0; y <= y1; ++y) {
                for(int z = z0; z <= z1; ++z) {
                    this.chunks[(x + y * this.xChunks) * this.zChunks + z].setDirty();
                }
            }
        }

    }

    public void tileChanged(int x, int y, int z) {
        this.setDirty(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
    }

    public void lightColumnChanged(int x, int z, int y0, int y1) {
        this.setDirty(x - 1, y0 - 1, z - 1, x + 1, y1 + 1, z + 1);
    }

    public void allChanged() {
        this.setDirty(0, 0, 0, this.world.width, this.world.depth, this.world.height);
    }
}
