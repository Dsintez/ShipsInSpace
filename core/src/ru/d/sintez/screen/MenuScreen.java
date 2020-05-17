package ru.d.sintez.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    private Texture buttonImg;
    private TextureAtlas atlas;
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
        backgroundImg = new Texture("Galaxy.jpg");
        background = new Background(backgroundImg);

        atlas = new TextureAtlas(Gdx.files.internal("Atlas/Atlas.pack"));
        ship = new Ship(atlas, 3);

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
        atlas.dispose();
        backgroundImg.dispose();
        super.dispose();
    }
}
