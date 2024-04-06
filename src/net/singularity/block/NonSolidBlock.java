package net.singularity.block;

public class NonSolidBlock extends Block {

    protected NonSolidBlock(int id, int tex) {
        super(id, tex);
    }

    public boolean isSolid() {
        return false;
    }

    public boolean blocksLight() {
        return false;
    }
}
