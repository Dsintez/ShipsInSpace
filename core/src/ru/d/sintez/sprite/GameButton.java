package ru.d.sintez.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.Sprite;
import ru.d.sintez.math.Rect;

public class GameButton extends Sprite {
    public GameButton(Texture region) {
        super(new TextureRegion(region));
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (super.isMe(touch)) {
            scale = 0.5f;
        }
        return false;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.08f);
        this.pos.set(worldBounds.pos);
        pos.sub(0.23f, 0.43f);
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (super.isMe(touch)) {
            Gdx.app.exit();
        }
        scale = 1f;
        return false;
    }
}
