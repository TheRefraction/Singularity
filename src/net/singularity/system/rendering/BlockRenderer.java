package net.singularity.system.rendering;

import net.singularity.entity.Block;
import net.singularity.Main;
import net.singularity.entity.Model;
import net.singularity.system.Camera;
import net.singularity.system.Shader;
import net.singularity.utils.Transformation;
import net.singularity.utils.Utils;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class BlockRenderer implements IRenderer {

    private final Shader shader;
    private final List<Block> blocks;

    public BlockRenderer() throws Exception {
        blocks = new ArrayList<>();
        shader = new Shader();
    }

    @Override
    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResources("/shaders/vertex.vsh"));
        shader.createFragmentShader(Utils.loadResources("/shaders/fragment.fsh"));
        shader.link();
        shader.createUniform("textureSampler");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
    }

    @Override
    public void render(Camera camera) {
        shader.bind();
        shader.setUniform("projectionMatrix", Main.getWindow().updateProjectionMatrix());

        for(Block block : blocks) {
            for(int i = 0; i < 6; i++) {
                Model model = block.getModels()[i];
                if(!block.getRenderFaces()[i])
                    continue;
                bind(model);
                prepare(block, camera);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
                unbind();
            }
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
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void prepare(Object bl, Camera camera) {
        Block block = (Block) bl;
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(block.getPos());
        shader.setUniform("textureSampler", 0);
        shader.setUniform("transformationMatrix", matrix);
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
