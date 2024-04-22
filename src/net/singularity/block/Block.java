package net.singularity.block;

import net.singularity.physics.AABB;
import net.singularity.utils.Const;
import net.singularity.world.World;

import java.util.Random;

public class Block {
    public static final Block[] blocks = new Block[256];
    public static final Block empty = null;
    public static final Block rock = new Block(1, 1);
    public static final Block grass = new GrassBlock(2);
    public static final Block dirt = new DirtBlock(3);
    public static final Block cobblestone = new Block(4, 16);
    public static final Block bedrock = new Block(5, 17);
    public static final Block rose = new BushBlock(6, 12);
    public static final Block dandelion = new BushBlock(7, 13);
    public static final Block mushroomRed = new BushBlock(8, 28);
    public static final Block mushroomBrown = new BushBlock(9, 29);
    public static final Block web = new BushBlock(10, 11);
    public static final Block glass = new TransparentBlock(11, 39);
    public static final Block water = new LiquidBlock(12, 14);
    public static final Block log = new LogBlock(13, 20);
    public static final Block leaves = new NonSolidBlock(14, 22);
    public static final Block wood = new Block(15, 4);
    public static final Block sappling = new BushBlock(16, 15);

    public static final int BOTTOM_FACE = 1;
    public static final int TOP_FACE = 2;
    public static final int BACK_FACE = 4;
    public static final int FRONT_FACE = 8;
    public static final int RIGHT_FACE = 16;
    public static final int LEFT_FACE = 32;
    public static final int LAYER_BOTTOM = 64;
    public static final int LAYER_TOP = 128;
    public static final int LAYER_BACK = 256;
    public static final int LAYER_FRONT = 512;
    public static final int LAYER_RIGHT = 1024;
    public static final int LAYER_LEFT = 2048;

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

    public int getFacesToRender(World world, int x, int y, int z) {
        int flags = 0;

        if(this.getBlockType() == EBlockType.BUSH) {
            if(this.shouldRenderFace(world, x, y - 1, z) || this.shouldRenderFace(world, x, y + 1, z) ||
                    this.shouldRenderFace(world, x - 1, y, z) || this.shouldRenderFace(world, x + 1, y, z) ||
                    this.shouldRenderFace(world, x, y, z - 1) || this.shouldRenderFace(world, x, y, z + 1)) {
                flags = 1 + (world.isLit(x, y, z) ? 0 : 1);
            }
        } else {
            if(this.shouldRenderFace(world, x, y - 1, z)) {
                flags |= BOTTOM_FACE;
                flags |= (world.isLit(x, y - 1, z) ? 0 : 1) * LAYER_BOTTOM;
            }
            if(this.shouldRenderFace(world, x, y + 1, z)) {
                flags |= TOP_FACE;
                flags |= (world.isLit(x, y + 1, z) ? 0 : 1) * LAYER_TOP;
            }
            if(this.shouldRenderFace(world, x, y, z - 1)) {
                flags |= BACK_FACE;
                flags |= (world.isLit(x, y, z - 1) ? 0 : 1) * LAYER_BACK;
            }
            if(this.shouldRenderFace(world, x, y, z + 1)) {
                flags |= FRONT_FACE;
                flags |= (world.isLit(x, y, z + 1) ? 0 : 1) * LAYER_FRONT;
            }
            if(this.shouldRenderFace(world, x - 1, y, z)) {
                flags |= RIGHT_FACE;
                flags |= (world.isLit(x - 1, y, z) ? 0 : 1) * LAYER_RIGHT;
            }
            if(this.shouldRenderFace(world, x + 1, y, z)) {
                flags |= LEFT_FACE;
                flags |= (world.isLit(x + 1, y, z) ? 0 : 1) * LAYER_LEFT;
            }
        }

        return flags;
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

    protected boolean shouldRenderFace(World world, int x, int y, int z) {
        return !(world.isSolidTile(x, y, z) && world.isLightBlocker(x, y, z));
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
