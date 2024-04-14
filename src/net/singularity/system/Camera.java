package net.singularity.system;

import org.joml.RayAabIntersection;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Camera {

    private Vector3f pos, rot, dir;
    private FrustumCullingFilter frustumFilter;
    private RayAabIntersection rayCast;
    private Vector3i selectedBlock;

    public Camera() {
        this.pos = new Vector3f(0, 0, 0);
        this.rot = new Vector3f(0, 0, 0);
        this.dir = new Vector3f(0,0,1);
        this.frustumFilter = new FrustumCullingFilter();
        this.rayCast = new RayAabIntersection();
        this.selectedBlock = new Vector3i(-1, -1, -1);
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

    public void update(float x, float y, float z, float r_x, float r_y, float r_z) {
        setPosition(x, y, z);
        setRotation(r_x, r_y, r_z);

        dir = new Vector3f(0,0,1).rotateX((float) Math.toRadians(r_x)).rotateY((float) Math.toRadians(r_y)).rotateZ((float) Math.toRadians(r_z));

        rayCast.set(x, y, z, dir.x, dir.y, -dir.z);
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRot() {
        return rot;
    }

    public FrustumCullingFilter getFrustumFilter() {
        return frustumFilter;
    }

    public RayAabIntersection getRayCast() {
        return rayCast;
    }

    public Vector3i getSelectedBlock() {
        return selectedBlock;
    }
}
