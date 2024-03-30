#version 400 core

in vec2 fragTextureCoords;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform float outSelected;

void main() {
    fragColor = texture(textureSampler, fragTextureCoords);
    if(outSelected > 0) {
        fragColor = vec4(fragColor.xy, 1, 1);
    }
}