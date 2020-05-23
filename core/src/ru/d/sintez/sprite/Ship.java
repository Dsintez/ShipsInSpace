package ru.d.sintez.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.Sprite;
import ru.d.sintez.math.Rect;
import ru.d.sintez.pool.BulletPool;
import ru.d.sintez.utils.Regions;

public class Ship extends Sprite {

    private final byte PROTRACTION = 20;
    private static final float SIZE = 0.1f;
    private static final float MARGIN = 0.04f;
    private static final int INVALID_POINTER = -1;

    private final Vector2 v0;
    private final Vector2 v;

    private int leftPointer;
    private int rightPointer;

    private boolean pressedLeft;
    private boolean pressedRight;

    private Rect worldBounds;
    byte timer;

    private BulletPool bulletPool;
    private TextureRegion[] bulletRegion;
    private Vector2 bulletV;

    public Ship(TextureAtlas atlas, int countPNG, BulletPool bulletPool) {
        super(atlas.findRegion("mainShip"), 1, 3, countPNG);
        this.bulletPool = bulletPool;
        bulletRegion = Regions.split(atlas.findRegion("bullets"), 1, 2, 2);
        bulletV = new Vector2(0, 0.5f);
        v0  = new Vector2(0.4f, 0f);
        v = new Vector2();
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(SIZE);
        setBottom(worldBounds.getBottom() + MARGIN);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (regions.length > 1) {
            if (timer % PROTRACTION == 0) {
                if (frame == regions.length - 1) frame = 0;
                else frame++;
                timer = 0;
            }
            timer++;
        }
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
                shoot();
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

    private void shoot() {
        Bullet bullet1 = bulletPool.obtain();
        bullet1.set(this, bulletRegion[0], pos.cpy().add(0.03f, 0f), bulletV, 0.03f, worldBounds, 1);
        Bullet bullet2 = bulletPool.obtain();
        bullet2.set(this, bulletRegion[0], pos.cpy().sub(0.03f, 0f), bulletV, 0.03f, worldBounds, 1);
    }
}
