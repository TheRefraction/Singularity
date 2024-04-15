package net.singularity.block;

import net.singularity.world.World;

import java.util.Random;

public class GrassBlock extends Block {
    protected GrassBlock(int id) {
        super(id);
        this.tex = 3;
    }

    protected int getTexture(int face) {
        if(face == 1) return 0;
        else return face == 0 ? 2 : 3;
    }

    public void tick(World world, int x, int y, int z, Random random) {
        if((world.getTile(x, y + 1, z) != 0 && world.isSolidTile(x, y + 1, z)) || !world.isLit(x, y, z)) {
            world.setTile(x, y, z, dirt.id);
        }
    }
}
