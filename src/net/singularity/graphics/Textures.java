package net.singularity.graphics;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Textures {
    private HashMap<String, Integer> idMap = new HashMap<>();

    public int loadTexture(String filename) {
        try {
            if (this.idMap.containsKey(filename)) {
                return this.idMap.get(filename);
            } else {
                PNGDecoder decoder = new PNGDecoder(Textures.class.getResourceAsStream(filename));
                ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

                decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
                buffer.flip();

                /*for(int i=0; i<buffer.capacity(); i++) {
                    System.out.println(buffer.get(i));
                }*/

                int id = GL11.glGenTextures();
                this.idMap.put(filename, id);

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
                GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

                GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

                return id;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load texture: " + filename);
        }
    }
}
