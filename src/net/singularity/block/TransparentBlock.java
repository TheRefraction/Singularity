package net.singularity.block;

public class TransparentBlock extends Block {

    protected TransparentBlock(int id, int tex) {
        super(id, tex);
    }

    public boolean blocksLight() {
        return false;
    }
}
