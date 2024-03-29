package net.singularity.system;

public interface ILogic {

    void init() throws Exception;

    void input(MouseInput mouseInput);

    void update(float interval);

    void render();

    void cleanup();
}
