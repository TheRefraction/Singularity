package net.singularity.system.rendering;

import net.singularity.Main;
import net.singularity.block.Block;
import net.singularity.entity.Model;
import net.singularity.system.Camera;
import net.singularity.system.Shader;
import net.singularity.utils.Transformation;
import net.singularity.utils.Utils;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
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
    private final Map<Vector2i, List<Vector3f>> blocks;

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
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
    }

    @Override
    public void render(Camera camera) {
        shader.bind();
        shader.setUniform("projectionMatrix", Main.getWindow().updateProjectionMatrix());

        for(Vector2i key : blocks.keySet()) {
            float[] textureCoords = Block.blocks[key.x].getFaceTexCoords(key.y);
            Model model = renderer.getLoader().updateModelTexCoords(renderer.getLoader().getBlockModels(key.y), textureCoords);
            bind(model);
            List<Vector3f> posList = blocks.get(key);
            for(Vector3f pos : posList) {
                prepare(new Vector4f(key.y, pos.x, pos.y, pos.z), camera);
                //GL11.glEnable(GL11.GL_BLEND);
                //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
                //GL11.glDisable(GL11.GL_BLEND);
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
        Vector4f vect = (Vector4f) o;
        shader.setUniform("textureSampler", 0);
        shader.setUniform("face", vect.x);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(new Vector3f(vect.y, vect.z, vect.w)));
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }

    public Map<Vector2i, List<Vector3f>> getBlocks() {
        return blocks;
    }
}
