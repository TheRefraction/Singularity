package net.singularity.system;

import net.singularity.physics.AABB;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;

public class FrustumCullingFilter {
    private final Matrix4f frustumMatrix;
    private FrustumIntersection frustumInter;

    public FrustumCullingFilter() {
        this.frustumMatrix = new Matrix4f();
        this.frustumInter = new FrustumIntersection();
    }

    public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix) {
        frustumMatrix.set(projMatrix).mul(viewMatrix);
        frustumInter.set(frustumMatrix);
    }

    public boolean insideFrustum(AABB boundingBox) {
        return true;//frustumInter.testAab(boundingBox.getMinPos(), boundingBox.getMaxPos());
    }
}
