package game.components.jumper;

import sparkle.assets.TextureAtlas;
import sparkle.components.Animation;
import sparkle.core.Component;
import sparkle.drawables.Sprite;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class JumperAnimation extends Component {
    private final Animation idle;
    private final Animation activated;
    private Animation current;
    private Sprite sprite;

    public JumperAnimation() {
        var textureAtlas = new TextureAtlas(ResourceLoader.getTexture("textures/jumper.png"), 1, 8);
        idle = new Animation(textureAtlas, 0, 0, 1, 0);
        activated = new Animation(textureAtlas, 0.05f, 0, 7, 1);
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

    public Animation getActivated() {
        return activated;
    }

    public Animation getCurrent() {
        return current;
    }

    public void use(Animation animation) {
        resetAllExcept(animation);
        current = animation;
    }

    private void resetAllExcept(Animation exception) {
        var animations = new Animation[]{idle, activated};
        for (var animation : animations) {
            if (animation != exception) {
                animation.reset();
            }
        }
    }
}