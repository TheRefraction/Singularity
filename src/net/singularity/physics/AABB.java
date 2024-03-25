package net.singularity.physics;

import org.joml.Vector3f;

public class AABB {
    private Vector3f minVec, maxVec;
    private Vector3f minPos, maxPos;

    public AABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minVec = new Vector3f(minX, minY, minZ);
        this.maxVec = new Vector3f(maxX, maxY, maxZ);
        this.minPos = this.minVec;
        this.maxPos = this.maxVec;
    }

    public AABB(Vector3f minVec, Vector3f maxVec) {
        this.minVec = minVec;
        this.maxVec = maxVec;
        this.minPos = minVec;
        this.maxPos = maxVec;
    }

    public void updatePosition(Vector3f position) {
        this.minPos = new Vector3f(this.minVec).add(position);
        this.maxPos = new Vector3f(this.maxVec).add(position);
    }

    public boolean intersect(AABB other) {
        return this.minPos.x <= other.maxPos.x
            && this.maxPos.x >= other.minPos.x
            && this.minPos.y <= other.maxPos.y
            && this.maxPos.y >= other.minPos.y
            && this.minPos.z <= other.maxPos.z
            && this.maxPos.z >= other.minPos.z;
    }

    public Vector3f getMinVec() {
        return minVec;
    }

    public Vector3f getMaxVec() {
        return maxVec;
    }
}
