package net.singularity.block;

import net.singularity.physics.AABB;
import net.singularity.utils.Const;
import net.singularity.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Block {
    public static final Block[] blocks = new Block[256];
    public static final Block empty = null;
    public static final Block rock = new Block(1, 1);
    public static final Block grass = new GrassBlock(2);
    public static final Block dirt = new DirtBlock(3);
    public static final Block cobblestone = new Block(4, 16);

    public int tex;
    public final int id;

    protected Block(int id) {
        blocks[id] = this;
        this.id = id;
    }

    protected Block(int id, int tex) {
        this(id);
        this.tex = tex;
    }

    public List<Integer> getFacesToRender(World world, int x, int y, int z, int layer) {
        List<Integer> facesToRender = new ArrayList<>();

        if(this.shouldRenderFace(world, x, y - 1, z, layer)) {
            facesToRender.add(0);
        }
        if(this.shouldRenderFace(world, x, y + 1, z, layer) || this.getBlockType() == EBlockType.SLAB) {
            facesToRender.add(1);
        }
        if(this.shouldRenderFace(world, x, y, z - 1, layer)) {
            facesToRender.add(2);
        }
        if(this.shouldRenderFace(world, x, y, z + 1, layer)) {
            facesToRender.add(3);
        }
        if(this.shouldRenderFace(world, x - 1, y, z, layer)) {
            facesToRender.add(4);
        }
        if(this.shouldRenderFace(world, x + 1, y, z, layer)) {
            facesToRender.add(5);
        }

        return facesToRender;
    }

    public float[] getFaceTexCoords(int face) {
        int tex = getTexture(face);

        float u0 = (float) (tex % 16) / 16.0f;
        float u1 = u0 + Const.BLOCK_TEX_UV_STEP;
        float v0 = (float) (tex / 16) / 16.0f;
        float v1 = v0 + Const.BLOCK_TEX_UV_STEP;

        if(this.getBlockType() == EBlockType.SLAB && face != 0 && face != 1) {
            v0 += Const.BLOCK_TEX_UV_STEP / 2f;
        }

        float[] textureCoords;

        switch(face) {
            case 0:
                textureCoords = new float[] {
                        u0, v1,
                        u0, v0,
                        u1, v0,
                        u1, v1
                };
                break;
            case 1:
                textureCoords = new float[] {
                        u1, v1,
                        u1, v0,
                        u0, v0,
                        u0, v1
                };
                break;
            case 2, 4:
                textureCoords = new float[] {
                        u1, v0,
                        u0, v0,
                        u0, v1,
                        u1, v1
                };
                break;
            case 3:
                textureCoords = new float[] {
                        u0, v0,
                        u0, v1,
                        u1, v1,
                        u1, v0
                };
                break;
            case 5:
                textureCoords = new float[] {
                        u0, v1,
                        u1, v1,
                        u1, v0,
                        u0, v0
                };
                break;
            default:
                System.out.println("Unknown face!");
                return null;
        }

        return textureCoords;
    }

    public void tick(World world, int x, int y, int z, Random random) {

    }

    public void destroy() {

    }

    protected boolean shouldRenderFace(World world, int x, int y, int z, int layer) {
        return !(world.isSolidTile(x, y, z)) && world.isLit(x, y, z) ^ layer == 1; // && Block.blocks[world.getTile(x, y, z)].getBlockType() == EBlockType.NORMAL
    }

    protected int getTexture(int face) {
        return this.tex;
    }

    public AABB getAABB(int x, int y, int z) {
        return new AABB((float) x, (float) y, (float) z, (float) (x + 1), (float) (y + 1), (float) (z + 1));
    }

    public EBlockType getBlockType() {
        return EBlockType.NORMAL;
    }

    public boolean isSolid() {
        return true;
    }

    public boolean blocksLight() {
        return true;
    }
}
