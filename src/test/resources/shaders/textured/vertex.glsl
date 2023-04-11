#version 420 core

in vec3 position;
in vec2 texcoord;

out vec2 pass_texcoord;

void main() {
    gl_Position = vec4(position, 1.0);
    pass_texcoord = texcoord;
}