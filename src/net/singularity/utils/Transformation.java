package net.singularity.utils;

import net.singularity.entity.Entity;
import net.singularity.system.Camera;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    public static Matrix4f createTransformationMatrix(Entity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(entity.pos)
                .rotateX(Math.toRadians(entity.rot.x))
                .rotateY(Math.toRadians(entity.rot.y))
                .rotateZ(Math.toRadians(entity.rot.z))
                .scale(entity.scale);

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(float x, float y, float z) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(x, y, z);

        return matrix;
    }

    public static Matrix4f getViewMatrix(Camera camera) {
        Vector3f pos = camera.getPos();
        Vector3f rot = camera.getRot();
        Matrix4f matrix = new Matrix4f();

        matrix.identity()
                .rotate(Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate(Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate(Math.toRadians(rot.z), new Vector3f(0, 0, 1))
                .translate(-pos.x, -pos.y, -pos.z);

        return matrix;
    }
}
