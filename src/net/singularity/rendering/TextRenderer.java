package net.singularity.rendering;

import net.singularity.graphics.Mesh;
import net.singularity.graphics.Shader;
import net.singularity.graphics.Vertex;
import net.singularity.text.Font;
import net.singularity.text.Glyph;
import net.singularity.text.Text;
import net.singularity.utils.Const;
import net.singularity.utils.Transformation;
import net.singularity.utils.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextRenderer {
    private final Renderer renderer;
    private final Shader shader;
    private final Mesh charMesh;
    private final List<Text> texts;

    private static int[] tileIndices = {
            0, 1, 3,
            3, 1, 2
    };

    public TextRenderer(Renderer renderer) throws Exception {
        this.renderer = renderer;
        this.shader = new Shader();
        this.texts = new ArrayList<>();
        this.charMesh = new Mesh(
                new Vertex[]{
                        new Vertex(new Vector3f(0, 1, 0), new Vector2f(0, 1)),
                        new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 0)),
                        new Vertex(new Vector3f(1, 0, 0), new Vector2f(1, 0)),
                        new Vertex(new Vector3f(1, 1, 0), new Vector2f(1, 1)),
                },
                tileIndices
        );
    }

    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResources("/shaders/textVertex.vsh"));
        shader.createFragmentShader(Utils.loadResources("/shaders/textFragment.fsh"));
        shader.link();
        shader.createUniform("textureSampler");
        shader.createUniform("color");
        shader.createUniform("orthoProjMatrix");
    }

    public void render(Font font) {
        shader.bind();

        //texts.add(new Text("Hello", new Vector2f(-0.95f,0.95f), new Vector4f(1,1,1,1), 0.05f));

        for(Text text : texts) {
            int i = 0;
            for(Character c : text.getText().toCharArray()) {
                Glyph ch = font.getGlyph(c);

                float u0 = Const.FONT_TEX_UV_STEP * ch.x;
                float v0 = Const.FONT_TEX_UV_STEP * ch.y;
                float u1 = u0 + Const.FONT_TEX_UV_STEP;
                float v1 = v0 + Const.FONT_TEX_UV_STEP;

                FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(4 * 2);
                float[] textureData = new float[]{
                        u0, v0,
                        u0, v1,
                        u1, v1,
                        u1, v0
                };
                textureBuffer.put(textureData).flip();

                charMesh.updateBufferObject(textureBuffer, charMesh.getTBO(), 2, 2);
                bind(charMesh);

                prepare(text, i);
                GL11.glDrawElements(GL11.GL_TRIANGLES, charMesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
                unbind();

                i++;
            }
        }
        texts.clear(); // TO SEE

        shader.unbind();
    }

    public void bind(Mesh mesh) {
        GL30.glBindVertexArray(mesh.getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderer.getTextures().loadTexture("/textures/font.png"));
    }

    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void prepare(Text text, int i) {
        shader.setUniform("textureSampler", 0);
        shader.setUniform("orthoProjMatrix", Transformation.getOrtoProjModelMatrix(text.getPosition().x + i * text.getSize(), text.getPosition().y, 0, text.getSize()));
        shader.setUniform("color", text.getColor());
    }

    public void destroy() {
        shader.destroy();
    }
}
