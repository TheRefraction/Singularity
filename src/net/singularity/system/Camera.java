package net.singularity.system;

import org.joml.Vector3f;

public class Camera {

    private Vector3f position, rotation;
    private FrustumCullingFilter frustumFilter;

    public Camera() {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.frustumFilter = new FrustumCullingFilter();
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public FrustumCullingFilter getFrustumFilter() {
        return frustumFilter;
    }
}
