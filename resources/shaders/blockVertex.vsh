#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 fragTextureCoords;
out vec4 outPosition;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    outPosition = vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * outPosition;
    fragTextureCoords = textureCoords;
}