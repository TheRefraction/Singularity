package net.singularity.block;

import net.singularity.physics.AABB;

public class SlabBlock extends Block{
    protected SlabBlock(int id, int tex) {
        super(id, tex);
    }

    public AABB getAABB(int x, int y, int z) {
        return new AABB((float) x, (float) y, (float) z, (float) (x + 1), (float) (y + 0.5f), (float) (z + 1));
    }

    public EBlockType getBlockType() {
        return EBlockType.SLAB;
    }
}
