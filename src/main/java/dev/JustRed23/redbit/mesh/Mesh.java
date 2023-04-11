package dev.JustRed23.redbit.mesh;

import java.util.List;

public class Mesh {
    private final int vaoID;
    private final List<Integer> vbos;
    private final int vertexCount;
    private final int attribCount;

    public Mesh(int vaoID, List<Integer> vbos, int vertexCount, int attribCount) {
        this.vaoID = vaoID;
        this.vbos = vbos;
        this.vertexCount = vertexCount;
        this.attribCount = attribCount;
    }

    public void cleanup() {
        MeshLoader.cleanup(this);
    }

    public int vaoID() {
        return vaoID;
    }

    public List<Integer> vbos() {
        return vbos;
    }

    public int vertexCount() {
        return vertexCount;
    }

    public int attribCount() {
        return attribCount;
    }
}
