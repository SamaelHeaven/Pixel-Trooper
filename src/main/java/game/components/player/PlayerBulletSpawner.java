package game.components.player;

import sparkle.assets.Audio;
import sparkle.core.*;
import sparkle.drawables.Sprite;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class PlayerBulletSpawner extends Component {
    private final Audio sound = ResourceLoader.getAudio("audio/player-shoot-sound.ogg", 0.25f);
    private Sprite sprite;
    private float shootTimeLeft;

    @Override
    protected void start() {
        sprite = Objects.requireNonNull(super.getComponent(Sprite.class));
    }

    @Override
    protected void update() {
        shootTimeLeft -= Time.getDelta();
        if (isShootPressed() && shootTimeLeft <= 0) {
            shootTimeLeft = 0.5f;
            Game.getScene().addGameObject(new GameObject("playerBullet", new PlayerBullet(sprite)));
            sound.play();
        }
    }

    private boolean isShootPressed() {
        return KeyInput.isKeyPressed(Key.L) || KeyInput.isKeyPressed(Key.X) || Math.round(Gamepad.getGamepads().getFirst().getAxis(GamepadAxis.RIGHT_TRIGGER)) == 1;
    }
}