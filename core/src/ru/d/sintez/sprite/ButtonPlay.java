package ru.d.sintez.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.d.sintez.base.ScaleButton;
import ru.d.sintez.math.Rect;
import ru.d.sintez.screen.GameScreen;

public class ButtonPlay extends ScaleButton {

    private final float MARGIN = 0.01f;
    private final Game game;

    public ButtonPlay(TextureAtlas region, Game game) {
        super(new TextureRegion(region.findRegion("Play")));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.09f);
        setBottom(worldBounds.getBottom() + MARGIN);
        setLeft(worldBounds.getLeft() + MARGIN);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen(game));
    }
}
