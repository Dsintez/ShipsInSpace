package ru.d.sintez.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.BaseScreen;
import ru.d.sintez.math.Rect;
import ru.d.sintez.sprite.*;

public class MenuScreen extends BaseScreen {

    private final Game game;
    private Background background;
    private Texture backgroundImg;
    private TextureAtlas buttonsAtlas;
    private TextureAtlas starAtlas;
    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;
    private Star[] stars;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonExit.touchDown(touch, pointer, button);
        buttonPlay.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch, pointer, button);
        buttonPlay.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public void show() {
        super.show();
        backgroundImg = new Texture("Galaxy.jpg");
        background = new Background(backgroundImg);

        buttonsAtlas = new TextureAtlas(Gdx.files.internal("Atlas/Buttons.pack"));
        buttonExit = new ButtonExit(buttonsAtlas);
        buttonPlay = new ButtonPlay(buttonsAtlas, game);

        starAtlas = new TextureAtlas(Gdx.files.internal("Atlas/Stars.pack"));
        stars = new Star[256];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(starAtlas);
        }
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        backgroundImg.dispose();
        backgroundImg.dispose();
        super.dispose();
    }

    private void update(float delta) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        batch.end();
    }
}
