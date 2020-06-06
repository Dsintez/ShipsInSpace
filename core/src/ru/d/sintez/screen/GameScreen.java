package ru.d.sintez.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import ru.d.sintez.base.BaseScreen;
import ru.d.sintez.base.Font;
import ru.d.sintez.math.Rect;
import ru.d.sintez.pool.BulletPool;
import ru.d.sintez.pool.EnemyPool;
import ru.d.sintez.pool.ExplosionPool;
import ru.d.sintez.sprite.*;
import ru.d.sintez.utils.EnemyEmitter;

import java.util.List;

public class GameScreen extends BaseScreen {

    private static final float TEXT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private enum State {PLAYING, GAME_OVER}

    private Background background;
    private MainShip mainShip;
    private Texture backgroundImg;
    private TextureAtlas atlas;
    private Star[] stars;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private Sound[] soundsShip;
    private EnemyEmitter enemyEmitter;
    private State state;
    private GameOver gameOver;
    private NewGame newGame;
    private int frags;
    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        backgroundImg = new Texture("Galaxy.jpg");
        background = new Background(backgroundImg);

        bulletPool = new BulletPool();
        soundsShip = new Sound[]{Gdx.audio.newSound(Gdx.files.internal("Sounds/shoot.mp3"))};
        atlas = new TextureAtlas(Gdx.files.internal("Atlas/Ships.atlas"));
        explosionPool = new ExplosionPool(atlas);
        mainShip = new MainShip(atlas, 3, bulletPool, soundsShip, explosionPool);
        enemyPool = new EnemyPool(bulletPool, worldBounds, soundsShip, explosionPool);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool);

        font = new Font("Font/font.fnt", "Font/font.png");
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();

        stars = new Star[64];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        state = State.PLAYING;
        gameOver = new GameOver(atlas);
        newGame = new NewGame(atlas, this);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            mainShip.touchDown(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            newGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            mainShip.touchUp(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            newGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
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
        gameOver.resize(worldBounds);
        newGame.resize(worldBounds);
        font.setSize(FONT_SIZE);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollision();
        free();
        draw();
    }

    @Override
    public void dispose() {
        bulletPool.dispose();
        backgroundImg.dispose();
        atlas.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        font.dispose();
        for (Sound sound : soundsShip) {
            sound.dispose();
        }
        super.dispose();
    }

    public void startNewGame() {
        frags = 0;
        mainShip.startNewGame();
        bulletPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        state = State.PLAYING;
    }

    private void update(float delta) {
        if (state == State.PLAYING) {
            bulletPool.updateActiveSprites(delta);
            mainShip.update(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta, frags);
        }
        explosionPool.updateActiveSprites(delta);
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
    }

    private void checkCollision() {
        if (state != State.PLAYING) {
            return;
        }
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (EnemyShip enemyShip : enemyShipList) {
            float minDist = enemyShip.getHalfHeight() + mainShip.getHalfHeight();
            if (mainShip.pos.dst(enemyShip.pos) < minDist) {
                enemyShip.destroy();
                mainShip.damage(enemyShip.getBulletDamage());
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != mainShip || bullet.isDestroyed()) {
                    continue;
                }
                if (enemyShip.isBulletCollision(bullet)) {
                    enemyShip.damage(bullet.getDamage());
                    bullet.destroy();
                    if (enemyShip.isDestroyed()) {
                        frags += 1;
                    }
                }
            }
        }
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == mainShip || bullet.isDestroyed()) {
                continue;
            }
            if (mainShip.isBulletCollision(bullet)) {
                mainShip.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
        if (mainShip.isDestroyed()) {
            state = State.GAME_OVER;
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
        if (state == State.PLAYING) {
            bulletPool.drawActiveSprites(batch);
            mainShip.draw(batch);
            enemyPool.drawActiveSprites(batch);
        } else if (state == State.GAME_OVER) {
            gameOver.draw(batch);
            newGame.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + TEXT_MARGIN, worldBounds.getTop() - TEXT_MARGIN, Align.left);
        font.draw(batch, sbHP.append(HP).append(mainShip.getHealthPoint()), worldBounds.pos.x, worldBounds.getTop() - TEXT_MARGIN, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - TEXT_MARGIN, worldBounds.getTop() - TEXT_MARGIN, Align.right);
    }
}
