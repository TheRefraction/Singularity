package net.singularity.world;

import net.singularity.block.Block;
import org.joml.Math;

import java.util.Random;

public class WorldGen {
    private final int width;
    private final int height;
    private final int depth;
    private final Random random = new Random();

    public WorldGen(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public byte[] generateMap() {
        int w = this.width;
        int h = this.height;
        int d = this.depth;
        int[] heightmap1 = (new NoiseMap(0)).read(w, h);
        int[] heightmap2 = (new NoiseMap(0)).read(w, h);
        int[] cf = (new NoiseMap(1)).read(w, h);
        int[] rockMap = (new NoiseMap(1)).read(w, h);
        byte[] blocks = new byte[this.width * this.height * this.depth];

        int x;
        int y;
        int length;
        for(x = 0; x < w; ++x) {
            for(y = 0; y < d; ++y) {
                for(int z = 0; z < h; ++z) {
                    int dh1 = heightmap1[x + z * this.width];
                    int dh2 = heightmap2[x + z * this.width];
                    length = cf[x + z * this.width];
                    if(length < 128) {
                        dh2 = dh1;
                    }

                    int dh = Math.max(dh1, dh2);

                    dh = dh / 8 + d / 3;
                    int rh = rockMap[x + z * this.width] / 8 + d / 3;
                    if(rh > dh - 2) {
                        rh = dh - 2;
                    }

                    int i = (y * this.height + z) * this.width + x;
                    int id = 0;

                    if(y == dh) {
                        id = Block.grass.id;
                    }

                    if(y < dh) {
                        id = Block.dirt.id;
                    }

                    if(y <= rh) {
                        id = Block.rock.id;
                    }

                    if(y == 0) {
                        id = Block.bedrock.id;
                    }

                    blocks[i] = (byte)id;
                }
            }
        }

        x = w * h * d / 256 / 64;

        for(y = 0; y < x; ++y) {
            float x1 = this.random.nextFloat() * w;
            float y1 = this.random.nextFloat() * d;
            float z = this.random.nextFloat() * h;
            length = (int)(this.random.nextFloat() + this.random.nextFloat() * 150.0f);

            float dir1 = this.random.nextFloat() * 3.14159f * 2.0f;
            float dira1 = 0.0f;

            float dir2 = this.random.nextFloat() * 3.14159f * 2.0f;
            float dira2 = 0.0f;

            for(int l = 0; l < length; ++l) {
                x1 += Math.sin(dir1) * Math.cos(dir2);
                z += Math.cos(dir1) * Math.cos(dir2);
                y1 += Math.sin(dir2);

                dir1 += dira1 * 0.2f;
                dira1 *= 0.9f;
                dira1 += this.random.nextFloat() - this.random.nextFloat();

                dir2 += dira2 * 0.5f;
                dir2 *= 0.5f;
                dira2 *= 0.9f;
                dira2 += this.random.nextFloat() - this.random.nextFloat();

                float size = Math.sin(l * 3.14159f / length) * 2.5f + 1.0f;

                for(int xx = (int)(x1 - size); xx <= (int)(x1 + size); ++xx) {
                    for(int yy = (int)(y1 - size); yy <= (int)(y1 + size); ++yy) {
                        for(int zz = (int)(z - size); zz <= (int)(z + size); ++zz) {
                            float xd = xx - x1;
                            float yd = yy - y1;
                            float zd = zz - z;

                            float dd = xd * xd + yd * yd * 2.0f + zd * zd;
                            if (dd < size * size && xx >= 1 && yy >= 1 && zz >= 1 && xx < this.width - 1 && yy < this.depth - 1 && zz < this.height - 1) {
                                int ii = (yy * this.height + zz) * this.width + xx;
                                if (blocks[ii] == Block.rock.id) {
                                    blocks[ii] = 0;
                                }
                            }
                        }
                    }
                }
            }
        }

        return blocks;
    }
}
