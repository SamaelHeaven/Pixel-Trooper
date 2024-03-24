package game.components.ExplodingEye;

import game.components.player.PlayerHealth;
import sparkle.assets.Audio;
import sparkle.components.HitBox;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.GameObject;
import sparkle.core.Time;
import sparkle.drawables.Sprite;
import sparkle.math.Vector2;
import sparkle.math.Vector2c;
import sparkle.physics.ContactListener;
import sparkle.physics.RigidBody;
import sparkle.prefabs.HitBoxPrefab;
import sparkle.utils.ResourceLoader;

import java.util.Locale;
import java.util.Objects;

public final class ExplodingEye extends Component implements ContactListener, EyeExplosionHittable {
    public static final String IGNORE_COLLISION_TAG = "EXPLODING_EYE_IGNORE_COLLISION";
    private static final Vector2c speed = new Vector2c(170, 100);
    private static final Vector2c maxPlayerDistance = new Vector2c(5, 10);
    private static final int damage = 30;
    private static final int explosionRadius = 80;
    private static final float blinkDelay = 0.5f;
    private final Audio blinkSound;
    private final Audio explosionSound;
    private final int aggressionDistance;
    private Sprite sprite;
    private ExplodingEyeAnimation animation;
    private GameObject player;
    private PlayerHealth playerHealth;
    private RigidBody rigidBody;
    private Vector2 lastPosition;
    private GameObject lowerHitBox;
    private float blinkTimeLeft;
    private boolean hasExploded;

    public ExplodingEye(int aggressionDistance) {
        this.aggressionDistance = aggressionDistance;
        blinkSound = ResourceLoader.getAudio("audio/exploding-eye-blink-sound.ogg", 0.4f);
        explosionSound = ResourceLoader.getAudio("audio/exploding-eye-explosion-sound.ogg", 0.3f);
    }

    public static int getDamage() {
        return damage;
    }

    @Override
    protected void start() {
        sprite = super.getComponent(Sprite.class);
        animation = super.getComponent(ExplodingEyeAnimation.class);
        player = Game.getScene().getGameObjectByName("player");
        rigidBody = super.getComponent(RigidBody.class);
        playerHealth = player.getComponent(PlayerHealth.class);
        var hitBoxPrefab = new HitBoxPrefab();
        hitBoxPrefab.setBoxSize(super.size.getY());
        hitBoxPrefab.setType(HitBox.Type.BOTTOM);
        hitBoxPrefab.setIgnoreTrigger(true);
        hitBoxPrefab.setGameObject(super.getGameObject());
        lowerHitBox = hitBoxPrefab.instantiate();
        Game.getScene().addGameObject(lowerHitBox);
        initializeState();
        updateSpritePosition();
    }

    @Override
    protected void update() {
        if (animation.getCurrent() == animation.getExplode()) {
            super.removeComponents(RigidBody.class);
        }
        if (shouldBeRemoved()) {
            Game.getScene().removeGameObject(super.getGameObject());
            return;
        }
        updateHittableList();
        updateFlippedHorizontally();
        handleHasNotMoved();
        updateSpritePosition();
    }

    @Override
    protected void fixedUpdate() {
        updateVelocity();
        updateLastPosition();
    }

    @Override
    public boolean shouldCollide(GameObject contact) {
        var name = contact.getName().toLowerCase(Locale.ENGLISH);
        if (!contact.getComponents(ExplodingEye.class).isEmpty()) {
            return false;
        }
        if (name.contains("bullet")) {
            return true;
        }
        if (name.contains("player")) {
            return false;
        }
        if (contact.getTags().contains(IGNORE_COLLISION_TAG)) {
            return false;
        }
        if (Objects.requireNonNull(contact.getComponent(RigidBody.class)).isTrigger()) {
            return false;
        }
        return ContactListener.super.shouldCollide(contact);
    }

    @Override
    public void onContactBegin(GameObject contact) {
        var name = contact.getName().toLowerCase(Locale.ENGLISH);
        if (name.contains("bullet")) {
            onHitByBullet(contact);
        }
    }

    @Override
    public void onHitEyeExplosion(GameObject explodingEye) {
        if (animation.getCurrent() != animation.getExplode()) {
            animation.use(animation.getExplode());
        }
    }

    private boolean shouldBeRemoved() {
        return animation.getCurrent() == animation.getExplode() && animation.getCurrent().isFinished();
    }

    private void onHitByBullet(GameObject bullet) {
        if (animation.getCurrent() == animation.getExplode()) {
            return;
        }
        Game.getScene().removeGameObject(bullet);
        animation.use(animation.getExplode());
        animation.getCurrent().setIndex(animation.getCurrent().getBeginIndex() + 2);
    }

