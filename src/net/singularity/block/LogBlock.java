package net.singularity.block;

public class LogBlock extends Block {
    protected LogBlock(int id, int tex) {
        super(id, tex);
    }

    protected int getTexture(int face) {
        if(face == 0 || face == 1) return tex + 1;
        else return tex;
    }
}
