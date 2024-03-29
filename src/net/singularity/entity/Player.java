package net.singularity.entity;

import net.singularity.Main;
import net.singularity.physics.AABB;
import net.singularity.system.MouseInput;
import net.singularity.system.Window;
import net.singularity.utils.Const;
import net.singularity.utils.Utils;
import net.singularity.world.World;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.clamp;

public class Player {
    private Vector3f pos, rotation;
    private final World world;
    private boolean noClip;
    private boolean onGround;
    private float eyeHeight;
    private float health;
    private float speed;
    private float jumpHeight;
    private float ySpeed;

    private final Vector3f posInc;

    private AABB boundingBox;

    private final List<Block> colList;

    public Player(World world, Vector3f pos, Vector3f rotation) {
        this.pos = pos;
        this.rotation = rotation;
        this.world = world;
        this.noClip = false;
        this.onGround = true;
        this.eyeHeight = 1.5f;
        this.health = 20.0f;
        this.speed = 0.1f;
        this.jumpHeight = 0.2f;
        this.ySpeed = 0;
        this.posInc = new Vector3f(0,0,0);
        this.colList = new ArrayList<>();
        init_AABB();
    }

    public void init_AABB() {
        this.boundingBox = new AABB(new Vector3f(-1, -1, -1), new Vector3f(1,1,1));
    }

    public void update(float interval) {
        for(Block block : world.getCurrentChunk().getBlocks()) {
            if(getDistanceFrom(block.getPos()) <= 3.0f)
                colList.add(block);
        }

        if(onGround || noClip) {
            if(ySpeed < 0)
                ySpeed = 0;
        } else
            ySpeed -= 0.008f;

        if(!noClip) {
            this.boundingBox.updatePosition(new Vector3f(pos.x, pos.y - Const.COLLISION_GRANULARITY, pos.z));
            onGround = isColliding();
            this.boundingBox.updatePosition(pos);

            posInc.y = ySpeed;
        }

        if(pos.y <= -32f) {
            onGround = true;
            pos.y = -32f;
        }

        movePosition(posInc.x, posInc.y, posInc.z);
        world.getCamera().setPosition(pos.x, pos.y + this.eyeHeight, pos.z);
        world.getCamera().setRotation(rotation.x, rotation.y, 0);

        colList.clear();
    }

    public void updateInput(MouseInput mouseInput) {
        Window window = Main.getWindow();
        if(window.isKeyPressed(GLFW.GLFW_KEY_F1))
            noClip=!noClip;

        posInc.set(0, 0, 0);

        if(window.isKeyPressed(GLFW.GLFW_KEY_Z))
            posInc.z = -1;
        else if(window.isKeyPressed(GLFW.GLFW_KEY_S))
            posInc.z = 1;

        if(window.isKeyPressed(GLFW.GLFW_KEY_Q))
            posInc.x = -1;
        else if(window.isKeyPressed(GLFW.GLFW_KEY_D))
            posInc.x = 1;

        if(noClip) {
            if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE))
                posInc.y = 1;
            else if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
                posInc.y = -1;
        } else if(onGround) {
            if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE))
                ySpeed = jumpHeight;
            else if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
                eyeHeight = 1.0f;
            else eyeHeight = 1.25f;
        }

        posInc.mul(speed);

        if(mouseInput.isLeftButtonPress()) {
            Vector2f rotVec = mouseInput.getDisplVec().mul(Const.MOUSE_SENSITIVITY);
            moveRotation(rotVec.x, rotVec.y, 0);
        }
    }

    public boolean isColliding() {
        for(Block block : colList) {
            if(this.boundingBox.intersect(block.getBoundingBox()))
                return true;
        }
        return false;
    }

    public void movePosition(float x, float y, float z) {
        float dx = 0;
        float dy = getDy(y);
        float dz = 0;

        if(z != 0) {
            dx = getDx(Math.sin(Math.toRadians(rotation.y)) * -1.0f * z);
            dz = getDz(Math.cos(Math.toRadians(rotation.y)) * z);
        }

        if(x != 0) {
            dx = getDx(Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x);
            dz = getDz(Math.cos(Math.toRadians(rotation.y - 90)) * x);
        }

        pos.add(new Vector3f(dx, dy, dz));
        this.boundingBox.updatePosition(pos);
    }

    private float getDx(float dx) {
        if(!noClip) {
            this.boundingBox.updatePosition(new Vector3f(pos.x + dx, pos.y, pos.z));
            if(isColliding()) {
                for(float i = 0; i < abs(dx); i += Const.COLLISION_GRANULARITY) {
                    this.boundingBox.updatePosition(new Vector3f(pos.x + i * Utils.sign(dx), pos.y, pos.z));
                    if(isColliding()) {
                        dx = (i - Const.COLLISION_GRANULARITY) * Utils.sign(dx);
                        break;
                    }
                }
            }
        }
        return dx;
    }

    private float getDy(float dy) {
        if(!noClip) {
            this.boundingBox.updatePosition(new Vector3f(pos.x, pos.y + dy, pos.z));
            if(isColliding()) {
                for(float i = 0; i < abs(dy); i += Const.COLLISION_GRANULARITY) {
                    this.boundingBox.updatePosition(new Vector3f(pos.x, pos.y + i * Utils.sign(dy), pos.z));
                    if(isColliding()) {
                        dy = (i - Const.COLLISION_GRANULARITY) * Utils.sign(dy);
                        break;
                    }
                }
            }
        }
        return dy;
    }

    private float getDz(float dz) {
        if(!noClip) {
            this.boundingBox.updatePosition(new Vector3f(pos.x, pos.y, pos.z + dz));
            if(isColliding()) {
                for(float i = 0; i < abs(dz); i += Const.COLLISION_GRANULARITY) {
                    this.boundingBox.updatePosition(new Vector3f(pos.x, pos.y, pos.z + i * Utils.sign(dz)));
                    if(isColliding()) {
                        dz = (i - Const.COLLISION_GRANULARITY) * Utils.sign(dz);
                        break;
                    }
                }
            }
        }
        return dz;
    }

    public void setPosition(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public void moveRotation(float x, float y, float z) {
        this.rotation.x = clamp(this.rotation.x + x, -90f, 90f);
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public AABB getBoundingBox() {
        return boundingBox;
    }

    public float getDistanceFrom(Vector3f position) {
        return position.distance(this.pos);
    }
}
