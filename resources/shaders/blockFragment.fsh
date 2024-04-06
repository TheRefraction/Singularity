#version 400 core

in vec2 fragTextureCoords;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform float face;

void main() {
    fragColor = texture(textureSampler, fragTextureCoords);
    float light = 1.0f;
    if(face == 2 || face == 3 || face == 4 || face == 5) {
        light = 0.8f;
    } else if(face == 0) {
        light = 0.6f;
    }
    fragColor = light * fragColor;
}