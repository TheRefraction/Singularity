package net.singularity.entity;

import net.singularity.physics.AABB;
import net.singularity.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Math;

import java.util.List;

public class Entity {
    protected World world;
    public AABB aabb;
    protected Vector2f bbSize = new Vector2f(0.6f, 1.8f);
    private Model model;

    public Vector3f pos;
    public Vector3f oldPos;
    public Vector3f incPos;
    public Vector3f rot;
    public float scale = 1.0f;

    protected boolean onGround = false;
    public boolean removed = false;
    protected float heightOffset = 0f;

    public Entity(World world, Model model) {
        this.world = world;
        this.model = model;
        this.resetPos();
        this.rot = new Vector3f(0,0,0);
        this.oldPos = pos;
        this.incPos = new Vector3f(0,0,0);
    }

    public Entity(World world, Model model, Vector3f pos, Vector3f rot) {
        this.world = world;
        this.model = model;
        this.setPos(pos.x, pos.y, pos.z);
        this.rot = rot;
        this.oldPos = pos;
        this.incPos = new Vector3f(0,0,0);
    }

    public void resetPos() {
        float x = (float)Math.random() * this.world.width;
        float y = this.world.depth + 10;
        float z = (float)Math.random() * this.world.height;
        this.setPos(x, y, z);
    }

    public void update() {
        tick();
    }

    public void remove() {
        this.removed = true;
    }

    public void tick() {
        this.oldPos = this.pos;
    }

    protected void setSize(float width, float height) {
        this.bbSize = new Vector2f(width, height);
    }

    protected void setPos(float x, float y, float z) {
        this.pos = new Vector3f(x, y, z);
        float w = this.bbSize.x / 2f;
        float h = this.bbSize.y / 2f;
        this.aabb = new AABB(x - w, y - h, z - w, x + w, y + h, z + w);
    }

    public void move(float x, float y, float z) {
        float dx = x;
        float dy = y;
        float dz = z;
        List<AABB> aabbList = this.world.getCubes(this.aabb.expand(x, y, z));

        int i;
        for (i = 0; i < aabbList.size(); ++i) {
            y = aabbList.get(i).clipYCollide(this.aabb, y);
        }

        this.aabb.move(0f, y, 0f);

        for (i = 0; i < aabbList.size(); ++i) {
            x = aabbList.get(i).clipXCollide(this.aabb, x);
        }

        this.aabb.move(x, 0f, 0f);

        for (i = 0; i < aabbList.size(); ++i) {
            z = aabbList.get(i).clipZCollide(this.aabb, z);
        }

        this.aabb.move(0f, 0f, z);
        this.onGround = dy != y && dy < 0f;

        if(dx != x) this.incPos.x = 0f;
        if(dy != y) this.incPos.y = 0f;
        if(dz != z) this.incPos.z = 0f;

        this.pos = new Vector3f((this.aabb.x0 + this.aabb.x1) / 2f, this.aabb.y0 + this.heightOffset, (this.aabb.z0 + this.aabb.z1) / 2f);
    }

    public void moveRelative(float x, float z, float speed) {
        float dist = x * x + z * z;
        if(dist >= 0.01f) {
            dist = speed / Math.sqrt(dist);
            x *= dist;
            z *= dist;

            float sin = Math.sin(Math.toRadians(this.rot.y));
            float cos = Math.cos(Math.toRadians(this.rot.y));

            this.incPos.x += x * cos - z * sin;
            this.incPos.z += z * cos + x * sin;
        }
    }

    public float getDistanceFrom(Vector3f pos) {
        return this.pos.distance(pos);
    }

    public Model getModel() {
        return this.model;
    }
}
