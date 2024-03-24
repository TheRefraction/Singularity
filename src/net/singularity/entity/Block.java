package net.singularity.entity;

import net.singularity.physics.AABB;
import net.singularity.system.ObjectLoader;
import net.singularity.utils.Const;
import net.singularity.world.Chunk;
import org.joml.Vector3f;

public class Block {
    private int blockId;
    private int localId;
    private Chunk chunk;
    private Vector3f pos;

    private Model[] models;
    private String[] textures;
    private boolean[] renderFaces = {true, true, true, true, true, true}; // TOP, BOTTOM, FRONT, BACK, LEFT, RIGHT

    private boolean opaque;
    private boolean hasUpdated;

    private int[] indices = {
            0, 1, 3,
            3, 1, 2
    };

    private float[] textureCoords = {
            0, 0,
            0, 1,
            1, 1,
            1, 0
    };

    private AABB aabb;

    public Block(int blockId, int localId, Chunk chunk, Vector3f pos) throws Exception {
        this.blockId = blockId;
        this.localId = localId;
        this.chunk = chunk;
        this.pos = pos;
        init_texture();
        init_model();
    }

    private void init_texture() {
        textures = new String[] {
                "resources/textures/grass.png",
                "resources/textures/dirt.png",
                "resources/textures/grass_side.png",
                "resources/textures/grass_side.png",
                "resources/textures/grass_side.png",
                "resources/textures/grass_side.png"};
        opaque = true;
    }

    public void init_model() throws Exception {
        float[] vertices;
        models = new Model[6];

        ObjectLoader loader = chunk.getWorld().getLoader();

        //TOP
        vertices = new float[]{-1f, 1f, 1f,
                -1f, 1f, -1f,
                1f, 1f, -1f,
                1f, 1f, 1f};
        models[0] = loader.loadModel(vertices, textureCoords, indices);
        models[0].setTexture(new Texture(loader.loadTexture(textures[0])));

        //BOTTOM
        vertices = new float[]{1f, -1f, 1f,
                1f, -1f, -1f,
                -1f, -1f, -1f,
                -1f, -1f, 1f};
        models[1] = loader.loadModel(vertices, textureCoords, indices);
        models[1].setTexture(new Texture(loader.loadTexture(textures[1])));

        //FRONT
        vertices = new float[]{1f,  1f, 1f,
                1f, -1f, 1f,
                -1f, -1f, 1f,
                -1f,  1f, 1f,};
        models[2] = loader.loadModel(vertices, textureCoords, indices);
        models[2].setTexture(new Texture(loader.loadTexture(textures[2])));

        //BACK
        vertices = new float[]{-1f,  1f, -1f,
                -1f, -1f, -1f,
                1f, -1f, -1f,
                1f,  1f, -1f,};
        models[3] = loader.loadModel(vertices, textureCoords, indices);
        models[3].setTexture(new Texture(loader.loadTexture(textures[3])));

        //LEFT
        vertices = new float[]{-1f,  1f, 1f,
                -1f, -1f, 1f,
                -1f, -1f, -1f,
                -1f,  1f, -1f,};
        models[4] = loader.loadModel(vertices, textureCoords, indices);
        models[4].setTexture(new Texture(loader.loadTexture(textures[4])));

        //RIGHT
        vertices = new float[]{1f,  1f, -1f,
                1f, -1f, -1f,
                1f, -1f, 1f,
                1f,  1f, 1f,};
        models[5] = loader.loadModel(vertices, textureCoords, indices);
        models[5].setTexture(new Texture(loader.loadTexture(textures[5])));

        hasUpdated = false;
    }

    public void update(float interval) {
        if(!hasUpdated) {
            updateRenderingFaces();
            hasUpdated = true;
        }
    }

    public int[] getNeighborBlocks() {
        int[] neighborBlocks = new int[6];

        if(localId / (Const.CHUNK_BASE_SIZE * Const.CHUNK_BASE_SIZE) == Const.CHUNK_HEIGHT - 1)
            neighborBlocks[0] = -1;
        else neighborBlocks[0] = localId + Const.CHUNK_BASE_SIZE * Const.CHUNK_BASE_SIZE;

        if(localId / (Const.CHUNK_BASE_SIZE * Const.CHUNK_BASE_SIZE) == 0)
            neighborBlocks[1] = -1;
        else neighborBlocks[1] = localId - Const.CHUNK_BASE_SIZE * Const.CHUNK_BASE_SIZE;

        if(localId % (Const.CHUNK_BASE_SIZE * Const.CHUNK_BASE_SIZE) >= (Const.CHUNK_BASE_SIZE * (Const.CHUNK_BASE_SIZE - 1)) && localId % (Const.CHUNK_BASE_SIZE * Const.CHUNK_BASE_SIZE) < Const.CHUNK_BASE_SIZE * Const.CHUNK_BASE_SIZE)
            neighborBlocks[2] = -1;
        else neighborBlocks[2] = localId + Const.CHUNK_BASE_SIZE;

        if(localId % (Const.CHUNK_BASE_SIZE * Const.CHUNK_BASE_SIZE) < Const.CHUNK_BASE_SIZE)
            neighborBlocks[3] = -1;
        else neighborBlocks[3] = localId - Const.CHUNK_BASE_SIZE;

        if(localId % Const.CHUNK_BASE_SIZE == 0)
            neighborBlocks[4] = -1;
        else neighborBlocks[4] = localId - 1;

        if(localId % Const.CHUNK_BASE_SIZE == Const.CHUNK_BASE_SIZE - 1)
            neighborBlocks[5] = -1;
        else neighborBlocks[5] = localId + 1;

        return neighborBlocks;
    }

    public void updateRenderingFaces() {
        int[] neighborBlocks = getNeighborBlocks();
        //System.out.println(localId);
        //System.out.println("===");
        for(int i = 0; i < 6 ; i++) {
            //System.out.println(neighborBlocks[i]);
            if(neighborBlocks[i] == -1) {
                renderFaces[i] = true;
            } else renderFaces[i] = !chunk.getBlocks().get(neighborBlocks[i]).isOpaque();;
        }
        //System.out.println("-------------");
    }

    public void setPos(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public Vector3f getPos() {
        return pos;
    }

    public int getBlockId() {
        return blockId;
    }

    public int getLocalId() {
        return localId;
    }

    public boolean isOpaque() {
        return opaque;
    }

    public boolean[] getRenderFaces() {
        return renderFaces;
    }

    public Model[] getModels() {
        return models;
    }
}
