package dev.JustRed23.redbit.engine.render;

import dev.JustRed23.redbit.engine.err.UniformException;
import dev.JustRed23.redbit.engine.obj.components.SpriteRenderer;
import dev.JustRed23.redbit.engine.window.View;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class BatchRenderer {

    /* COMMON */
    private final int POSITION_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXTURE_COORDS_SIZE = 2;
    private final int TEXTURE_ID_SIZE = 1;

    private final int POSITION_OFFSET = 0;
    private final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;
    private final int TEXTURE_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEXTURE_ID_OFFSET = TEXTURE_COORDS_OFFSET + TEXTURE_COORDS_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = POSITION_SIZE + COLOR_SIZE + TEXTURE_COORDS_SIZE + TEXTURE_ID_SIZE;
    private final int VERTEX_BYTES = VERTEX_SIZE * Float.BYTES;

    private final List<Texture> textures = new ArrayList<>(8);
    private final int[] textureSlots = new int[] {0, 1, 2, 3, 4, 5, 6, 7};
    /* COMMON */

    private final ShaderProgram shader;
    private final View view;
    private final int maxSprites;

    private SpriteRenderer[] renderers;
    private int totalSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoId, vboId;

    public BatchRenderer(View view, int maxSize, ShaderProgram shader) {
        this.maxSprites = maxSize;
        this.shader = shader;
        this.view = view;
        this.renderers = new SpriteRenderer[maxSize];
        this.vertices = new float[maxSize * 4 * VERTEX_SIZE];
        this.totalSprites = 0;
        this.hasRoom = true;
    }

    public void start() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboid = glGenBuffers();
        int[] indices = genIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboid);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_BYTES, POSITION_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEXTURE_COORDS_SIZE, GL_FLOAT, false, VERTEX_BYTES, TEXTURE_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_BYTES, TEXTURE_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteRenderer renderer) {
        int index = this.totalSprites;
        renderers[index] = renderer;
        this.totalSprites++;

        if (renderer.getTexture() != null)
            if (!textures.contains(renderer.getTexture()))
                textures.add(renderer.getTexture());

        loadVertexData(index);

        if (this.totalSprites >= this.maxSprites)
            this.hasRoom = false;
    }

    public void render() throws UniformException {
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.bind();
        shader.set("uProjectionMatrix", view.getCamera().getProjectionMatrix());
        shader.set("uViewMatrix", view.getCamera().getViewMatrix());

        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            glBindTexture(GL_TEXTURE_2D, textures.get(i).id());
        }

        shader.set("uTextures", textureSlots);

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, totalSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        glBindVertexArray(0);

        shader.unbind();
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);

        shader.cleanup();
    }

    private void loadVertexData(int index) {
        SpriteRenderer renderer = this.renderers[index];

        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = renderer.getColor();

        int texId = 0;
        if (renderer.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i).equals(renderer.getTexture())) {
                    texId = i + 1;
                    break;
                }
            }
        }

        float xAdd = 1f, yAdd = 1f;
        for (int i = 0; i < 4; i++) {
            if (i == 1)
                yAdd = -0.0f;
            else if (i == 2)
                xAdd = -0.0f;
            else if (i == 3)
                yAdd = 1.0f;

            Vector4f currentPos = new Vector4f(
                    renderer.parent.transform.position.x + (xAdd * renderer.parent.transform.scale.x),
                    renderer.parent.transform.position.y + (yAdd * renderer.parent.transform.scale.y),
                    0, 1);

            vertices[offset] = currentPos.x;
            vertices[offset + 1] = currentPos.y;

            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            vertices[offset + 6] = renderer.getTextureCoords()[i].x;
            vertices[offset + 7] = renderer.getTextureCoords()[i].y;

            vertices[offset + 8] = texId;

            offset += VERTEX_SIZE;
        }
    }

    private int @NotNull [] genIndices() {
        int[] indices = new int[maxSprites * 6];
        for (int i = 0; i < maxSprites; i++)
            loadElementIndices(indices, i);
        return indices;
    }

    private void loadElementIndices(int @NotNull [] elements, int index) {
        int indexOffset = index * 6;
        int offset = index * 4;

        //triangle 1
        elements[indexOffset] = offset + 3;
        elements[indexOffset + 1] = offset + 2;
        elements[indexOffset + 2] = offset;

        //triangle 2
        elements[indexOffset + 3] = offset;
        elements[indexOffset + 4] = offset + 2;
        elements[indexOffset + 5] = offset + 1;
    }

    public boolean hasRoom() {
        return hasRoom;
    }

    public boolean hasTextureRoom() {
        return textures.size() < 8;
    }

    public boolean hasTexture(Texture texture) {
        return textures.contains(texture);
    }
}
