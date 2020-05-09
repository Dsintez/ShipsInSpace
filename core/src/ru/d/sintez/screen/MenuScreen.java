package ru.d.sintez.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture background;
    private Texture star;
    private Vector2 posStar;
    private Vector2 touch;
    private Vector2 direction;
    private Vector2 directionNor;

    private Vector2 speedup;
    private float length;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        direction = direction.set(touch).sub(posStar);
        length = direction.len();
        directionNor = direction.cpy().nor().scl(speedup);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public void show() {
        super.show();
        background = new Texture("galaxyNebula.jpg");
        star = new Texture("Star.png");
        posStar = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        touch = new Vector2();
        direction = new Vector2();
        directionNor = new Vector2();
        speedup = new Vector2(4.0f,4.0f);
        length = 0.0f;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (length >= 0) {
            length -= directionNor.len();
            direction.add(directionNor);
            posStar.add(directionNor);
        }
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(star, posStar.x, posStar.y, Gdx.graphics.getWidth() / 100 * 5, Gdx.graphics.getHeight() / 100 * 5);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
