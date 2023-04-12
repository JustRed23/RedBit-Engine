package dev.JustRed23.redbit.shader;

import dev.JustRed23.redbit.mesh.Mesh;
import dev.JustRed23.redbit.mesh.TexturedMesh;
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

    public ShaderProgram() {
        this.programId = glCreateProgram();
        if (programId == 0)
            throw new RuntimeException("Could not create Shader");
        super.setProgramId(programId);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void createShader(@NotNull String vertex, @NotNull String fragment) throws IOException {
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

    public void render(Mesh mesh, RenderCallback render) {
        bind();
        for (int i = 0; i < mesh.attribCount(); i++)
            glEnableVertexAttribArray(i);
        render.render(mesh);
        for (int i = 0; i < mesh.attribCount(); i++)
            glDisableVertexAttribArray(i);
        unbind();
    }

    public void render(TexturedMesh texturedMesh, RenderCallback render) {
        bind();
        for (int i = 0; i < texturedMesh.attribCount(); i++)
            glEnableVertexAttribArray(i);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texturedMesh.textureID());
        render.render(texturedMesh);
        for (int i = 0; i < texturedMesh.attribCount(); i++)
            glDisableVertexAttribArray(i);
        unbind();
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
