package net.singularity.system;

import net.singularity.Main;
import org.lwjgl.opengl.GL11;

public class RenderingSystem {

    private final Window window;

    public RenderingSystem() {
        window = Main.getWindow();
    }

    public void init() throws Exception {

    }

    public void render() {

    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {

    }
}
