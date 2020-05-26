package ru.d.sintez.pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.d.sintez.base.SpritePool;
import ru.d.sintez.sprite.Explosion;

public class ExplosionPool extends SpritePool<Explosion> {
    private TextureAtlas atlas;
    private Sound sound;

    @Override
    protected Explosion newObject() {
        return new Explosion(atlas, sound);
    }

    public ExplosionPool(TextureAtlas atlas) {
        this.atlas = atlas;
        sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/boom.mp3"));
    }

    @Override
    public void dispose() {
        super.dispose();
        sound.dispose();
    }
}
