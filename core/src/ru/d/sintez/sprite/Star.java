package ru.d.sintez.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.Sprite;
import ru.d.sintez.math.Rect;
import ru.d.sintez.math.Rnd;

public class Star extends Sprite {

    private static final float ANIMATE_INTERVAL = 4f;
    private Vector2 v;
    private Rect worldBounds;

    private float animateTimer;
    private boolean animate;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("stars"), 6, 5, 28);
        v = new Vector2();
        float vx = Rnd.nextFloat(-0.005f, 0.005f);
        float vy = Rnd.nextFloat(-0.1f, -0.05f);
        v.set(vx, vy);
        worldBounds = new Rect();
        frame = 27;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.012f);
        float PosX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float PosY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(PosX, PosY);
        this.worldBounds = worldBounds;
        animateTimer = Rnd.nextFloat(0f, ANIMATE_INTERVAL);
    }

    public void update(float delta) {
        animateTimer += delta;
        if (animateTimer > ANIMATE_INTERVAL/2 && animate) {
            if (++frame == regions.length) {
                frame = 0;
                animate = false;
            }
        }
        if (animateTimer > ANIMATE_INTERVAL) {
            animateTimer = 0f;
            frame = 0;
            animate = true;
        }
        pos.mulAdd(v, delta);
        checkBounds();
    }

    private void checkBounds() {
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
        if (getLeft() > worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getTop() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
        }
    }
}
