package net.singularity.block;

import net.singularity.world.World;

public class TransparentBlock extends Block {

    protected TransparentBlock(int id, int tex) {
        super(id, tex);
    }

    public boolean blocksLight() {
        return false;
    }

    protected boolean shouldRenderFace(World world, int x, int y, int z) {
        return !world.isSolidTile(x, y, z);
    }
}
