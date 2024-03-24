package net.singularity.entity;

import net.singularity.system.ObjectLoader;
import org.joml.Vector3f;

public class Block {
    private int id;
    private int chunk_id;
    private Vector3f pos;
    private ObjectLoader loader;

    private Model[] models;
    private String[] textures;
    private boolean[] renderFaces = {true, true, true, true, true, true}; // TOP, BOTTOM, FRONT, BACK, LEFT, RIGHT

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

    public Block(int id, int chunk_id, ObjectLoader loader, Vector3f pos) throws Exception {
        this.id = id;
        this.chunk_id = chunk_id;
        this.loader = loader;
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
    }

    public void init_model() throws Exception {
        float[] vertices;
        models = new Model[6];

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
    }

    public void setPos(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public Vector3f getPos() {
        return pos;
    }

    public int getId() {
        return id;
    }

    public boolean[] getRenderFaces() {
        return renderFaces;
    }

    public Model[] getModels() {
        return models;
    }
}
