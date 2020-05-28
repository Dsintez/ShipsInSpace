package ru.d.sintez.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.d.sintez.base.ScaleButton;
import ru.d.sintez.math.Rect;
import ru.d.sintez.screen.GameScreen;

public class NewGame extends ScaleButton {

    private final Game game;

    public NewGame(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("newGame"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(-0.3f);
        setHeightProportion(0.3f);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen(game));
    }
}
