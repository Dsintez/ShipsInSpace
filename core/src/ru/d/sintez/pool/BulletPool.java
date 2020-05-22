package ru.d.sintez.pool;

import ru.d.sintez.base.SpritePool;
import ru.d.sintez.sprite.Bullet;

public class BulletPool extends SpritePool<Bullet> {
    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
