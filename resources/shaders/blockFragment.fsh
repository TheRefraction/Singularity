#version 400 core

in vec2 fragTextureCoords;
in vec4 outPosition;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform int face;
uniform int layer;
uniform float outSelected;

void main() {
    vec4 color = texture(textureSampler, fragTextureCoords);
    if(color.a < 0.1) {
        discard;
    }

    float light = 1.0;
    if(face == 2 || face == 3 || face == 4 || face == 5) {
        light = 0.8;
    } else if(face == 0) {
        light = 0.6;
    }
    
    if(layer == 1) {
        light = 0.6;
    }

    color = light * color;

    if(outSelected > 0) {
        float size = 0.01;
        if((outPosition.x <= size || outPosition.x >= 1 - size) && (outPosition.z <= size || outPosition.z >= 1 - size)
        || (outPosition.y <= size || outPosition.y >= 1 - size) && (outPosition.z <= size || outPosition.z >= 1 - size)
        || (outPosition.y <= size || outPosition.y >= 1 - size) && (outPosition.x <= size || outPosition.x >= 1 - size)) {
            color = vec4(1, 1, 1, 1);
        }
    }

    fragColor = color;
}