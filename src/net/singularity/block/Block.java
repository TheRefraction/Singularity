package net.singularity.block;

import net.singularity.physics.AABB;
import net.singularity.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Block {
    public static final Block[] blocks = new Block[256];
    public static final Block empty = null;
    public static final Block rock = new Block(1, 1);
    public static final Block grass = new GrassBlock(2);
    public static final Block dirt = new Block(3, 2);
    public static final Block wood = new Block(4, 4);
    public static final Block glass = new NonSolidBlock(5, 39);

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

    public List<Integer> getFacesToRender(World world, int x, int y, int z) {
        List<Integer> facesToRender = new ArrayList<>();

        if(this.shouldRenderFace(world, x, y - 1, z)) {
            facesToRender.add(0);
        }
        if(this.shouldRenderFace(world, x, y + 1, z)) {
            facesToRender.add(1);
        }
        if(this.shouldRenderFace(world, x, y, z - 1)) {
            facesToRender.add(2);
        }
        if(this.shouldRenderFace(world, x, y, z + 1)) {
            facesToRender.add(3);
        }
        if(this.shouldRenderFace(world, x - 1, y, z)) {
            facesToRender.add(4);
        }
        if(this.shouldRenderFace(world, x + 1, y, z)) {
            facesToRender.add(5);
        }

        return facesToRender;
    }

    public float[] getFaceTexCoords(int face) {
        int tex = getTexture(face);

        float u0 = (float) (tex % 16) / 16.0f;
        float u1 = u0 + 0.0624375f;
        float v0 = (float) (tex / 16) / 16.0f;
        float v1 = v0 + 0.0624375f;

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

    protected boolean shouldRenderFace(World world, int x, int y, int z) {
        return !world.isSolidTile(x, y, z);
    }

    protected int getTexture(int face) {
        return this.tex;
    }

    public AABB getAABB(int x, int y, int z) {
        return new AABB((float) x, (float) y, (float) z, (float) (x + 1), (float) (y + 1), (float) (z + 1));
    }

    public boolean isSolid() {
        return true;
    }

    public boolean blocksLight() {
        return true;
    }
}
