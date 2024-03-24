package net.singularity.utils;

import net.singularity.entity.Entity;
import net.singularity.system.Camera;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    public static Matrix4f createTransformationMatrix(Entity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(entity.getPos())
                .rotateX(Math.toRadians(entity.getRotation().x))
                .rotateY(Math.toRadians(entity.getRotation().y))
                .rotateZ(Math.toRadians(entity.getRotation().z)).scale(entity.getScale());

        return matrix;
    }

    public static Matrix4f getViewMatrix(Camera camera) {
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();

        matrix.identity()
                .rotate(Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate(Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate(Math.toRadians(rot.z), new Vector3f(0, 0, 1))
                .translate(-pos.x, -pos.y, -pos.z);

        return matrix;
    }
}
