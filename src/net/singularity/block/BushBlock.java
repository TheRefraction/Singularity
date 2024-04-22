package net.singularity.block;

import net.singularity.physics.AABB;
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

    public AABB getAABB(int x, int y, int z) {
        return new AABB(x, y, z, x + 1, y + 1, z + 1);
    }

    public EBlockType getBlockType() {
        return EBlockType.BUSH;
    }

    public void tick(World world, int x, int y, int z, Random random) {
        if(((id == rose.id || id == dandelion.id || id == sappling.id) && world.getTile(x, y - 1, z) != dirt.id && world.getTile(x, y - 1, z) != grass.id)
                || ((id == mushroomRed.id || id == mushroomBrown.id) && world.getTile(x, y - 1, z) != rock.id)) {
            world.setTile(x, y, z, 0);
        }
        if (id == sappling.id && world.isLit(x, y, z)) {
            world.setTile(x, y, z, log.id);
            world.setTile(x, y + 1, z, log.id);
            world.setTile(x, y + 2, z, log.id);
            world.setTile(x, y + 3, z, log.id);
            for(int i = -2; i < 3; i++) {
                for(int j = -2; j < 3; j++) {
                    for(int k = 0; k < 3; k++) {
                        if(k == 2 && i != -2 && j != -2 && i != 2 && j != 2) {
                            world.setTile(x + i, y + 4, z + j, leaves.id);
                        } else if(k != 2 && (i != 0 || j != 0) && (Math.abs(i) != 2 || Math.abs(j) != 2)) {
                            world.setTile(x + i, y + 2 + k, z + j, leaves.id);
                        }
                    }
                }
            }
        }
    }
}
