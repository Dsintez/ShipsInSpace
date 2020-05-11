package ru.d.sintez.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.Sprite;
import ru.d.sintez.math.Rect;

public class Ship extends Sprite {

    private Object lock = new Object();
    private float length;
    private Vector2 direction;
    private Vector2 directionNor;
    private Vector2 speedup;

    public Ship(Texture region) {
        super(new TextureRegion(region));
        direction = new Vector2();
        directionNor = new Vector2();
        speedup = new Vector2(0.01f, 0.01f);
        length = 0.0f;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.04f);
        this.pos.set(worldBounds.pos);
    }

    @Override
    public void update(float delta) {
        synchronized (lock) {
            if (length >= 0) {
                super.update(delta);
                length -= directionNor.len();
                direction.add(directionNor);
                pos.add(directionNor);
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.printf("Ship touchDown screenX = %s, screenY = %s%n", screenX, screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        synchronized (lock) {
            direction = direction.set(touch).sub(pos);
            length = direction.len();
            directionNor = direction.cpy().nor().scl(speedup);
            System.out.printf("direction x = %s, y = %s%n", direction.x, direction.y);
        }
        return false;
    }
}
