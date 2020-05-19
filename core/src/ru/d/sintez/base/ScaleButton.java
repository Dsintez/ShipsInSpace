package ru.d.sintez.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class ScaleButton extends Sprite {

    private final float SCALE = 0.9f;

    private boolean pressed;
    private int pointer;

    public ScaleButton(TextureRegion region) {
        super(region);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (pressed || !isMe(touch)) {
            return false;
        }
        this.pointer = pointer;
        pressed = true;
        scale = SCALE;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (this.pointer != pointer || !pressed) {
            return false;
        }
        if (isMe(touch)) {
            action();
        }
        pressed = false;
        scale = 1f;
        return false;
    }

    public abstract void action();
}
