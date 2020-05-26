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
import ru.d.sintez.pool.EnemyPool;
import ru.d.sintez.pool.ExplosionPool;
import ru.d.sintez.sprite.*;
import ru.d.sintez.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private Background background;
    private MainShip mainShip;
    private Texture backgroundImg;
    private TextureAtlas shipsAtlas;
    private TextureAtlas starAtlas;
    private Star[] stars;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private Sound[] soundsShip;
    private EnemyEmitter enemyEmitter;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShip.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public void show() {
        super.show();
        backgroundImg = new Texture("Galaxy.jpg");
        background = new Background(backgroundImg);

        bulletPool = new BulletPool();
        soundsShip = new Sound[] {Gdx.audio.newSound(Gdx.files.internal("Sounds/shoot.mp3"))};
        shipsAtlas = new TextureAtlas(Gdx.files.internal("Atlas/Ships.atlas"));
        explosionPool = new ExplosionPool(shipsAtlas);
        mainShip = new MainShip(shipsAtlas, 3, bulletPool, soundsShip, explosionPool);
        enemyPool = new EnemyPool(bulletPool, worldBounds, soundsShip, explosionPool);
        enemyEmitter = new EnemyEmitter(shipsAtlas, enemyPool);

        starAtlas = new TextureAtlas(Gdx.files.internal("Atlas/Stars.pack"));
        stars = new Star[64];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(starAtlas);
        }
    }

    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        if (keycode == Input.Keys.ESCAPE) {
            game.setScreen(new MenuScreen(game));
        }
        return false;
    }

    @Override
    public void resize(Rect worldBounds) {
        enemyEmitter.resize(worldBounds);
        background.resize(worldBounds);
        mainShip.resize(worldBounds);
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
        enemyPool.dispose();
        explosionPool.dispose();
        for (Sound sound : soundsShip) {
            sound.dispose();
        }
        super.dispose();
    }

    private void update(float delta) {
        bulletPool.updateActiveSprites(delta);
        mainShip.update(delta);
        enemyPool.updateActiveSprites(delta);
        enemyEmitter.generate(delta);
        explosionPool.updateActiveSprites(delta);
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
    }

    private void free() {
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        mainShip.draw(batch);
        enemyPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }
}
