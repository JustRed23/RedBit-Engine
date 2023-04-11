#version 420 core

in vec2 pass_texcoord;

out vec4 FragColor;

uniform sampler2D texSampler;

void main() {
    FragColor = texture(texSampler, pass_texcoord);
}