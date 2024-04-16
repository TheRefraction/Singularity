package net.singularity.particle;

import net.singularity.entity.Entity;
import net.singularity.world.World;

public class Particle extends Entity {
    public int tex;
    private int age;
    private int lifeTime = 0;
    private float size;

    public Particle(World world, float x, float y, float z, int tex) {
        super(world, null);
        this.tex = tex;
        this.setPos(x, y, z);
        this.setSize(0.2f, 0.2f);
        this.heightOffset = this.bbSize.y / 2.0f;
        this.age = 0;
    }

    public void tick() {

    }
}
