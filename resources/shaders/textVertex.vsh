#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 fragTextureCoords;

uniform mat4 orthoProjMatrix;

void main() {
    gl_Position = orthoProjMatrix * vec4(position, 1.0);
    fragTextureCoords = textureCoords;
}