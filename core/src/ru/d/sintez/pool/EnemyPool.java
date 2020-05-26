package ru.d.sintez.pool;

import com.badlogic.gdx.audio.Sound;
import ru.d.sintez.base.SpritePool;
import ru.d.sintez.math.Rect;
import ru.d.sintez.sprite.EnemyShip;

public class EnemyPool extends SpritePool<EnemyShip> {
    private Rect worldBounds;
    private BulletPool bulletPool;
    private Sound[] sounds;
    private ExplosionPool explosionPool;

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(bulletPool, worldBounds, sounds, explosionPool);
    }

    public EnemyPool(BulletPool bulletPool, Rect worldBounds, Sound[] sounds, ExplosionPool explosionPool)  {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.sounds = sounds;
        this.explosionPool = explosionPool;
    }
}
