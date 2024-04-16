package net.singularity.graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class BlockMesh {
    private static final int[] tileIndices = {
            0, 1, 3,
            3, 1, 2
    };

    public static final Mesh[] meshes = new Mesh[14];
    public static final BlockMesh blockBottom = new BlockMesh(0, new Mesh(
            new Vertex[] {
                    new Vertex(new Vector3f(0, 0, 1), new Vector2f(0, 1)),
                    new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 0)),
                    new Vertex(new Vector3f(1, 0, 0), new Vector2f(1, 0)),
                    new Vertex(new Vector3f(1, 0, 1), new Vector2f(1, 1)),
            }, tileIndices));

    public static final BlockMesh blockTop = new BlockMesh(1, new Mesh(
            new Vertex[] {
                    new Vertex(new Vector3f(1, 1, 1), new Vector2f(0, 1)),
                    new Vertex(new Vector3f(1, 1, 0), new Vector2f(0, 0)),
                    new Vertex(new Vector3f(0, 1, 0), new Vector2f(1, 0)),
                    new Vertex(new Vector3f(0, 1, 1), new Vector2f(1, 1)),
            }, tileIndices));

    public static final BlockMesh blockBack = new BlockMesh(2, new Mesh(
            new Vertex[] {
                    new Vertex(new Vector3f(0, 1, 0), new Vector2f(0, 1)),
                    new Vertex(new Vector3f(1, 1, 0), new Vector2f(0, 0)),
                    new Vertex(new Vector3f(1, 0, 0), new Vector2f(1, 0)),
                    new Vertex(new Vector3f(0, 0, 0), new Vector2f(1, 1)),
            }, tileIndices));

    public static final BlockMesh blockFront = new BlockMesh(3, new Mesh(
            new Vertex[] {
                    new Vertex(new Vector3f(0, 1, 1), new Vector2f(0, 1)),
                    new Vertex(new Vector3f(0, 0, 1), new Vector2f(0, 0)),
                    new Vertex(new Vector3f(1, 0, 1), new Vector2f(1, 0)),
                    new Vertex(new Vector3f(1, 1, 1), new Vector2f(1, 1)),
            }, tileIndices));

    public static final BlockMesh blockRight = new BlockMesh(4, new Mesh(
            new Vertex[] {
                    new Vertex(new Vector3f(0, 1, 1), new Vector2f(0, 1)),
                    new Vertex(new Vector3f(0, 1, 0), new Vector2f(0, 0)),
                    new Vertex(new Vector3f(0, 0, 0), new Vector2f(1, 0)),
                    new Vertex(new Vector3f(0, 0, 1), new Vector2f(1, 1)),
            }, tileIndices));

    public static final BlockMesh blockLeft = new BlockMesh(5, new Mesh(
            new Vertex[] {
                    new Vertex(new Vector3f(1, 0, 1), new Vector2f(0, 1)),
                    new Vertex(new Vector3f(1, 0, 0), new Vector2f(0, 0)),
                    new Vertex(new Vector3f(1, 1, 0), new Vector2f(1, 0)),
                    new Vertex(new Vector3f(1, 1, 1), new Vector2f(1, 1)),
            }, tileIndices));

    public static final BlockMesh blockBush1 = new BlockMesh(6, new Mesh(
            new Vertex[] {
                    new Vertex(new Vector3f(0, 1, 0), new Vector2f(0, 1)),
                    new Vertex(new Vector3f(1, 1, 1), new Vector2f(0, 0)),
                    new Vertex(new Vector3f(1, 0, 1), new Vector2f(1, 0)),
                    new Vertex(new Vector3f(0, 0, 0), new Vector2f(1, 1)),
            }, tileIndices));

    public static final BlockMesh blockBush2 = new BlockMesh(7, new Mesh(
            new Vertex[] {
                    new Vertex(new Vector3f(1, 1, 0), new Vector2f(0, 1)),
                    new Vertex(new Vector3f(0, 1, 1), new Vector2f(0, 0)),
                    new Vertex(new Vector3f(0, 0, 1), new Vector2f(1, 0)),
                    new Vertex(new Vector3f(1, 0, 0), new Vector2f(1, 1)),
            }, tileIndices));

    public final int id;

    protected BlockMesh(int id, Mesh mesh) {
        meshes[id] = mesh;
        this.id = id;
    }

}
