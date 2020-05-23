package ru.d.sintez.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.BaseScreen;
import ru.d.sintez.math.Rect;
import ru.d.sintez.pool.BulletPool;
import ru.d.sintez.sprite.*;

public class GameScreen extends BaseScreen {

    private Background background;
    private Ship ship;
    private Texture backgroundImg;
    private TextureAtlas shipsAtlas;
    private TextureAtlas starAtlas;
    private Star[] stars;
    private BulletPool bulletPool;
    private Sound[] soundsMainShip;
    private EnemyShip enemyShip;

    public GameScreen(Game game) {
        super(game);
    }

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

        bulletPool = new BulletPool();
        soundsMainShip = new Sound[] {Gdx.audio.newSound(Gdx.files.internal("Sounds/shoot.mp3"))};
        shipsAtlas = new TextureAtlas(Gdx.files.internal("Atlas/Ships.pack"));
        ship = new Ship(shipsAtlas, 3, bulletPool, soundsMainShip);

        starAtlas = new TextureAtlas(Gdx.files.internal("Atlas/Stars.pack"));
        stars = new Star[64];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(starAtlas);
        }

        enemyShip = new EnemyShip(shipsAtlas, 1, bulletPool);
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
        if (keycode == Input.Keys.ESCAPE) {
            game.setScreen(new MenuScreen(game));
        }
        return false;
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        ship.resize(worldBounds);
        enemyShip.resize(worldBounds);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        free();
        draw();
    }

    @Override
    public void dispose() {
        bulletPool.dispose();
        backgroundImg.dispose();
        shipsAtlas.dispose();
        starAtlas.dispose();
        for (Sound sound : soundsMainShip) {
            sound.dispose();
        }
        super.dispose();
    }

    private void update(float delta) {
        bulletPool.updateActiveSprites(delta);
        ship.update(delta);
        enemyShip.update(delta);
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
    }

    private void free() {
        bulletPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        ship.draw(batch);
        enemyShip.draw(batch);
        batch.end();
    }
}
