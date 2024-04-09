package net.singularity.rendering;

import net.singularity.block.Block;
import net.singularity.block.EBlockType;
import net.singularity.graphics.BlockMesh;
import net.singularity.graphics.Mesh;
import net.singularity.system.Camera;
import net.singularity.graphics.Shader;
import net.singularity.utils.Transformation;
import net.singularity.utils.Utils;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockRenderer {
    private final Renderer renderer;
    private final Shader shader;
    private final Map<Vector3i, List<Vector3f>> blocks;

    public BlockRenderer(Renderer renderer) throws Exception {
        this.renderer = renderer;
        this.shader = new Shader();
        this.blocks = new HashMap<>();
    }

    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResources("/shaders/blockVertex.vsh"));
        shader.createFragmentShader(Utils.loadResources("/shaders/blockFragment.fsh"));
        shader.link();
        shader.createUniform("textureSampler");
        shader.createUniform("face");
        shader.createUniform("layer");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
    }

    public void render(Camera camera) {
        shader.bind();
        shader.setUniform("projectionMatrix", renderer.getWindow().getProjectionMatrix());

        for(Vector3i key : blocks.keySet()) {
            Block block = Block.blocks[key.x];
            int face = key.y;
            EBlockType blockType = block.getBlockType();

            FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(4 * 2);
            float[] textureData = block.getFaceTexCoords(face);
            textureBuffer.put(textureData).flip();

            Mesh mesh = BlockMesh.meshes[face];
            mesh.updateBufferObject(textureBuffer, mesh.getTBO(), 2, 2);
            bind(mesh);
            List<Vector3f> posList = blocks.get(key);

            for(Vector3f pos : posList) {
                prepare(camera, pos.x, pos.y, pos.z, face, key.z);
                GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
            }
            unbind();
        }
        blocks.clear();

        shader.unbind();
    }

    public void bind(Mesh mesh) {
        GL30.glBindVertexArray(mesh.getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderer.getTextures().loadTexture("/textures/terrain.png"));
    }

    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void prepare(Camera camera, float x, float y, float z, int face, int layer) {
        shader.setUniform("textureSampler", 0);
        shader.setUniform("face", face);
        shader.setUniform("layer", layer);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(x, y, z));
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    }

    public void destroy() {
        shader.destroy();
    }

    public Map<Vector3i, List<Vector3f>> getBlocks() {
        return blocks;
    }
}
