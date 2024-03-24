package game.components.jumper;

import game.components.player.PlayerAnimation;
import game.components.player.PlayerHealth;
import game.components.player.PlayerMovement;
import sparkle.assets.Audio;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.GameObject;
import sparkle.math.Bounds;
import sparkle.math.Vector2;
import sparkle.physics.ContactListener;
import sparkle.physics.RigidBody;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class Jumper extends Component implements ContactListener {
    private static final Audio sound = ResourceLoader.getAudio("audio/jumper-sound.ogg", 0.25f);
    private PlayerMovement playerMovement;
    private JumperAnimation animation;
    private PlayerAnimation playerAnimation;
    private PlayerHealth playerHealth;
    private RigidBody playerBody;
    private Bounds boucingBounds;
    private GameObject playerLowerHitBox;

    @Override
    protected void start() {
        var player = Objects.requireNonNull(Game.getScene().getGameObjectByName("player"));
        playerHealth = player.getComponent(PlayerHealth.class);
        playerMovement = player.getComponent(PlayerMovement.class);
        playerAnimation = player.getComponent(PlayerAnimation.class);
        animation = super.getComponent(JumperAnimation.class);
        playerBody = player.getComponent(RigidBody.class);
        boucingBounds = new Bounds(super.position, super.size.multiply(1, 0).translateY(1));
        playerLowerHitBox = Game.getScene().getGameObjectByName("playerLowerHitBox");
    }

    @Override
    protected void update() {
        if (animation.getCurrent() == animation.getActivated() && animation.getCurrent().isFinished()) {
            animation.use(animation.getIdle());
        }
        if (playerHealth.isDead()) {
            super.removeComponents(RigidBody.class);
            return;
        }
        if (animation.getCurrent() == animation.getIdle() && Math.round(playerBody.getLinearVelocity().getY()) == 0 && playerLowerHitBox.getBounds().intersectsWith(boucingBounds)) {
            playerMovement.jump(new Vector2(0, -190));
            animation.use(animation.getActivated());
            playerAnimation.getJump().reset();
            playerAnimation.use(playerAnimation.getJump());
            sound.stop();
            sound.play();
        }
    }

    @Override
    public boolean shouldCollide(GameObject contact) {
        if (contact == playerLowerHitBox) {
            return false;
        }
        return ContactListener.super.shouldCollide(contact);
    }
}