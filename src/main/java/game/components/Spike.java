package game.components;

import game.components.player.PlayerHealth;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.math.Bounds;
import sparkle.physics.ContactListener;
import sparkle.physics.RigidBody;

import java.util.Objects;

public final class Spike extends Component implements ContactListener {
    private String playerHitBoxName;
    private PlayerHealth playerHealth;
    private Bounds bounds;
    private boolean damage;

    @Override
    protected void start() {
        playerHealth = Game.getScene().getGameObjectByType(PlayerHealth.class);
        var sprite = Objects.requireNonNull(super.getComponent(Sprite.class));
        playerHitBoxName = (sprite.isFlippedVertically() ? "playerUpperHitBox" : "playerLowerHitBox");
        if (sprite.isFlippedVertically()) {
            bounds = new Bounds(super.position.getX(), super.position.getY() + super.size.getY(), super.size.getX(), 0);
        } else {
            bounds = new Bounds(super.position.getX(), super.position.getY(), super.size.getX(), 0);
        }
    }

    @Override
    protected void update() {
        if (playerHealth.isDead()) {
            super.removeComponents(RigidBody.class);
            return;
        }
        if (damage) {
            playerHealth.takeDamage(25);
        }
    }

    @Override
    public void onContactBegin(GameObject contact) {
        if (contact.getName().equals(playerHitBoxName) && contact.getBounds().intersectsWith(bounds)) {
            damage = true;
        }
    }

    @Override
    public void onContactEnd(GameObject contact) {
        if (contact.getName().equals(playerHitBoxName)) {
            damage = false;
        }
    }
}