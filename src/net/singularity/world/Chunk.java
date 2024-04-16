package net.singularity.world;

import net.singularity.entity.Player;
import net.singularity.physics.AABB;
import net.singularity.rendering.Renderer;

public class Chunk {
    public final World world;
    public final int x0;
    public final int y0;
    public final int z0;
    public final int x1;
    public final int y1;
    public final int z1;
    public final float x;
    public final float y;
    public final float z;
    public final AABB aabb;
    private boolean dirty = true;
    public long dirtiedTime = 0L;
    public static int updates;
    private static long totalTime;
    private static int totalUpdates;

    static {
        updates = 0;
        totalUpdates = 0;
        totalTime = 0L;
    }

    public Chunk(World world, int x0, int y0, int z0, int x1, int y1, int z1) {
        this.world = world;
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x = (x0 + x1) / 2f;
        this.y = (y0 + y1) / 2f;
        this.z = (z0 + z1) / 2f;
        this.aabb = new AABB(x0, y0, z0, x1, y1, z1);
    }

    public void rebuild(Renderer renderer) {
        this.dirty = false;
        int tiles = 0;
        updates++;

        long before = System.nanoTime();
        for(int x = this.x0; x < this.x1; ++x) {
            for(int y = this.y0; y < this.y1; ++y) {
                for(int z = this.z0; z < this.z1; ++z) {
                    int tileId = this.world.getTile(x, y, z);
                    renderer.processBlock(this.world, tileId, x, y, z); //TO SEE
                    if (tileId > 0) {
                        tiles++;
                    }
                }
            }
        }

        long after = System.nanoTime();
        if(tiles > 0) {
            totalTime += after - before;
            totalUpdates++;
        }
    }

    public void setDirty() {
        if(!this.dirty) {
            this.dirtiedTime = System.currentTimeMillis();
        }

        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public float distanceToSqr(Player player) {
        float xd = player.pos.x - this.x;
        float yd = player.pos.y - this.y;
        float zd = player.pos.z - this.z;
        return xd * xd + yd * yd + zd * zd;
    }
}
