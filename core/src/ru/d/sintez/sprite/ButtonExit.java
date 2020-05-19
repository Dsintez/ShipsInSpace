package ru.d.sintez.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.d.sintez.base.ScaleButton;
import ru.d.sintez.math.Rect;

public class ButtonExit extends ScaleButton {
    private final float MARGIN = 0.05f;

    public ButtonExit(TextureAtlas region) {
        super(new TextureRegion(region.findRegion("Exit")));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        setBottom(worldBounds.getBottom() + MARGIN);
        setRight(worldBounds.getRight() - MARGIN);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
