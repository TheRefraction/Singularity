package net.singularity.entity;

import net.singularity.block.Block;
import net.singularity.physics.AABB;
import net.singularity.system.Input;
import net.singularity.utils.HitResult;
import net.singularity.world.World;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Player extends Entity {
    private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;
    private int leftButtonBuffer = 0, rightButtonBuffer = 0;

    public Player(World world) {
        super(world, null);
        this.heightOffset = 1.6f;
    }

    public void update() {
        tick();

        world.getCamera().update(pos.x, pos.y, pos.z, rot.x, rot.y, rot.z);
    }

    public void tick() {
        newMouseX = Input.getMouseX();
        newMouseY = Input.getMouseY();

        this.oldPos = this.pos;
        float dx = 0f;
        float dz = 0f;

        if(leftButtonBuffer > 0) {
            leftButtonBuffer -= 1;
        }

        if(rightButtonBuffer > 0) {
            rightButtonBuffer -= 1;
        }

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

        if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) && leftButtonBuffer <= 0) {
            HitResult hitResult = world.getCamera().getHitResult();
            if(hitResult != null) {
                Block old = Block.blocks[world.getTile(hitResult.x, hitResult.y, hitResult.z)];
                boolean changed = world.setTile(hitResult.x, hitResult.y, hitResult.z, 0);
                if(old != null && changed) {
                    old.destroy(); // to change
                    leftButtonBuffer = 8;
                }
            }
        }

        if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT) && rightButtonBuffer <= 0) {
            HitResult hitResult = world.getCamera().getHitResult();
            if(hitResult != null) {
                int x = hitResult.x;
                int y = hitResult.y;
                int z = hitResult.z;

                switch(hitResult.f) {
                    case 0:
                        y--;
                        break;
                    case 1:
                        y++;
                        break;
                    case 2:
                        z--;
                        break;
                    case 3:
                        z++;
                        break;
                    case 4:
                        x--;
                        break;
                    case 5:
                        x++;
                        break;
                }

                AABB aabb = Block.blocks[Block.rose.id].getAABB(x, y, z);
                if(aabb == null || world.isFree(aabb)) {
                    world.setTile(x, y, z, Block.cobblestone.id);
                    rightButtonBuffer = 8;
                }
            }
        }

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
