package dev.JustRed23.redbit.mesh;

import java.io.FileNotFoundException;
import java.util.List;

public final class TexturedMesh extends Mesh {

    private final int textureID;

    public TexturedMesh(int vaoID, List<Integer> vbos, int vertexCount, int attribCount, String texturePath) throws FileNotFoundException {
        super(vaoID, vbos, vertexCount, attribCount);
        this.textureID = MeshTexture.load(texturePath);
    }

    public int textureID() {
        return textureID;
    }
}
