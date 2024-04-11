#version 400 core

in vec2 fragTextureCoords;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec4 color;

void main() {
    fragColor = texture(textureSampler, fragTextureCoords);
    if(fragColor.a < 0.1f) {
        discard;
    }
    fragColor = color * fragColor;
}