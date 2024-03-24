package net.singularity.physics;

import org.joml.Vector3f;

public class AABB {
    private Vector3f minVec, maxVec;

    public AABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minVec = new Vector3f(minX, minY, minZ);
        this.maxVec = new Vector3f(maxX, maxY, maxZ);
    }

    public AABB(Vector3f minVec, Vector3f maxVec) {
        this.minVec = minVec;
        this.maxVec = maxVec;
    }

    public boolean intersect(AABB other) {
        return this.minVec.x <= other.maxVec.x
                && this.maxVec.x >= other.minVec.x
                && this.minVec.y <= other.maxVec.y
                && this.maxVec.y >= other.minVec.y
                && this.minVec.z <= other.maxVec.z
                && this.maxVec.z >= other.minVec.z;
    }

    public float getMinX() {
        return minVec.x;
    }

    public float getMinY() {
        return minVec.y;
    }

    public float getMinZ() {
        return minVec.z;
    }

    public float getMaxX() {
        return maxVec.x;
    }

    public float getMaxY() {
        return maxVec.y;
    }

    public float getMaxZ() {
        return maxVec.z;
    }
}
