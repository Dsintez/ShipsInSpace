package ru.d.sintez.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.Sprite;
import ru.d.sintez.math.Rect;

public class Ship extends Sprite {

    private final byte PROTRACTION = 20;

    private float length;
    private Vector2 direction;
    private Vector2 directionNor;
    private Vector2 speedup;
    private Vector2 touch;
    byte timer;

    public Ship(TextureAtlas atlas, int countPNG) {
        super(new TextureRegion());
        regions = new TextureRegion[countPNG];
        for (int i = 0; i < regions.length; i++) {
            regions[i] = atlas.findRegion("Ship", i);
        }
        direction = new Vector2();
        directionNor = new Vector2();
        touch = new Vector2();
        speedup = new Vector2(0.005f, 0.005f);
        length = 0.0f;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        this.pos.set(worldBounds.pos);
    }

    @Override
    public void update(float delta) {
        if (regions.length > 1) {
            if (timer % PROTRACTION == 0) {
                if (frame == regions.length - 1) frame = 0;
                else frame++;
                timer = 0;
            }
            timer++;
        }
        if (length >= 0) {
            super.update(delta);
            length -= directionNor.len();
            direction.add(directionNor);
            pos.add(directionNor);
        } else {
            pos.set(touch);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.printf("Ship touchDown screenX = %s, screenY = %s%n", screenX, screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch.set(touch);
        direction = direction.set(touch).sub(pos);
        length = direction.len();
        directionNor = direction.cpy().nor().scl(speedup);
        System.out.printf("direction x = %s, y = %s%n", direction.x, direction.y);
        return false;
    }
}
