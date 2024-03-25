package net.singularity.system;

import net.singularity.physics.AABB;
import org.joml.Math;
import org.joml.Vector3f;

public class Camera {

    private Vector3f position, rotation;
    private AABB boundingBox;

    public Camera() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        init_AABB();
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        init_AABB();
    }

    public void init_AABB() {
        this.boundingBox = new AABB(new Vector3f(-1, -2, -1), new Vector3f(1,1,1));
    }

    public void movePosition(float x, float y, float z) {
        if(z != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if(x != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }
        position.y += y;
        this.boundingBox.updatePosition(position);
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

    public void moveRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public AABB getBoundingBox() {
        return boundingBox;
    }

    public float getDistanceFrom(Vector3f position) {
        return position.distance(this.position);
    }
}
