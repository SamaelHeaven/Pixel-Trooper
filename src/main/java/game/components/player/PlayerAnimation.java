package game.components.player;

import sparkle.assets.TextureAtlas;
import sparkle.components.Animation;
import sparkle.core.Component;
import sparkle.drawables.Sprite;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class PlayerAnimation extends Component {
    private final Animation crouch;
    private final Animation death;
    private final Animation idle;
    private final Animation jump;
    private final Animation run;
    private Animation current;
    private Sprite sprite;

    public PlayerAnimation() {
        crouch = new Animation(new TextureAtlas(ResourceLoader.getTexture("textures/player-crouch.png"), 1, 3), 0.125f, 0, 2, 1);
        death = new Animation(new TextureAtlas(ResourceLoader.getTexture("textures/player-death.png"), 1, 8), 0.125f, 0, 7, 1);
        idle = new Animation(new TextureAtlas(ResourceLoader.getTexture("textures/player-idle.png"), 1, 5), 0.25f, 0, 4, Animation.INDEFINITE);
        jump = new Animation(new TextureAtlas(ResourceLoader.getTexture("textures/player-jump.png"), 1, 2), 0.25f, 0, 1, 1);
        run = new Animation(new TextureAtlas(ResourceLoader.getTexture("textures/player-run.png"), 1, 6), 0.15f, 0, 5, Animation.INDEFINITE);
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

    public Animation getCrouch() {
        return crouch;
    }

    public Animation getDeath() {
        return death;
    }

    public Animation getIdle() {
        return idle;
    }

    public Animation getJump() {
        return jump;
    }

    public Animation getRun() {
        return run;
    }

    public void use(Animation animation) {
        resetAllExcept(animation);
        current = animation;
    }

    private void resetAllExcept(Animation exception) {
        var animations = new Animation[]{crouch, death, idle, jump, run};
        for (var animation : animations) {
            if (animation != exception) {
                animation.reset();
            }
        }
    }
}