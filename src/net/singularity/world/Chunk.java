package net.singularity.world;

import net.singularity.physics.AABB;
import net.singularity.system.rendering.RenderManager;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Chunk {
    public final World world;
    public final Vector3i posMin;
    public final Vector3i posMax;
    public final Vector3f position;
    public final AABB boundingBox;

    public Chunk(World world, Vector3i posMin, Vector3i posMax) {
        this.world = world;
        this.posMin = posMin;
        this.posMax = posMax;
        this.position = new Vector3f((posMin.x + posMax.x) / 2.0f, (posMin.y + posMax.y) / 2.0f, (posMin.z + posMax.z) / 2.0f);
        this.boundingBox = new AABB((float)posMin.x, (float)posMin.y, (float)posMin.z, (float)posMax.x, (float)posMax.y, (float)posMax.z);
    }

    public void render(RenderManager renderer) {
        int tiles = 0;
        for(int x = this.posMin.x; x < this.posMax.x; ++x) {
            for(int y = this.posMin.y; y < this.posMax.y; ++y) {
                for(int z = this.posMin.z; z < this.posMax.z; ++z) {
                    int tileId = this.world.getTile(x, y, z);
                    if (tileId > 0) {
                        renderer.processBlock(this.world, tileId, x, y, z);
                        ++tiles;
                    }
                }
            }
        }
    }
}
