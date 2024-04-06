package net.singularity.system;

import org.joml.Vector3f;

public class Camera {

    private Vector3f pos, rot;
    //private FrustumCullingFilter frustumFilter;

    public Camera() {
        this.pos = new Vector3f(0, 0, 0);
        this.rot = new Vector3f(0, 0, 0);
        //this.frustumFilter = new FrustumCullingFilter();
    }

    public void setPosition(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public void setRotation(float x, float y, float z) {
        this.rot.x = x;
        this.rot.y = y;
        this.rot.z = z;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRot() {
        return rot;
    }

    /*public FrustumCullingFilter getFrustumFilter() {
        return frustumFilter;
    }*/
}
