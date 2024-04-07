package net.singularity.entity;

import net.singularity.Main;
import net.singularity.system.MouseInput;
import net.singularity.system.Window;
import net.singularity.utils.Const;
import net.singularity.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Player extends Entity {
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
        this.oldPos = this.pos;
        float dx = 0f;
        float dz = 0f;

        Window window = Main.getWindow();

        if(window.isKeyPressed(GLFW.GLFW_KEY_R))
            this.resetPos();

        if(window.isKeyPressed(GLFW.GLFW_KEY_Z))
            --dz;
        else if(window.isKeyPressed(GLFW.GLFW_KEY_S))
            ++dz;

        if(window.isKeyPressed(GLFW.GLFW_KEY_Q))
            --dx;
        else if(window.isKeyPressed(GLFW.GLFW_KEY_D))
            ++dx;

        if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE) && this.onGround)
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
    }

    public void updateMouseInput(MouseInput mouseInput) {
        Vector2f rotVec = mouseInput.getDisplVec().mul(Const.MOUSE_SENSITIVITY);
        this.rot.x += rotVec.x;
        this.rot.y += rotVec.y;
        this.rot.x = Math.clamp(this.rot.x, -90f, 90f);
    }
}
