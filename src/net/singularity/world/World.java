package net.singularity.world;

import net.singularity.entity.Entity;
import net.singularity.entity.Player;
import net.singularity.system.Camera;
import net.singularity.system.ObjectLoader;
import net.singularity.system.rendering.RenderManager;
import net.singularity.utils.Const;
import net.singularity.utils.Utils;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World {
    private static long currentChunk = -1;
    private final ObjectLoader loader;

    private HashMap<Long, Chunk> chunks;
    private List<Entity> entities;

    private final Camera camera;
    private final Player player;

    public World(ObjectLoader loader, Camera camera) {
        this.loader = loader;
        this.camera = camera;
        this.player = new Player(this, new Vector3f(1,10,1), new Vector3f(0,0,0));
    }

    public void init() throws Exception {
        chunks = new HashMap<Long, Chunk>();
        entities = new ArrayList<>();
        //load if saved else gen
        Chunk chunk;
        for(int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                long tokenId = Const.CHUNK_MAX_LINE * j + i;
                chunk = new Chunk(tokenId, this, new Vector2i(i, j));
                chunk.init();
                chunks.put(tokenId, chunk);
            }
        }

        currentChunk = 0;
    }

    public void update(float interval, RenderManager renderer) {
        player.update(interval);
        for(long i : chunks.keySet())
            chunks.get(i).update(interval, renderer);

        for(Entity entity : entities)
            renderer.processEntity(entity);

        long tmpX = (long) (player.getPos().x / Const.CHUNK_BASE_SIZE / 2 + ((player.getPos().x < 0) ? -1 : 0));
        long tmpZ = (long) (player.getPos().z / Const.CHUNK_BASE_SIZE / 2 + ((player.getPos().z < 0) ? -1 : 0));
        currentChunk = (Const.CHUNK_MAX_LINE * tmpZ + tmpX);
    }

    public void cleanup() {
        for(long i : chunks.keySet())
            chunks.get(i).cleanup();
        chunks.clear();
    }

    public ObjectLoader getLoader() {
        return loader;
    }

    public Camera getCamera() {
        return camera;
    }

    public long getCurrentChunkID() {
        return currentChunk;
    }

    public HashMap<Long, Chunk> getChunks() {
        return chunks;
    }

    public Chunk getCurrentChunk() {
        return chunks.get(currentChunk);
    }

    public Player getPlayer() {
        return player;
    }
}
