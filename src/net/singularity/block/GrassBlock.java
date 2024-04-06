package net.singularity.block;

public class GrassBlock extends Block {
    protected GrassBlock(int id) {
        super(id);
        this.tex = 3;
    }

    protected int getTexture(int face) {
        if(face == 1) return 0;
        else return face == 0 ? 2 : 3;
    }
}
