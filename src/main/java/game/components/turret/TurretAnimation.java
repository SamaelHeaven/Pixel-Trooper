package game.components.turret;

import sparkle.assets.TextureAtlas;
import sparkle.components.Animation;
import sparkle.core.Component;
import sparkle.drawables.Sprite;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class TurretAnimation extends Component {
    private final Animation idle;
    private final Animation hide;
    private final Animation show;
    private final Animation shoot;
    private final Animation death;
    private Animation current;
    private Sprite sprite;

    public TurretAnimation() {
        var textureAtlas = new TextureAtlas(ResourceLoader.getTexture("textures/turret.png"), 7, 15);
        idle = new Animation(textureAtlas, 0, 15, 15, Animation.INDEFINITE);
        hide = new Animation(textureAtlas, 0.06f, 0, 14, 1);
        show = new Animation(textureAtlas, 0.06f, 30, 44, 1);
        shoot = new Animation(textureAtlas, 0.1f, 47, 52, 1);
        death = new Animation(textureAtlas, 0.1f, 75, 81, 1);
        current = hide;
        current.setIndex(current.getEndIndex());
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

    public Animation getHide() {
        return hide;
    }

    public Animation getShow() {
        return show;
    }

    public Animation getShoot() {
        return shoot;
    }

    public Animation getDeath() {
        return death;
    }

    public void use(Animation animation) {
        resetAllExcept(animation);
        current = animation;
    }

    private void resetAllExcept(Animation exception) {
        var animations = new Animation[]{idle, hide, show, shoot, death};
        for (var animation : animations) {
            if (animation != exception) {
                animation.reset();
            }
        }
    }
}