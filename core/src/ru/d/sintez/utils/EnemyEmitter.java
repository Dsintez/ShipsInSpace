package ru.d.sintez.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.d.sintez.math.Rect;
import ru.d.sintez.math.Rnd;
import ru.d.sintez.pool.EnemyPool;
import ru.d.sintez.sprite.EnemyShip;

public class EnemyEmitter {

    private static final float GENERATE_INTERVAL = 3f;

    private static  final float ENEMY_SMALL_HEIGHT = 0.07f;
    private static final int ENEMY_SMALL_HP = 1;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.02f;
    private static final float ENEMY_SMALL_VY = -0.3f;
    private static final int ENEMY_SMALL_DAMAGE = 1;
    private static final int ENEMY_SMALL_HOLDER = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 1f;

    private static  final float ENEMY_MEDIUM_HEIGHT = 0.15f;
    private static final int ENEMY_MEDIUM_HP = 3;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.042f;
    private static final float ENEMY_MEDIUM_VY = -0.25f;
    private static final int ENEMY_MEDIUM_DAMAGE = 5;
    private static final int ENEMY_MEDIUM_HOLDER = 1;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL = 4f;

    private static  final float ENEMY_BIG_HEIGHT = 0.2f;
    private static final int ENEMY_BIG_HP = 5;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.06f;
    private static final float ENEMY_BIG_VY = -0.3f;
    private static final int ENEMY_BIG_DAMAGE = 10;
    private static final int ENEMY_BIG_HOLDER = 2;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 1f;

    private Rect worldBounds;
    private float generateTimer;

    private final TextureRegion[] enemySmallRegions;
    private final TextureRegion[] enemyMediumRegions;
    private final TextureRegion[] enemyBigRegions;
    private final Vector2 enemySmallV;
    private final Vector2 enemyMediumV;
    private final Vector2 enemyBigV;

    private final TextureRegion bulletRegion;
    private final EnemyPool enemyPool;

    private int level;

    public EnemyEmitter(TextureAtlas atlas, EnemyPool enemyPool) {
        TextureRegion enemy0 = atlas.findRegion("smallShip");
        this.enemySmallRegions = Regions.split(enemy0, 1, 2, 2);
        TextureRegion enemy1 = atlas.findRegion("mediumShip");
        this.enemyMediumRegions = Regions.split(enemy1, 1, 2, 2);
        TextureRegion enemy2 = atlas.findRegion("bigShip");
        this.enemyBigRegions = Regions.split(enemy2, 1, 2, 2);
        this.enemySmallV = new Vector2(0, -0.075f);
        this.enemyMediumV = new Vector2(0, -0.03f);
        this.enemyBigV = new Vector2(0, -0.005f);
        this.bulletRegion = Regions.split(atlas.findRegion("bullets"), 1, 2, 2)[1];
        this.enemyPool = enemyPool;
        this.level = 1;
    }

    public void resize(Rect worldBounds){
        this.worldBounds = worldBounds;
    }

    public void generate(float delta, int frags) {
        level = frags / 10 + 1;
        generateTimer += delta * level;
        if (generateTimer >= GENERATE_INTERVAL) {
            generateTimer = 0f;
            EnemyShip enemyShip = enemyPool.obtain();
            float type = (float) Math.random();
            if (type < 0.5f) {
                enemyShip.set(
                        enemySmallRegions,
                        enemySmallV,
                        bulletRegion,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        ENEMY_SMALL_VY,
                        ENEMY_SMALL_DAMAGE,
                        ENEMY_SMALL_HOLDER,
                        ENEMY_SMALL_RELOAD_INTERVAL,
                        ENEMY_SMALL_HP,
                        ENEMY_SMALL_HEIGHT
                );
            } else if (type < 0.8f) {
                enemyShip.set(
                        enemyMediumRegions,
                        enemyMediumV,
                        bulletRegion,
                        ENEMY_MEDIUM_BULLET_HEIGHT,
                        ENEMY_MEDIUM_VY,
                        ENEMY_MEDIUM_DAMAGE,
                        ENEMY_MEDIUM_HOLDER,
                        ENEMY_MEDIUM_RELOAD_INTERVAL,
                        ENEMY_MEDIUM_HP,
                        ENEMY_MEDIUM_HEIGHT
                );
            } else {
                enemyShip.set(
                        enemyBigRegions,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_VY,
                        ENEMY_BIG_DAMAGE,
                        ENEMY_BIG_HOLDER,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HP,
                        ENEMY_BIG_HEIGHT
                );
            }
            enemyShip.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemyShip.getHalfWidth(), worldBounds.getRight() - enemyShip.getHalfWidth());
            enemyShip.setBottom(worldBounds.getTop());
        }
    }

    public int getLevel() {
        return level;
    }
}
