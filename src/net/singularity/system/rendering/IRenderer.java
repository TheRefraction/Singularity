package net.singularity.system.rendering;

import net.singularity.entity.Model;
import net.singularity.system.Camera;

public interface IRenderer<T> {

    public void init() throws Exception;

    public void render(Camera camera);

    abstract void bind(Model model);

    public void unbind();

    public void prepare(T t, Camera camera);

    public void cleanup();
}