    private void updateSpritePosition() {
        sprite.getPosition().set(super.position.plus(super.size.minus(sprite.getSize()).divide(2)));
        sprite.getPosition().setX(sprite.getPosition().getX() + (sprite.isFlippedHorizontally() ? -27 : 27));
        sprite.getPosition().setY(sprite.getPosition().getY() + 16);
    }

    private void initializeState() {
        blinkTimeLeft = blinkDelay;
        hasExploded = false;
        lastPosition = new Vector2(super.position);
    }

    private void updateHittableList() {
        if (animation.getCurrent() == animation.getExplode() && animation.getCurrent().getIndex() >= animation.getCurrent().getBeginIndex() + 2 && !hasExploded) {
            hasExploded = true;
            explosionSound.play();
            for (var gameObject : Game.getScene()) {
                if (gameObject == super.getGameObject()) {
                    continue;
                }
                for (var eyeExplosionHittable : gameObject.getComponents(EyeExplosionHittable.class)) {
                    Vector2 distanceFromGameObject = super.getGameObject().getBounds().getCenter().minus(gameObject.getBounds().getCenter());
                    if (Math.abs(distanceFromGameObject.getX()) <= explosionRadius && Math.abs(distanceFromGameObject.getY()) <= explosionRadius) {
                        eyeExplosionHittable.onHitEyeExplosion(super.getGameObject());
                    }
                }
            }
        }
    }

    private void updateVelocity() {
        rigidBody.getLinearVelocity().set(0);
        if (playerHealth.isDead() || animation.getCurrent() == animation.getExplode()) {
            return;
        }
        var distanceFromPlayer = super.getGameObject().getBounds().getCenter().minus(player.getBounds().getCenter());
        if (Math.abs(distanceFromPlayer.getX()) > aggressionDistance || Math.abs(distanceFromPlayer.getY()) > aggressionDistance) {
            return;
        }
        rigidBody.getLinearVelocity().set(speed);
        rigidBody.getLinearVelocity().set(rigidBody.getLinearVelocity().multiply(-distanceFromPlayer.getModifierX(), -distanceFromPlayer.getModifierY()));
        if (!shouldGoDown() && rigidBody.getLinearVelocity().getModifierY() == 1) {
            rigidBody.getLinearVelocity().setY(0);
        }
        if (Math.abs(distanceFromPlayer.getX()) <= maxPlayerDistance.getX()) {
            rigidBody.getLinearVelocity().setX(0);
        }
        if (Math.abs(distanceFromPlayer.getY()) <= maxPlayerDistance.getY()) {
            rigidBody.getLinearVelocity().setY(0);
        }
        updateBlink();
    }

    private boolean shouldGoDown() {
        var lowerHitBox = this.lowerHitBox.getComponent(HitBox.class);
        if (lowerHitBox == null) {
            return false;
        }
        if (lowerHitBox.getColliders().isEmpty()) {
            return true;
        }
        for (var collider : lowerHitBox.getColliders()) {
            if (collider.getBounds().intersectsWith(lowerHitBox.getGameObject().getBounds())) {
                return false;
            }
        }
        return true;
    }

    private void updateFlippedHorizontally() {
        if (Math.round(rigidBody.getLinearVelocity().getX()) < 0) {
            sprite.setFlippedHorizontally(true);
        } else if (Math.round(rigidBody.getLinearVelocity().getX()) > 0) {
            sprite.setFlippedHorizontally(false);
        }
    }

    private void updateLastPosition() {
        lastPosition.set(super.position);
    }

    private void handleHasNotMoved() {
        if (!lastPosition.equals(super.position) && animation.getCurrent() == animation.getIdle()) {
            animation.getCurrent().reset();
        }
    }

    private void updateBlink() {
        var distanceFromPlayer = super.getGameObject().getBounds().getCenter().minus(player.getBounds().getCenter());
        if (Math.abs(distanceFromPlayer.getX()) <= explosionRadius && Math.abs(distanceFromPlayer.getY()) <= explosionRadius) {
            blink();
            return;
        }
        blinkTimeLeft = blinkDelay;
        animation.use(animation.getIdle());
    }

    private void blink() {
        if (blinkTimeLeft <= 0) {
            animation.use(animation.getExplode());
            return;
        }
        blinkTimeLeft -= Time.getFixedDelta();
        animation.use(animation.getBlink());
        if (animation.getCurrent().getIndex() == animation.getCurrent().getEndIndex()) {
            if (blinkSound.getState() == Audio.State.STOPPED) {
                blinkSound.play();
            }
        }
    }
}
