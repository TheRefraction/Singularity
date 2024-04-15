package net.singularity.block;

import net.singularity.world.World;

import java.util.Random;

public class DirtBlock extends Block {
    protected DirtBlock(int id) {
        super(id);
        this.tex = 2;
    }

    public void tick(World world, int x, int y, int z, Random random) {
        if(world.isLit(x, y, z)) {
            world.setTile(x, y, z, grass.id);
        }
    }
}
