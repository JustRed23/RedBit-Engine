package dev.JustRed23.redbit.engine.obj.components;

import dev.JustRed23.redbit.engine.render.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {

    private final Texture texture;
    private List<Sprite> sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int spriteCount, int spacing) {
        this.sprites = new ArrayList<>();
        this.texture = texture;

        int x = 0; int y = texture.height() - spriteHeight;
        for (int i = 0; i < spriteCount; i++) {
            float topY = (y + spriteHeight) / (float) texture.height();
            float rightX = (x + spriteWidth) / (float) texture.width();
            float bottomY = y / (float) texture.height();
            float leftX = x / (float) texture.width();

            Vector2f[] textureCoords = new Vector2f[] {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            sprites.add(new Sprite(texture, textureCoords));

            x += spriteWidth + spacing;
            if (x + spriteWidth >= texture.width()) {
                x = 0;
                y -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return sprites.get(index);
    }
}
