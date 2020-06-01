package ru.d.sintez.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.Ship;
import ru.d.sintez.math.Rect;
import ru.d.sintez.pool.BulletPool;
import ru.d.sintez.pool.ExplosionPool;
import ru.d.sintez.utils.Regions;

public class MainShip extends Ship {

    private static final float SIZE = 0.1f;
    private static final float MARGIN = 0.04f;
    private static final int INVALID_POINTER = -1;
    private static final int HP = 100;

    private int leftPointer;
    private int rightPointer;

    private boolean pressedLeft;
    private boolean pressedRight;

    public MainShip(TextureAtlas atlas, int countPNG, BulletPool bulletPool, Sound[] sounds, ExplosionPool explosionPool) {
        super(atlas.findRegion("mainShip"), 1, 3, countPNG);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        bulletRegion = Regions.split(atlas.findRegion("bullets"), 1, 2, 2);
        bulletV = new Vector2(0, 0.5f);
        bulletHeight = 0.03f;
        bulletDamage = 1;
        reloadInterval = 0.3f;
        healthPoint = HP;
        v0.set(0.4f, 0f);
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;
        shoot = false;
        this.sounds = sounds;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(SIZE);
        setBottom(worldBounds.getBottom() + MARGIN);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        autoShoot(delta);
        if (getLeft() < worldBounds.getLeft()) {
            stop();
            setLeft(worldBounds.getLeft());
        } else if (getRight() > worldBounds.getRight()) {
            stop();
            setRight(worldBounds.getRight());
        }
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
            case Input.Keys.SPACE:
                shoot = true;
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
            case Input.Keys.SPACE:
                shoot = false;
                break;
        }
        return false;
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }

    @Override
    protected void shoot() {
        sounds[0].play(0.3f);
        Bullet bullet1 = bulletPool.obtain();
        bullet1.set(this, bulletRegion[0], pos.cpy().add(0.03f, 0f), bulletV, 0.03f, worldBounds, 1);
        Bullet bullet2 = bulletPool.obtain();
        bullet2.set(this, bulletRegion[0], pos.cpy().sub(0.03f, 0f), bulletV, 0.03f, worldBounds, 1);
    }

    public boolean isBulletCollision(Bullet bullet) {
        return !(bullet.getRight() < getLeft() ||
                bullet.getLeft() > getRight() ||
                bullet.getBottom() > getTop() ||
                bullet.getTop() < getBottom());
    }
}
