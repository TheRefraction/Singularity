package net.singularity.world;

import net.singularity.entity.Entity;
import net.singularity.entity.Player;
import net.singularity.system.Camera;
import net.singularity.system.ObjectLoader;
import net.singularity.system.rendering.RenderManager;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class World {
    private static int currentChunk = -1;
    private final ObjectLoader loader;

    private List<Chunk> chunks;
    private List<Entity> entities;

    private final Camera camera;
    private final Player player;

    public World(ObjectLoader loader, Camera camera) {
        this.loader = loader;
        this.camera = camera;
        this.player = new Player(this, new Vector3f(1,10,1), new Vector3f(0,0,0));
    }

    public void init() throws Exception {
        chunks = new ArrayList<>();
        entities = new ArrayList<>();
        //load if saved else gen
        Chunk chunk = new Chunk(0, this, new Vector2i(0,0));
        chunk.init();
        chunks.add(chunk);
        Chunk chunk2 = new Chunk(1, this, new Vector2i(3,0));
        chunk2.init();
        chunks.add(chunk2);
        currentChunk = 0;
    }

    public void update(float interval, RenderManager renderer) {
        player.update(interval);
        chunks.get(0).update(interval, renderer);
        chunks.get(1).update(interval, renderer);

        for(Entity entity : entities) {
            renderer.processEntity(entity);
        }
    }

    public void cleanup() {
        chunks.get(0).cleanup();
        chunks.get(1).cleanup();
        chunks.clear();
    }

    public ObjectLoader getLoader() {
        return loader;
    }

    public Camera getCamera() {
        return camera;
    }

    public int getCurrentChunkID() {
        return currentChunk;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }

    public Chunk getCurrentChunk() {
        return chunks.get(currentChunk);
    }

    public Player getPlayer() {
        return player;
    }
}
