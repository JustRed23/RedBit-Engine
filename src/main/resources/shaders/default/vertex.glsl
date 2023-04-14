#version 450 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoord;
layout (location = 3) in float aTexId;

out vec4 fColor;
out vec2 fTexCoord;
out float fTexId;

uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

void main()
{
    gl_Position = uProjectionMatrix * uViewMatrix * vec4(aPos, 1.0);
    fColor = aColor;
    fTexCoord = aTexCoord;
    fTexId = aTexId;
}