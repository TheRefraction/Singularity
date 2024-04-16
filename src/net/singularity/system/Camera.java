package net.singularity.system;

import net.singularity.physics.AABB;
import net.singularity.utils.HitResult;
import net.singularity.utils.Transformation;
import net.singularity.world.World;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Camera {

    private final Vector3f pos, rot, dir;
    private final Frustum frustumFilter;
    private HitResult hitResult = null;
    private static final int PICK_RANGE = 4;

    public Camera() {
        this.pos = new Vector3f(0, 0, 0);
        this.rot = new Vector3f(0, 0, 0);
        this.dir = new Vector3f(0, 0, 0);
        this.frustumFilter = new Frustum();
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
    }

    public void pick(World world) {
        hitResult = null;
        Vector2f nearFar = new Vector2f(0,0);
        Transformation.getViewMatrix(this).positiveZ(dir).negate();
        float closestDistance = Float.POSITIVE_INFINITY;

        ArrayList<AABB> aabbs = world.getCubes(new AABB(pos.x - PICK_RANGE, pos.y - PICK_RANGE, pos.z - PICK_RANGE, pos.x + PICK_RANGE, pos.y + PICK_RANGE, pos.z + PICK_RANGE), true);
        for(AABB aabb : aabbs) {
            if(Intersectionf.intersectRayAab(pos, dir, new Vector3f(aabb.x0, aabb.y0, aabb.z0), new Vector3f(aabb.x1, aabb.y1, aabb.z1), nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;

                float xI = pos.x + closestDistance * dir.x;
                float yI = pos.y + closestDistance * dir.y;
                float zI = pos.z + closestDistance * dir.z;

                int face = -1;
                if(xI == aabb.x0) {
                    face = 4;
                } else if(xI == aabb.x1) {
                    face = 5;
                } else if(yI == aabb.y0) {
                    face = 0;
                } else if(yI == aabb.y1) {
                    face = 1;
                } else if(zI == aabb.z0) {
                    face = 2;
                } else if(zI == aabb.z1) {
                    face = 3;
                }

                if(face != -1) {
                    hitResult = new HitResult((int) aabb.x0, (int) aabb.y0, (int) aabb.z0, face);
                }
            }
        }
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRot() {
        return rot;
    }

    public Frustum getFrustumFilter() {
        return frustumFilter;
    }

    public HitResult getHitResult() {
        return hitResult;
    }
}
