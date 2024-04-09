package net.singularity.entity;

import net.singularity.system.Input;
import net.singularity.world.World;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Player extends Entity {
    private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;

    public Player(World world) {
        super(world, null);
        this.heightOffset = 1.6f;
    }

    public void update() {
        tick();

        world.getCamera().setPosition(this.pos.x, this.pos.y, this.pos.z);
        world.getCamera().setRotation(this.rot.x, this.rot.y, this.rot.z);
    }

    public void tick() {
        newMouseX = Input.getMouseX();
        newMouseY = Input.getMouseY();

        this.oldPos = this.pos;
        float dx = 0f;
        float dz = 0f;

        if(Input.isKeyDown(GLFW.GLFW_KEY_R))
            this.resetPos();

        if(Input.isKeyDown(GLFW.GLFW_KEY_Z))
            --dz;
        else if(Input.isKeyDown(GLFW.GLFW_KEY_S))
            ++dz;

        if(Input.isKeyDown(GLFW.GLFW_KEY_Q))
            --dx;
        else if(Input.isKeyDown(GLFW.GLFW_KEY_D))
            ++dx;

        if(Input.isKeyDown(GLFW.GLFW_KEY_SPACE) && this.onGround)
            this.incPos.y = 0.4f;

        this.moveRelative(dx, dz, this.onGround ? 0.05f : 0.02f);
        this.incPos.y -= 0.04f;
        this.move(this.incPos.x, this.incPos.y, this.incPos.z);
        this.incPos.x *= 0.91F;
        this.incPos.y *= 0.98F;
        this.incPos.z *= 0.91F;
        if(this.onGround) {
            this.incPos.x *= 0.7F;
            this.incPos.z *= 0.7F;
        }

        float dmx = (float) (newMouseX - oldMouseX);
        float dmy = (float) (newMouseY - oldMouseY);

        this.rot.add(new Vector3f(dmy * 0.15f, dmx * 0.15f, 0));
        this.rot.x = Math.clamp(this.rot.x, -90f, 90f);

        oldMouseX = newMouseX;
        oldMouseY = newMouseY;
    }
}
