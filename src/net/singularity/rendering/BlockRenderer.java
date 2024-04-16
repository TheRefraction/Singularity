package net.singularity.rendering;

import net.singularity.block.Block;
import net.singularity.block.EBlockType;
import net.singularity.graphics.BlockMesh;
import net.singularity.graphics.Mesh;
import net.singularity.system.Camera;
import net.singularity.graphics.Shader;
import net.singularity.utils.Const;
import net.singularity.utils.Transformation;
import net.singularity.utils.Utils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class BlockRenderer {
    private static final int MAX_MEMORY = 4194304;

    private final Renderer renderer;
    private final Shader shader;
    private final List<Integer> data;
    private Mesh currentMesh = null;
    private final FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(4 * 2);

    public BlockRenderer(Renderer renderer) throws Exception {
        this.renderer = renderer;
        this.shader = new Shader();
        this.data = new ArrayList<>();
        initBuffer();
    }

    public void initBuffer() {
        for(int i = 0; i < MAX_MEMORY; i++) {
            this.data.add(0);
        }
    }

    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResources("/shaders/blockVertex.vsh"));
        shader.createFragmentShader(Utils.loadResources("/shaders/blockFragment.fsh"));
        shader.link();

        shader.createUniform("textureSampler");
        shader.createUniform("face");
        shader.createUniform("layer");
        shader.createUniform("outSelected");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
    }

    public void bind(Mesh mesh) {
        GL30.glBindVertexArray(mesh.getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderer.getTextures().loadTexture("/textures/terrain.png"));
    }

    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public void prepare(Camera camera, float x, float y, float z, int face, int layer) {
        boolean selected = camera.getHitResult() != null && camera.getHitResult().x == x && camera.getHitResult().y == y && camera.getHitResult().z == z;
        shader.setUniform("textureSampler", 0);
        shader.setUniform("face", face);
        shader.setUniform("layer", layer);
        shader.setUniform("outSelected", selected);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(x, y, z));
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    }

    public void render(Camera camera, byte[] blocks, int width, int height, int x0, int y0, int z0) {
        shader.bind();
        shader.setUniform("projectionMatrix", renderer.getWindow().getProjectionMatrix());

        for(int x = x0; x < x0 + Const.CHUNK_SIZE; x++) {
            for(int y = y0; y < y0 + Const.CHUNK_SIZE; y++) {
                for(int z = z0; z < z0 + Const.CHUNK_SIZE; z++) {
                    int index = (y * height + z) * width + x;

                    Block block = Block.blocks[blocks[index]];
                    if(block == null || blocks[index] == 0) {
                        continue;
                    }

                    EBlockType type = block.getBlockType();
                    int flags = data.get(index);

                    if(type == EBlockType.NORMAL) {
                        GL11.glEnable(GL11.GL_CULL_FACE);

                        if((Block.BOTTOM_FACE & flags) != 0 && camera.getPos().y < y) {
                            renderFace(camera, block, x, y, z, 0, (Block.LAYER_BOTTOM & flags) != 0 ? 1 : 0);
                        }

                        if((Block.TOP_FACE & flags) != 0 && camera.getPos().y > y + 1) {
                            renderFace(camera, block, x, y, z, 1, (Block.LAYER_TOP & flags) != 0 ? 1 : 0);
                        }

                        if((Block.BACK_FACE & flags) != 0 && camera.getPos().z < z) {
                            renderFace(camera, block, x, y, z, 2, (Block.LAYER_BACK & flags) != 0 ? 1 : 0);
                        }

                        if((Block.FRONT_FACE & flags) != 0 && camera.getPos().z > z + 1) {
                            renderFace(camera, block, x, y, z, 3, (Block.LAYER_FRONT & flags) != 0 ? 1 : 0);
                        }

                        if((Block.RIGHT_FACE & flags) != 0 && camera.getPos().x < x) {
                            renderFace(camera, block, x, y, z, 4, (Block.LAYER_RIGHT & flags) != 0 ? 1 : 0);
                        }

                        if((Block.LEFT_FACE & flags) != 0 && camera.getPos().x > x + 1) {
                            renderFace(camera, block, x, y, z, 5, (Block.LAYER_LEFT & flags) != 0 ? 1 : 0);
                        }
                    } else if(type == EBlockType.BUSH) {
                        GL11.glDisable(GL11.GL_CULL_FACE);

                        if(flags != 0) {
                            renderFace(camera, block, x, y, z, 6, flags == 2 ? 1 : 0);
                            renderFace(camera, block, x, y, z, 7, flags == 2 ? 1 : 0);
                        }
                    }
                }
            }
        }
        unbind();
        shader.unbind();
    }

    private void renderFace(Camera camera, Block block, int x, int y, int z, int face, int layer) {
        float[] textureData;
        if(face < 6) {
            textureData = block.getFaceTexCoords(face);
        } else textureData = block.getFaceTexCoords(2);
        textureBuffer.put(textureData).flip();

        currentMesh = BlockMesh.meshes[face];
        currentMesh.updateBufferObject(textureBuffer, currentMesh.getTBO(), 2, 2);

        bind(currentMesh);
        prepare(camera, x, y, z, face, layer);
        GL11.glDrawElements(GL11.GL_TRIANGLES, currentMesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
    }

    public void destroy() {
        textureBuffer.clear();
        shader.destroy();
    }

    public List<Integer> getData() {
        return data;
    }
}
