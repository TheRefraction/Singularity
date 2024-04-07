package net.singularity.rendering;

import net.singularity.Main;
import net.singularity.block.Block;
import net.singularity.block.EBlockType;
import net.singularity.entity.Model;
import net.singularity.system.Camera;
import net.singularity.system.Shader;
import net.singularity.utils.Transformation;
import net.singularity.utils.Utils;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockRenderer implements IRenderer{
    private final RenderManager renderer;
    private final Shader shader;
    private final Map<Vector3i, List<Vector3f>> blocks;

    public BlockRenderer(RenderManager renderer) throws Exception {
        this.renderer = renderer;
        shader = new Shader();
        blocks = new HashMap<>();
    }

    @Override
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

    @Override
    public void render(Camera camera) {
        shader.bind();
        shader.setUniform("projectionMatrix", Main.getWindow().updateProjectionMatrix());

        for(Vector3i key : blocks.keySet()) {
            Block block = Block.blocks[key.x];
            int face = key.y;
            EBlockType blockType = block.getBlockType();
            float[] textureCoords = block.getFaceTexCoords(face);

            Model model = renderer.getLoader().updateModelTexCoords(renderer.getLoader().getBlockModels(blockType, face), textureCoords);
            bind(model);
            List<Vector3f> posList = blocks.get(key);

            for(Vector3f pos : posList) {
                prepare(camera, pos.x, pos.y, pos.z, face, key.z);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbind();
        }
        blocks.clear();

        shader.unbind();
    }

    @Override
    public void bind(Model model) {
        GL30.glBindVertexArray((model.getId()));
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderer.getLoader().getBlockTexture().getId());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void prepare(Object o, Camera camera) {
    }

    public void prepare(Camera camera, float x, float y, float z, int face, int layer) {
        shader.setUniform("textureSampler", 0);
        shader.setUniform("face", face);
        shader.setUniform("layer", layer);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(x, y, z));
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }

    public Map<Vector3i, List<Vector3f>> getBlocks() {
        return blocks;
    }
}
