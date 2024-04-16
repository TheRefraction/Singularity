package net.singularity.system;

import net.singularity.physics.AABB;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;

public class Frustum {
    private final Matrix4f frustumMatrix;
    private final FrustumIntersection frustumInter;

    public Frustum() {
        this.frustumMatrix = new Matrix4f();
        this.frustumInter = new FrustumIntersection();
    }

    public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix) {
        frustumMatrix.set(projMatrix).mul(viewMatrix);
        frustumInter.set(frustumMatrix);
    }

    public boolean insideFrustum(AABB bb) {
        return frustumInter.testAab(bb.x0, bb.y0, bb.z0, bb.x1, bb.y1, bb.z1);
    }
}
