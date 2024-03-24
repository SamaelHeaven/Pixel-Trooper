package game.components.player;

import game.components.Box;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.physics.BodyType;
import sparkle.physics.Collider;
import sparkle.physics.ContactListener;
import sparkle.physics.RigidBody;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class PlayerBullet extends Component implements ContactListener {
    private final static float speed = 500;
    private final Sprite playerSprite;
    private Sprite sprite;
    private GameObject player;
    private boolean loaded;

    public PlayerBullet(Sprite playerSprite) {
        this.playerSprite = playerSprite;
    }

    @Override
    protected void start() {
        super.getGameObject().getTags().add(Box.DESTROY_TAG);
        player = playerSprite.getGameObject();
        var playerMovement = Objects.requireNonNull(player.getComponent(PlayerMovement.class));
        super.position.setX((player.getPosition().getX() + (player.getSize().getX() / 2)) + (playerSprite.isFlippedHorizontally() ? -32 : 20));
        super.position.setY(player.getPosition().getY() + (playerMovement.isCrouched() ? 34 : 24));
        super.size.set(16, 1);
        var body = new RigidBody(BodyType.DYNAMIC, Collider.BOX, 0, 0, 0, 0, true, true);
        body.getLinearVelocity().setX((playerSprite.isFlippedHorizontally() ? -speed : speed));
        super.addComponent(body);
        var texture = ResourceLoader.getTexture("textures/player-bullet.png");
        var size = texture.getSize().multiply(0.5f);
        sprite = new Sprite(super.size.minus(size).divide(2), size, texture);
        sprite.setFlippedHorizontally(playerSprite.isFlippedHorizontally());
        sprite.setAlpha(0);
        sprite.setAnchor(super.position);
        super.addComponent(sprite);
    }

    @Override
    protected void update() {
        if (!loaded) {
            loaded = true;
            return;
        }
        if (sprite.getAlpha() == 0) {
            sprite.setAlpha(1);
        }
        var distanceFromCamera = Game.getScene().getCamera().plus(Game.getSize().divide(2)).minus(super.getGameObject().getBounds().getCenter()).abs();
        if (distanceFromCamera.getX() > (Game.getWidth() / 2.0) * 1.5) {
            Game.getScene().removeGameObject(super.getGameObject());
        }
    }

    @Override
    public boolean shouldCollide(GameObject contact) {
        if (contact.getName().contains("player")) {
            return false;
        }
        return ContactListener.super.shouldCollide(contact);
    }

    @Override
    public void onContactBegin(GameObject contact) {
        if (!Objects.requireNonNull(contact.getComponent(RigidBody.class)).isTrigger()) {
            Game.getScene().removeGameObject(super.getGameObject());
        }
    }
}