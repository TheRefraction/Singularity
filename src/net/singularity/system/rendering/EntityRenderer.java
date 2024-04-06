package net.singularity.system.rendering;

import net.singularity.Main;
import net.singularity.entity.Entity;
import net.singularity.entity.Model;
import net.singularity.system.Camera;
import net.singularity.system.Shader;
import net.singularity.utils.Transformation;
import net.singularity.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRenderer implements IRenderer {

    private final RenderManager renderer;
    private final Shader shader;
    private final Map<Model, List<Entity>> entities;

    public EntityRenderer(RenderManager renderer) throws Exception {
        this.renderer = renderer;
        entities = new HashMap<>();
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

        for(Model model : entities.keySet()) {
            bind(model);
            List<Entity> entityList = entities.get(model);
            for(Entity entity : entityList) {
                prepare(entity, camera);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbind();
        }
        entities.clear();
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
    public void prepare(Object entity, Camera camera) {
        shader.setUniform("textureSampler", 0);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix((Entity) entity));
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }

    public Map<Model, List<Entity>> getEntities() {
        return entities;
    }
}
