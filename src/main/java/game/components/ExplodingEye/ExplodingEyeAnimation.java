package game.components.ExplodingEye;

import sparkle.components.Animation;
import sparkle.assets.TextureAtlas;
import sparkle.core.Component;
import sparkle.drawables.Sprite;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class ExplodingEyeAnimation extends Component {
    private final Animation idle;
    private final Animation blink;
    private final Animation explode;
    private Animation current;
    private Sprite sprite;

    public ExplodingEyeAnimation() {
        var textureAtlas = new TextureAtlas(ResourceLoader.getTexture("textures/exploding-eye.png"), 6, 20);
        idle = new Animation(textureAtlas, 0.1f, 0, 9, Animation.INDEFINITE);
        blink = new Animation(textureAtlas, 0.1f, 100, 101, Animation.INDEFINITE);
        explode = new Animation(textureAtlas, 0.1f, 100, 114, 1);
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

    public Animation getBlink() {
        return blink;
    }

    public Animation getExplode() {
        return explode;
    }

    public void use(Animation animation) {
        resetAllExcept(animation);
        current = animation;
    }

    private void resetAllExcept(Animation exception) {
        var animations = new Animation[]{idle, blink, explode};
        for (var animation : animations) {
            if (animation != exception) {
                animation.reset();
            }
        }
    }
}