package ru.d.sintez.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.base.BaseScreen;
import ru.d.sintez.math.Rect;
import ru.d.sintez.sprite.Background;
import ru.d.sintez.sprite.GameButton;
import ru.d.sintez.sprite.Ship;

public class MenuScreen extends BaseScreen {

    private Background background;
    private Ship ship;
    private Texture backgroundImg;
    private Texture star1;
    private Texture star2;
    private Texture buttonImg;
    private GameButton buttonExit;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        ship.touchDown(touch, pointer, button);
        buttonExit.touchDown(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch, pointer, button);
        return super.touchUp(touch, pointer, button);
    }

    @Override
    public void show() {
        super.show();
        backgroundImg = new Texture("galaxyNebula.jpg");
        background = new Background(backgroundImg);

        star1 = new Texture("Star\\Star1.png");
        star2 = new Texture("Star\\Star2.png");
        ship = new Ship(star1, star1, star1, star1, star1, star2, star2, star2, star2, star2);

        buttonImg = new Texture("Exit.png");
        buttonExit = new GameButton(buttonImg);
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        buttonExit.resize(worldBounds);
        ship.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        ship.update(delta);
        batch.begin();
        background.draw(batch);
        buttonExit.draw(batch);
        ship.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        backgroundImg.dispose();
        star1.dispose();
        star2.dispose();
        backgroundImg.dispose();
        super.dispose();
    }
}
