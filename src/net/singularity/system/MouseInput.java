package net.singularity.system;

import net.singularity.Main;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseInput {

    private final Vector2d currentPos;
    private final Vector2f displVec;

    private boolean inWindow = false, leftButtonPress = false, rightButtonPress = false, mouseLocked = false;

    public MouseInput() {
        this.currentPos = new Vector2d(0, 0);
        this.displVec = new Vector2f();
    }

    public void init() {
        GLFW.glfwSetCursorPosCallback(Main.getWindow().getWindowHandle(), (window, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });

        GLFW.glfwSetCursorEnterCallback(Main.getWindow().getWindowHandle(), (window, entered) -> {
            inWindow = entered;
        });

        GLFW.glfwSetMouseButtonCallback(Main.getWindow().getWindowHandle(), (window, button, action, mods) -> {
            leftButtonPress = (button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS);
            rightButtonPress = (button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS);
        });
    }

    public void input() {
        if(!mouseLocked) {
            if(leftButtonPress) {
                GLFW.glfwSetInputMode(Main.getWindow().getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                mouseLocked = true;
            } else GLFW.glfwSetInputMode(Main.getWindow().getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        } else {
            displVec.x = 0;
            displVec.y = 0;

            double x = currentPos.x - Main.getWindow().getWidthCenter();
            double y = currentPos.y - Main.getWindow().getHeightCenter();
            boolean rotateX = x != 0;
            boolean rotateY = y != 0;

            if (rotateX)
                displVec.y = (float) x;
            if (rotateY)
                displVec.x = (float) y;

            //GLFW.glfwSetCursorPos(Main.getWindow().getWindowHandle(), Main.getWindow().getWidthCenter(), Main.getWindow().getHeightCenter());
        }
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public boolean isLeftButtonPress() {
        return leftButtonPress;
    }

    public boolean isRightButtonPress() {
        return rightButtonPress;
    }

    public boolean isInWindow() {
        return inWindow;
    }
}
