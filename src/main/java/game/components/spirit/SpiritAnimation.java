package game.components.spirit;

import sparkle.components.Animation;
import sparkle.assets.TextureAtlas;
import sparkle.core.Component;
import sparkle.drawables.Sprite;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class SpiritAnimation extends Component {
    private final Animation idle;
    private final Animation attack;
    private final Animation death;
    private Animation current;
    private Sprite sprite;

    public SpiritAnimation() {
        var textureAtlas = new TextureAtlas(ResourceLoader.getTexture("textures/spirit.png"), 5, 12);
        idle = new Animation(textureAtlas, 0.25f, 0, 5, Animation.INDEFINITE);
        attack = new Animation(textureAtlas, 0.2f, 12, 22, 1);
        death = new Animation(textureAtlas, 0.2f, 36, 44, 1);
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

    public Animation getCurrent() {
        return current;
    }

    public Animation getIdle() {
        return idle;
    }

    public Animation getAttack() {
        return attack;
    }

    public Animation getDeath() {
        return death;
    }

    public void use(Animation animation) {
        resetAllExcept(animation);
        current = animation;
    }

    private void resetAllExcept(Animation exception) {
        var animations = new Animation[]{idle, attack, death};
        for (var animation : animations) {
            if (animation != exception) {
                animation.reset();
            }
        }
    }
}