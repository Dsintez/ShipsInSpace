package ru.d.sintez.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.d.sintez.base.Sprite;
import ru.d.sintez.math.Rect;

public class GameOver extends Sprite {
    public GameOver(TextureAtlas atlas) {
        super(atlas.findRegion("gameOver"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setTop(0.19f);
        setHeightProportion(0.6f);
    }
}
