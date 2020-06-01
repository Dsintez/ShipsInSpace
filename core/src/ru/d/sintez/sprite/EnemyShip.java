package ru.d.sintez.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.Ship;
import ru.d.sintez.math.Rect;
import ru.d.sintez.pool.BulletPool;
import ru.d.sintez.pool.ExplosionPool;

public class EnemyShip extends Ship {

    public static final float Y = -0.3f;
    private static final float SIZE = 0.1f;

    private TextureRegion bulletRegion;

    public EnemyShip(BulletPool bulletPool, Rect worldBounds, Sound[] sounds, ExplosionPool explosionPool) {
        super(bulletPool, worldBounds, sounds, explosionPool);
        shoot = true;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(SIZE);
        setBottom(worldBounds.getTop());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (getTop() < worldBounds.getTop()) {
            autoShoot(delta);
            v.set(v0);
        }
        if (getBottom() <= worldBounds.getBottom()) {
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int bulletDamage,
            float reloadInterval,
            int healthPoint,
            float height
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.healthPoint = healthPoint;
        setHeightProportion(height);
        this.v.set(0f, Y);
    }

    @Override
    protected void shoot() {
        sounds[0].play(0.1f);
        Bullet bullet1 = bulletPool.obtain();
        bullet1.set(this, bulletRegion, pos.cpy().add(bulletHeight*1.5f,-getHalfHeight()), bulletV, bulletHeight, worldBounds, bulletDamage);
        Bullet bullet2 = bulletPool.obtain();
        bullet2.set(this, bulletRegion, pos.cpy().sub(bulletHeight*1.5f,getHalfHeight()), bulletV, bulletHeight, worldBounds, bulletDamage);
    }

    public boolean isBulletCollision(Bullet bullet) {
        return !(bullet.getRight() < getLeft() ||
                bullet.getLeft() > getRight() ||
                bullet.getBottom() > getTop() ||
                bullet.getTop() < pos.y);
    }
}
