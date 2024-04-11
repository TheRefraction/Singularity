package net.singularity.text;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Text {
    private String text;
    private Vector2f position;
    private Vector4f color;
    private float size;

    public Text(String text, Vector2f position, Vector4f color, float size) {
        this.text = text;
        this.position = position;
        this.color = color;
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector4f getColor() {
        return color;
    }

    public float getSize() {
        return size;
    }
}
