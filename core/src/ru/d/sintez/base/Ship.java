package ru.d.sintez.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.math.Rect;
import ru.d.sintez.pool.BulletPool;
import ru.d.sintez.pool.ExplosionPool;
import ru.d.sintez.sprite.Bullet;
import ru.d.sintez.sprite.Explosion;

public class Ship  extends Sprite {

    protected final Vector2 v0;
    protected final Vector2 v;
    protected Rect worldBounds;

    protected ExplosionPool explosionPool;
    protected BulletPool bulletPool;
    protected TextureRegion[] bulletRegion;
    protected Vector2 bulletV;

    protected boolean shoot;
    protected float reloadTimer;
    protected Sound[] sounds;
    protected float bulletHeight;
    protected int bulletDamage;
    protected int healthPoint;
    protected float reloadInterval;

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        v0 = new Vector2();
        v = new Vector2();
    }

    public Ship(BulletPool bulletPool, Rect worldBounds, Sound[] sounds, ExplosionPool explosionPool) {
        this.worldBounds = worldBounds;
        this.explosionPool = explosionPool;
        this.bulletPool = bulletPool;
        this.sounds = sounds;
        v0 = new Vector2();
        v = new Vector2();
        bulletV = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
    }

    protected void shoot() {
        sounds[0].play(0.3f);
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion[0], pos, bulletV, bulletHeight, worldBounds, bulletDamage);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (reloadTimer > reloadInterval) {
            if (shoot && !isOutside(worldBounds)) {
                shoot();
                reloadTimer = 0;
            }
        }
        if (shoot) reloadTimer+=delta;
    }

    @Override
    public void destroy() {
        boom();
        super.destroy();
    }

    private void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }
}
