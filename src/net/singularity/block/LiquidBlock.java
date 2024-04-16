package net.singularity.block;

import net.singularity.physics.AABB;
import net.singularity.world.World;

import java.util.Random;

public class LiquidBlock extends Block {
    protected LiquidBlock(int id, int tex) {
        super(id, tex);
    }

    public boolean isSolid() {
        return false;
    }

    public AABB getAABB(int x, int y, int z) {
        return null;
    }

    protected boolean shouldRenderFace(World world, int x, int y, int z) {
        return !(world.isSolidTile(x, y, z) || world.getTile(x, y, z) == id);
    }

    public EBlockType getBlockType() {
        return EBlockType.NORMAL;
    }

    public void tick(World world, int x, int y, int z, Random random) {
        this.updateWater(world, x, y, z, 0);
    }

    public boolean updateWater(World world, int x, int y, int z, int depth) {
        boolean hasChanged = false;

        boolean change;
        do {
            --y;
            if (world.getTile(x, y, z) != 0) {
                break;
            }

            change = world.setTile(x, y, z, this.id);
            if (change) {
                hasChanged = true;
            }
        } while(change);

        ++y;
        if (!hasChanged) {
            hasChanged |= this.checkWater(world, x - 1, y, z, depth);
            hasChanged |= this.checkWater(world, x + 1, y, z, depth);
            hasChanged |= this.checkWater(world, x, y, z - 1, depth);
            hasChanged |= this.checkWater(world, x, y, z + 1, depth);
        }

        if (!hasChanged) {
            world.setTile(x, y, z, id); //NoUpdate
        }

        return hasChanged;
    }

    private boolean checkWater(World world, int x, int y, int z, int depth) {
        boolean hasChanged = false;
        int type = world.getTile(x, y, z);
        if (type == 0) {
            boolean changed = world.setTile(x, y, z, this.id);
            if (changed && depth < 3) {
                hasChanged |= this.updateWater(world, x, y, z, depth + 1);
            }
        }

        return hasChanged;
    }
}
