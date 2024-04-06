package net.singularity.system;

import net.singularity.entity.Model;
import net.singularity.entity.Texture;
import net.singularity.utils.SException;
import net.singularity.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    private Texture blockTexture;
    private Model[] blockModels;

    public Model loadModel(float[] vertices, float[] textureCoords, int[] indices) {
        int id = createVAO();
        storeIndicesBuffer(indices);
        storeDataInAttribList(0, 3, vertices);
        storeDataInAttribList(1, 2, textureCoords);
        unbind();
        return new Model(id, indices.length);
    }

    public Model updateModelTexCoords(Model model, float[] textureCoords) {
        changeDataInAttribList(3 * model.getId(), 1, 2, textureCoords);
        unbind();
        return new Model(model.getId(), model.getVertexCount());
    }

    public int loadTexture(String filename) throws Exception {
        int width, height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);
            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if(buffer == null)
                SException.raiseException(new Exception("Couldn't load " + filename + "\nINFO : " + STBImage.stbi_failure_reason()));

            width = w.get();
            height = h.get();
        }

        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);

        return id;
    }

    public void initBlockModels() {
        blockModels = new Model[6];
        float[] vertices;
        float[] textureCoords = {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        int[] tileIndices = {
                0, 1, 3,
                3, 1, 2
        };

        vertices = new float[] {
                0, 0, 1,
                0, 0, 0,
                1, 0, 0,
                1, 0, 1
        };

        blockModels[0] = this.loadModel(vertices, textureCoords, tileIndices);

        vertices = new float[] {
                1, 1, 1,
                1, 1, 0,
                0, 1, 0,
                0, 1, 1
        };

        blockModels[1] = this.loadModel(vertices, textureCoords, tileIndices);

        vertices = new float[] {
                0, 1, 0,
                1, 1, 0,
                1, 0, 0,
                0, 0, 0
        };

        blockModels[2] = this.loadModel(vertices, textureCoords, tileIndices);

        vertices = new float[] {
                0, 1, 1,
                0, 0, 1,
                1, 0, 1,
                1, 1, 1
        };

        blockModels[3] = this.loadModel(vertices, textureCoords, tileIndices);

        vertices = new float[] {
                0, 1, 1,
                0, 1, 0,
                0, 0, 0,
                0, 0, 1
        };

        blockModels[4] = this.loadModel(vertices, textureCoords, tileIndices);

        vertices = new float[] {
                1, 0, 1,
                1, 0, 0,
                1, 1, 0,
                1, 1, 1
        };

        blockModels[5] = this.loadModel(vertices, textureCoords, tileIndices);
    }

    public Model getBlockModels(int face) {
        return this.blockModels[face];
    }

    public void loadBlockTexture() throws Exception {
        blockTexture = new Texture(loadTexture("resources/textures/terrain.png"));
    }

    public Texture getBlockTexture() {
        return this.blockTexture;
    }

    private int createVAO() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttribList(int attribNo, int vertexCount, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        changeDataInAttribList(vbo, attribNo, vertexCount, data);
    }

    private void changeDataInAttribList(int vbo, int attribNo, int vertexCount, float[] data) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribNo, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        for(int vao : vaos)
            GL30.glDeleteVertexArrays(vao);
        for(int vbo : vbos)
            GL30.glDeleteBuffers(vbo);
        for(int texture : textures)
            GL30.glDeleteTextures(texture);
    }
}
