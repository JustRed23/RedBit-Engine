#version 420 core

in vec3 position;
in vec2 texcoord;

out vec2 pass_texcoord;

uniform mat4 projectionMatrix;

void main() {
    gl_Position = 1 * vec4(position, 1.0);
    pass_texcoord = texcoord;
}