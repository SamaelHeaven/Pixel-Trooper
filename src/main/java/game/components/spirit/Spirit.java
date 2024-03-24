package game.components.spirit;

import game.components.ExplodingEye.ExplodingEye;
import game.components.ExplodingEye.EyeExplosionHittable;
import game.components.Health;
import game.components.player.PlayerHealth;
import sparkle.assets.Audio;
import sparkle.components.Bar;
import sparkle.components.HitBox;
import sparkle.core.*;
import sparkle.drawables.Sprite;
import sparkle.math.Vector2;
import sparkle.math.Vector2c;
import sparkle.paints.Color;
import sparkle.physics.ContactListener;
import sparkle.physics.RigidBody;
import sparkle.prefabs.HitBoxPrefab;
import sparkle.utils.ResourceLoader;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class Spirit extends Component implements ContactListener, EyeExplosionHittable {
    public static final String IGNORE_COLLISION_TAG = "SPIRIT_IGNORE_COLLISION";
    private static final Vector2c speed = new Vector2c(130, 90);
    private static final Vector2c maxPlayerDistance = new Vector2c(16, 5);
    private static final int attackDistance = 50;
    private static final int damageDistance = 128;
    private static final int damage = 40;
    private static final int maxHealth = 6;
    private final Audio laughterSound;
    private final Audio deathSound;
    private final int aggressionDistance;
    private SpiritAnimation animation;
    private Sprite sprite;
    private GameObject player;
    private PlayerHealth playerHealth;
    private Health health;
    private Bar healthBar;
    private GameObject lowerHitBox;
    private RigidBody rigidBody;
    private long nextLaughterTime;

    public Spirit(int aggressionDistance) {
        this.aggressionDistance = aggressionDistance;
        laughterSound = ResourceLoader.getAudio("audio/spirit-laughter-sound.ogg", 0.2f);
        deathSound = ResourceLoader.getAudio("audio/spirit-death-sound.ogg", 0.15f);
    }

    @Override
    protected void start() {
        health = new Health(maxHealth);
        super.addComponent(health);
        animation = super.getComponent(SpiritAnimation.class);
        sprite = super.getComponent(Sprite.class);
        rigidBody = super.getComponent(RigidBody.class);
        player = Game.getScene().getGameObjectByName("player");
        playerHealth = player.getComponent(PlayerHealth.class);
        var hitBoxPrefab = new HitBoxPrefab();
        hitBoxPrefab.setBoxSize(24);
        hitBoxPrefab.setType(HitBox.Type.BOTTOM);
        hitBoxPrefab.setIgnoreTrigger(true);
        hitBoxPrefab.setGameObject(super.getGameObject());
        lowerHitBox = hitBoxPrefab.instantiate();
        Game.getScene().addGameObject(lowerHitBox);
        initializeHealthBar();
        initializeState();
        updateSpritePosition();
    }

    @Override
    protected void update() {
        animation.getCurrent().update();
        sprite.setTexture(animation.getCurrent().getTexture());
        if (health.isDead()) {
            updateDeath();
            return;
        }
        updateLaughter();
        updateAttack();
        updateVelocity();
        updateFlippedHorizontally();
        updateSpritePosition();
        updateHealthBar();
    }

    @Override
    protected void fixedUpdate() {
        updateVelocity();
    }

    @Override
    public void onHitEyeExplosion(GameObject explodingEye) {
        health.takeDamage(2);
    }

    @Override
    public boolean shouldCollide(GameObject contact) {
        var name = contact.getName().toLowerCase(Locale.ENGLISH);
        if (!contact.getComponents(ExplodingEye.class).isEmpty()) {
            return false;
        }
        if (contact.getName().equals("playerBullet")) {
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
        if (contact.getName().equals("playerBullet") && sprite.getAlpha() == 1) {
            health.takeDamage(1);
            Game.getScene().removeGameObject(contact);
        }
    }

    private void initializeHealthBar() {
        healthBar = new Bar();
        healthBar.setRenderingMode(RenderingMode.WORLD);
        healthBar.setSize(new Vector2(super.size.getX() / 2, 3));
        healthBar.setPosition(super.position.translateY(-10).translateX(healthBar.getSize().getX() / 2));
        healthBar.setForeground(new Color(224, 55, 29));
        healthBar.setRadius(5);
    }

    private void initializeState() {
        var random = ThreadLocalRandom.current();
        nextLaughterTime = Time.getTicks() + TimeUnit.SECONDS.toNanos(random.nextInt(5, 40 + 1));
    }

    private void updateVelocity() {
        if (playerHealth.isDead()) {
            rigidBody.getLinearVelocity().set(0);
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

    private void updateLaughter() {
        var random = ThreadLocalRandom.current();
        if (sprite.isOutsideScreen()) {
            nextLaughterTime = Time.getTicks() + TimeUnit.SECONDS.toNanos(random.nextInt(5, 40 + 1));
            return;
        }
        if (Time.getTicks() >= nextLaughterTime) {
            laughterSound.play();
            nextLaughterTime += TimeUnit.SECONDS.toNanos(random.nextInt(5, 40 + 1));
        }
    }

    private void updateSpritePosition() {
        sprite.getPosition().set(super.position.minus(super.size.divide(2)));
    }

    private void updateFlippedHorizontally() {
        var distanceFromPlayer = super.getGameObject().getBounds().getCenter().minus(player.getBounds().getCenter());
        if (distanceFromPlayer.getModifierX() == 1) {
            sprite.setFlippedHorizontally(true);
        } else if (distanceFromPlayer.getModifierX() == -1) {
            sprite.setFlippedHorizontally(false);
        }
    }

    private void updateAttack() {
        if (playerHealth.isDead()) {
            animation.use(animation.getIdle());
            return;
        }
        var distanceFromPlayer = super.getGameObject().getBounds().getCenter().minus(player.getBounds().getCenter());
        if (animation.getCurrent() == animation.getAttack() && animation.getCurrent().isFinished()) {
            if (Math.abs(distanceFromPlayer.getX()) <= damageDistance && Math.abs(distanceFromPlayer.getY()) <= damageDistance) {
                playerHealth.takeDamage(damage);
                animation.use(animation.getIdle());
            }
        }
        if (Math.abs(distanceFromPlayer.getX()) > attackDistance || Math.abs(distanceFromPlayer.getY()) > attackDistance) {
            if ((animation.getCurrent() != animation.getAttack() && animation.getCurrent().getIndex() == animation.getCurrent().getEndIndex()) || animation.getAttack() == animation.getCurrent() && animation.getCurrent().isFinished()) {
                animation.use(animation.getIdle());
            }
            return;
        }
        animation.use(animation.getAttack());
    }

    private void updateHealthBar() {
        healthBar.setPosition(super.position.translateY(-10).translateX(healthBar.getSize().getX() / 2));
        healthBar.setValue(health.getHealth() / health.getMaxHealth());
        healthBar.render();
    }

    private void updateDeath() {
        if (animation.getCurrent() != animation.getDeath()) {
            deathSound.play();
            super.removeComponents(RigidBody.class);
        }
        animation.use(animation.getDeath());
        var newAlpha = sprite.getAlpha() - (Time.getDelta() * 1.25f);
        if (newAlpha <= 0) {
            Game.getScene().removeGameObject(super.getGameObject());
            return;
        }
        sprite.setAlpha(newAlpha);
    }
}