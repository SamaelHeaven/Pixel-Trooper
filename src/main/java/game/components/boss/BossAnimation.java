package game.components.boss;

import sparkle.components.Animation;
import sparkle.assets.TextureAtlas;
import sparkle.core.Component;
import sparkle.drawables.Sprite;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class BossAnimation extends Component {
    private final Animation idle;
    private final Animation walk;
    private final Animation attack;
    private final Animation summon;
    private final Animation death;
    private Animation current;
    private Sprite sprite;

    public BossAnimation() {
        var textureAtlas = new TextureAtlas(ResourceLoader.getTexture("textures/boss.png"), 8, 8);
        idle = new Animation(textureAtlas, 0.2f, 0, 7, Animation.INDEFINITE);
        walk = new Animation(textureAtlas, 0.225f, 8, 15, Animation.INDEFINITE);
        attack = new Animation(textureAtlas, 0.2f, 16, 25, 1);
        summon = new Animation(textureAtlas, 0.2f, 39, 47, 1);
        death = new Animation(textureAtlas, 0.215f, 29, 38, 1);
        current = idle;
    }

    @Override
    protected void start() {
        sprite = Objects.requireNonNull(super.getComponent(Sprite.class));
    }

    @Override
    protected void update() {
        current.update();
        sprite.setTexture(current.getTexture());
    }

    public Animation getIdle() {
        return idle;
    }

    public Animation getWalk() {
        return walk;
    }

    public Animation getAttack() {
        return attack;
    }

    public Animation getSummon() {
        return summon;
    }

    public Animation getDeath() {
        return death;
    }

    public Animation getCurrent() {
        return current;
    }

    public void use(Animation animation) {
        resetAllExcept(animation);
        current = animation;
    }

    private void resetAllExcept(Animation exception) {
        var animations = new Animation[]{idle, walk, attack, summon, death};
        for (var animation : animations) {
            if (animation != exception) {
                animation.reset();
            }
        }
    }
}