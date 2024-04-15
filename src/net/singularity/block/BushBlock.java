package net.singularity.block;

import net.singularity.world.World;

import java.util.Random;

public class BushBlock extends Block{
    protected BushBlock(int id, int tex) {
        super(id, tex);
    }

    public boolean isSolid() {
        return false;
    }

    public boolean blocksLight() {
        return false;
    }

    public EBlockType getBlockType() {
        return EBlockType.BUSH;
    }

    public void tick(World world, int x, int y, int z, Random random) {
        if((id == rose.id || id == dandelion.id) && world.getTile(x, y - 1, z) != dirt.id && world.getTile(x, y - 1, z) != grass.id) {
            world.setTile(x, y, z, 0);
        }
    }
}
