package net.singularity.system.rendering;

import net.singularity.entity.Model;
import net.singularity.system.Camera;

public interface IRenderer<T> {

    void init() throws Exception;

    void render(Camera camera);

    void bind(Model model); //abstract

    void unbind();

    void prepare(T t, Camera camera);

    void cleanup();
}
