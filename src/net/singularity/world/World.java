package net.singularity.world;

import net.singularity.entity.Entity;
import net.singularity.system.Camera;
import net.singularity.system.ObjectLoader;
import net.singularity.system.rendering.RenderManager;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class World {
    private static int currentChunk = -1;
    private final ObjectLoader loader;

    private List<Chunk> chunks;
    private List<Entity> entities;

    private Camera camera;

    public World(ObjectLoader loader, Camera camera) {
        this.loader = loader;
        this.camera = camera;
    }

    public void init() throws Exception {
        chunks = new ArrayList<>();
        entities = new ArrayList<>();
        //load if saved else gen
        Chunk chunk = new Chunk(0, this, new Vector2i(0,0));
        chunk.init();
        chunks.add(chunk);
        currentChunk = 0;
    }

    public void update(float interval, RenderManager renderer) {
        chunks.get(0).update(interval, renderer);

        for(Entity entity : entities) {
            renderer.processEntity(entity);
        }
    }

    public void cleanup() {
        chunks.get(0).cleanup();
        chunks.clear();
    }

    public ObjectLoader getLoader() {
        return loader;
    }

    public Camera getCamera() {
        return camera;
    }

    public int getCurrentChunk() {
        return currentChunk;
    }
}
