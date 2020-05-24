package ru.d.sintez.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.Sprite;
import ru.d.sintez.math.Rect;
import ru.d.sintez.pool.BulletPool;
import ru.d.sintez.utils.Regions;

public class EnemyShip extends Sprite {

    private final byte PROTRACTION = 15;
    private static final float SIZE = 0.1f;

    private final Vector2 v0;
    private final Vector2 v;

    private boolean shoot;

    private Rect worldBounds;
    byte timer;

    private BulletPool bulletPool;
    private TextureRegion[] bulletRegion;
    private Vector2 bulletV;

    public EnemyShip(TextureAtlas atlas, int countPNG, BulletPool bulletPool) {
        super(atlas.findRegion("enemyAngel"), 1, 1, countPNG);
        this.bulletPool = bulletPool;
        bulletRegion = Regions.split(atlas.findRegion("bullets"), 1, 2, 2);
        bulletV = new Vector2(0, -0.5f);
        v0  = new Vector2(0f, -0.01f);
        v = new Vector2();
        shoot = true;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(SIZE);
        setBottom(worldBounds.getTop());
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v0, delta);
        if (timer % PROTRACTION == 0) {
            if (shoot) shoot();
            timer = 0;
        }
        if (shoot) timer++;
        else timer = 0;

        if (getLeft() < worldBounds.getLeft()) {
            stop();
            setLeft(worldBounds.getLeft());
        } else if (getRight() > worldBounds.getRight()) {
            stop();
            setRight(worldBounds.getRight());
        }
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
        bullet1.set(this, bulletRegion[1], pos.cpy().add(0.03f, 0f), bulletV, 0.03f, worldBounds, 1);
        Bullet bullet2 = bulletPool.obtain();
        bullet2.set(this, bulletRegion[1], pos.cpy().sub(0.03f, 0f), bulletV, 0.03f, worldBounds, 1);
    }
}
