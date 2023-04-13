package dev.JustRed23.redbit.engine.render;

import dev.JustRed23.redbit.engine.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram extends ShaderUniformity {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShaderProgram.class);

    private Shader vertexShader, fragmentShader;

    private final int programId;
    private boolean isBound;

    public ShaderProgram(String vertexPath, String fragmentPath) throws IOException {
        this.programId = glCreateProgram();
        if (programId == 0)
            throw new RuntimeException("Could not create Shader");
        super.setProgramId(programId);
        createShader(vertexPath, fragmentPath);
    }

    public void bind() {
        if (isBound)
            return;

        glUseProgram(programId);
        isBound = true;
    }

    private void createShader(@NotNull String vertex, @NotNull String fragment) throws IOException {
        if (vertex.isBlank() || fragment.isBlank())
            throw new RuntimeException("Shader locations not set");

        String vertexShaderSource = FileUtils.readFile(vertex);
        String fragmentShaderSource = FileUtils.readFile(fragment);

        Shader vertexShader = new Shader(vertexShaderSource, GL_VERTEX_SHADER);
        Shader fragmentShader = new Shader(fragmentShaderSource, GL_FRAGMENT_SHADER);

        vertexShader.compile();
        fragmentShader.compile();

        glAttachShader(programId, vertexShader.getShaderId());
        glAttachShader(programId, fragmentShader.getShaderId());

        link();
        validate();

        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public void unbind() {
        glUseProgram(0);
        isBound = false;
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

    private void link() {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
            LOGGER.error(glGetProgramInfoLog(programId, 1024));
    }

    private void validate() {
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)
            LOGGER.error(glGetProgramInfoLog(programId, 1024));
    }

    public int getProgramId() {
        return programId;
    }
}
