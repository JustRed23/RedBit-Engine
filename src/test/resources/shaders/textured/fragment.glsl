#version 420 core

in vec2 pass_texcoord;

out vec4 FragColor;

uniform sampler2D tex;

void main() {
    FragColor = texture(tex, pass_texcoord);
}