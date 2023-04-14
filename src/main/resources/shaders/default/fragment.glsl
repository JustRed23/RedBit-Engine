#version 450 core

in vec4 fColor;
in vec2 fTexCoord;
in float fTexId;

out vec4 color;

uniform sampler2D uTextures[8];

void main() {
    if (fTexId > 0) {
        color = fColor * texture(uTextures[int(fTexId)], fTexCoord);
    } else {
        color = fColor;
    }
}