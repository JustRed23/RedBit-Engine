package dev.JustRed23.redbit.shader;

import dev.JustRed23.redbit.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram extends ShaderUniformity {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShaderProgram.class);

    public static String VERTEX_SHADER_LOC = null;
    public static String FRAGMENT_SHADER_LOC = null;

    private Shader vertexShader, fragmentShader;

    private final int programId;

    public ShaderProgram() {
        this.programId = glCreateProgram();
        if (programId == 0)
            throw new RuntimeException("Could not create Shader");
        super.setProgramId(programId);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void createShader() throws IOException {
        String vertexShaderSource = FileUtils.readFile(VERTEX_SHADER_LOC);
        String fragmentShaderSource = FileUtils.readFile(FRAGMENT_SHADER_LOC);

        Shader vertexShader = new Shader(vertexShaderSource, GL_VERTEX_SHADER);
        Shader fragmentShader = new Shader(fragmentShaderSource, GL_FRAGMENT_SHADER);

        vertexShader.compile();
        fragmentShader.compile();

        glAttachShader(programId, vertexShader.getShaderId());
        glAttachShader(programId, fragmentShader.getShaderId());

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
            LOGGER.error(glGetProgramInfoLog(programId, 1024));

        validate();

        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();

        if (vertexShader != null)
            vertexShader.cleanup(programId);
        if (fragmentShader != null)
            fragmentShader.cleanup(programId);

        if (programId != 0)
            glDeleteProgram(programId);
    }

    public void validate() {
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)
            LOGGER.error(glGetProgramInfoLog(programId, 1024));
    }

    public void setVertexShaderLoc(String loc) {
        VERTEX_SHADER_LOC = loc;
    }

    public void setFragmentShaderLoc(String loc) {
        FRAGMENT_SHADER_LOC = loc;
    }

    public int getProgramId() {
        return programId;
    }
}
