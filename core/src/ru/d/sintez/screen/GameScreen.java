package ru.d.sintez.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.BaseScreen;
import ru.d.sintez.math.Rect;
import ru.d.sintez.sprite.*;

public class GameScreen extends BaseScreen {

    private Background background;
    private Ship ship;
    private Texture backgroundImg;
    private TextureAtlas shipsAtlas;
    private TextureAtlas starAtlas;
    private Star[] stars;

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        ship.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        ship.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public void show() {
        super.show();
        backgroundImg = new Texture("Galaxy.jpg");
        background = new Background(backgroundImg);

        shipsAtlas = new TextureAtlas(Gdx.files.internal("Atlas/Ships.pack"));
        ship = new Ship(shipsAtlas, 3);

        starAtlas = new TextureAtlas(Gdx.files.internal("Atlas/Stars.pack"));
        stars = new Star[64];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(starAtlas);
        }
    }

    public boolean keyDown(int keycode) {
        ship.keyDown(keycode);
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        ship.keyUp(keycode);
        return false;
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        ship.resize(worldBounds);
        /*for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }*/
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
        shipsAtlas.dispose();
        starAtlas.dispose();
        super.dispose();
    }

    private void update(float delta) {
        ship.update(delta);
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
        ship.draw(batch);
        batch.end();
    }


}
