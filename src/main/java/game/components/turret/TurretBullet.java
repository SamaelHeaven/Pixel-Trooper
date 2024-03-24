package game.components.turret;

import game.components.Box;
import game.components.player.PlayerHealth;
import sparkle.assets.TextureAtlas;
import sparkle.components.Animation;
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

public final class TurretBullet extends Component implements ContactListener {
    private static final int speed = 450;
    private static final int damage = 25;
    private final GameObject turret;
    private Sprite sprite;
    private Animation animation;

    public TurretBullet(GameObject turret) {
        this.turret = turret;
    }

    @Override
    protected void start() {
        super.position.set(turret.getPosition().plus(-8, 15));
        super.size.set(1);
        super.getGameObject().getTags().add(Box.DESTROY_TAG);
        var texture = ResourceLoader.getTexture("textures/turret-bullet.png");
        animation = new Animation(new TextureAtlas(texture, 2, 4), 0.1f, 0, 3, Animation.INDEFINITE);
        sprite = new Sprite(null, animation.getTexture().getSize().multiply(1.25f), animation.getTexture());
        sprite.setFlippedHorizontally(true);
        sprite.getPosition().set(super.position.plus(super.size.minus(sprite.getSize()).divide(2)));
        var rigidBody = new RigidBody(BodyType.DYNAMIC, Collider.BOX, 0, 0, 0, 0, true, true);
        rigidBody.getLinearVelocity().setX(-speed);
        super.getGameObject().addComponents(animation, rigidBody, sprite);
    }

    @Override
    protected void update() {
        sprite.getPosition().set(super.position.plus(super.size.minus(sprite.getSize()).divide(2)).translateX(8));
        animation.update();
        sprite.setTexture(animation.getTexture());
        var distanceFromCamera = Game.getScene().getCamera().plus(Game.getSize().divide(2)).minus(super.getGameObject().getBounds().getCenter()).abs();
        if (distanceFromCamera.getX() > (Game.getWidth() / 2.0) * 1.5) {
            Game.getScene().removeGameObject(super.getGameObject());
        }
    }

    @Override
    public void onContactBegin(GameObject contact) {
        var playerHealth = contact.getComponent(PlayerHealth.class);
        if (playerHealth != null) {
            playerHealth.takeDamage(damage);
        }
        if (!Objects.requireNonNull(contact.getComponent(RigidBody.class)).isTrigger()) {
            Game.getScene().removeGameObject(super.getGameObject());
        }
    }
}