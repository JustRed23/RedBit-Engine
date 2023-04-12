package dev.JustRed23.redbit.engine.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL20.*;

class Shader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Shader.class);

    private final int shaderId;
    private final int type;
    private final String source;

    public Shader(String source, int type) {
        this.source = source;
        this.type = type;
        this.shaderId = glCreateShader(type);
        if (shaderId == 0)
            throw new RuntimeException("Could not create Shader");
    }

    public void compile() {
        glShaderSource(shaderId, source);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
            LOGGER.error(glGetShaderInfoLog(shaderId, 1024));
    }

    public void cleanup(int programId) {
        glDetachShader(programId, shaderId);
        glDeleteShader(shaderId);
    }

    public int getShaderId() {
        return shaderId;
    }

    public int getType() {
        return type;
    }

    public String getSource() {
        return source;
    }
}
