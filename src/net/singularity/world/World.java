package net.singularity.world;

import net.singularity.block.Block;
import net.singularity.physics.AABB;
import net.singularity.entity.Player;
import net.singularity.system.Camera;
import net.singularity.rendering.Renderer;
import net.singularity.utils.Const;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class World {
    private final Camera camera;
    private final Player player;
    private final Random random = new Random();
    public byte[] blocks;
    private int[] lightDepths;
    int unprocessed = 0;
    public int width;
    public int depth;
    public int height;
    public int size;

    public World(Camera camera, int width, int depth, int height) {
        this.camera = camera;
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.size = width * depth * height;

        this.blocks = new byte[this.size];
        this.lightDepths = new int[width * height];
        boolean mapLoaded = this.load();
        if(!mapLoaded) {
            this.blocks = (new WorldGen(width, height, depth)).generateMap();
        }

        this.player = new Player(this);

        this.calcLightDepths(0,0,width, height);
    }

    public void init() {
    }

    public void update(Renderer renderer) {
        player.update();

        tick();
    }

    public void tick() {
        this.unprocessed += this.size;
        int ticks = this.unprocessed / Const.BLOCK_UPDATE_INTERVAL;
        this.unprocessed -= ticks * Const.BLOCK_UPDATE_INTERVAL;

        for(int i = 0; i < ticks; ++i) {
            int x = this.random.nextInt(this.width);
            int y = this.random.nextInt(this.depth);
            int z = this.random.nextInt(this.height);
            Block block = Block.blocks[this.getTile(x, y, z)];
            if (block != null) {
                block.tick(this, x, y, z, this.random);
            }
        }
    }

    public boolean load() {
        try {
            DataInputStream dis = new DataInputStream(new GZIPInputStream(new FileInputStream("level.dat")));
            dis.readFully(this.blocks);
            this.calcLightDepths(0, 0, this.width, this.height);

            dis.close();
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public void save() {
        try {
            DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(new FileOutputStream("level.dat")));
            dos.write(this.blocks);
            dos.close();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void calcLightDepths(int x0, int y0, int x1, int y1) {
        for(int x = x0; x < x0 + x1; ++x) {
            for(int z = y0; z < y0 + y1; ++z) {
                //int oldDepth = this.lightDepths[x + z * this.width];

                int y = this.depth - 1;
                while (y > 0 && !this.isLightBlocker(x, y, z)) {
                    --y;
                }

                this.lightDepths[x + z * this.width] = y;
            }
        }

    }

    public boolean isLightBlocker(int x, int y, int z) {
        Block block = Block.blocks[this.getTile(x, y, z)];
        return block != null && block.blocksLight();
    }

    public boolean setTile(int x, int y, int z, int type) {
        if (x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height) {
            if (type == this.blocks[(y * this.height + z) * this.width + x]) {
                return false;
            } else {
                this.blocks[(y * this.height + z) * this.width + x] = (byte)type;
                this.calcLightDepths(x, z, 1, 1);

                return true;
            }
        } else {
            return false;
        }
    }

    public boolean isLit(int x, int y, int z) {
        if (x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height) {
            return y >= this.lightDepths[x + z * this.width];
        } else {
            return true;
        }
    }

    public int getTile(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < this.width && y < this.depth && z < this.height ? this.blocks[(y * this.height + z) * this.width + x] : 0;
    }

    public ArrayList<AABB> getCubes(AABB aABB) {
        ArrayList<AABB> aabbList = new ArrayList<>();
        int x0 = (int)aABB.x0;
        int x1 = (int)(aABB.x1 + 1.0f);
        int y0 = (int)aABB.y0;
        int y1 = (int)(aABB.y1 + 1.0f);
        int z0 = (int)aABB.z0;
        int z1 = (int)(aABB.z1 + 1.0f);

        if (x0 < 0) x0 = 0;
        if (y0 < 0) y0 = 0;
        if (z0 < 0) z0 = 0;

        if (x1 > this.width) x1 = this.width;
        if (y1 > this.depth) y1 = this.depth;
        if (z1 > this.height) z1 = this.height;

        for(int x = x0; x < x1; ++x) {
            for(int y = y0; y < y1; ++y) {
                for(int z = z0; z < z1; ++z) {
                    Block block = Block.blocks[this.getTile(x, y, z)];
                    if (block != null) {
                        AABB aabb = block.getAABB(x, y, z);
                        if (block.isSolid() && aabb != null) {
                            aabbList.add(aabb);
                        }
                    }
                }
            }
        }

        return aabbList;
    }

    public boolean isSolidTile(int x, int y, int z) {
        Block block = Block.blocks[this.getTile(x, y, z)];
        return block != null && block.isSolid();
    }

    public Camera getCamera() {
        return camera;
    }

    public Player getPlayer() {
        return this.player;
    }
}
