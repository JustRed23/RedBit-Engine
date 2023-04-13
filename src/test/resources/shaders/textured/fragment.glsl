#version 450 core

in vec2 TexCoord;

out vec4 FragColor;

uniform sampler2D uTextureSampler;

void main() {
    FragColor = texture(uTextureSampler, TexCoord);
}