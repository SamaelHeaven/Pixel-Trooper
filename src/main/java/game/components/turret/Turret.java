package game.components.turret;

import game.components.ExplodingEye.EyeExplosionHittable;
import game.components.Health;
import game.components.player.PlayerHealth;
import sparkle.assets.Audio;
import sparkle.components.Bar;
import sparkle.core.*;
import sparkle.drawables.Rectangle;
import sparkle.math.Vector2;
import sparkle.paints.Color;
import sparkle.physics.ContactListener;
import sparkle.utils.ResourceLoader;

public final class Turret extends Component implements ContactListener, EyeExplosionHittable {
    private static final double shootDelay = 0.5;
    private static final int attackDistance = 500;
    private final Audio mechanicalSound;
    private final Audio shootSound;
    private final Audio deathSound;
    private TurretAnimation animation;
    private Health health;
    private Rectangle stand;
    private Bar healthBar;
    private GameObject player;
    private PlayerHealth playerHealth;
    private double shootInterval;

    public Turret() {
        mechanicalSound = ResourceLoader.getAudio("audio/turret-mechanical-sound.ogg", 0.3f);
        shootSound = ResourceLoader.getAudio("audio/turret-shoot-sound.ogg", 0.25f);
        deathSound = ResourceLoader.getAudio("audio/turret-death-sound.ogg", 0.4f);
    }

    @Override
    protected void start() {
        animation = super.getComponent(TurretAnimation.class);
        health = super.getComponent(Health.class);
        stand = super.getComponent(Rectangle.class);
        player = Game.getScene().getGameObjectByName("player");
        playerHealth = player.getComponent(PlayerHealth.class);
        initializeHealthBar();
    }

    @Override
    protected void update() {
        if (health.isDead()) {
            updateDeath();
            return;
        }
        if (!isHidden() && !health.isDead()) {
            healthBar.render();
        }
        updateShoot();
        updateShowingState();
        updateIdleState();
        updateHealthBar();
    }

    @Override
    public boolean shouldCollide(GameObject contact) {
        if (health.isDead() || !contact.getName().equals("playerBullet")) {
            return false;
        }
        if (isHidden()) {
            return contact.getBounds().intersectsWith(stand.getBounds());
        }
        return ContactListener.super.shouldCollide(contact);
    }

    @Override
    public void onContactBegin(GameObject contact) {
        if (!isHidden()) {
            health.takeDamage(1);
        }
        Game.getScene().removeGameObject(contact);
    }

    @Override
    public void onHitEyeExplosion(GameObject explodingEye) {
        health.takeDamage(2);
    }

    private void initializeHealthBar() {
        healthBar = new Bar();
        healthBar.setRenderingMode(RenderingMode.WORLD);
        healthBar.setPosition(super.position.translateY(-10));
        healthBar.setSize(new Vector2(super.size.getX(), 3));
        healthBar.setForeground(new Color(224, 55, 29));
        healthBar.setRadius(5);
        updateHealthBar();
    }

    private void updateDeath() {
        if (animation.getCurrent() != animation.getDeath()) {
            deathSound.play();
            animation.use(animation.getDeath());
        }
    }

    private void updateHealthBar() {
        healthBar.setValue(health.getHealth() / health.getMaxHealth());
    }

    private void updateShowingState() {
        var distanceFromPlayer = super.position.plus(super.size.divide(2)).minus(player.getBounds().getCenter()).getX();
        if (distanceFromPlayer <= attackDistance && distanceFromPlayer > 0 && !playerHealth.isDead() && shouldShow()) {
            mechanicalSound.stop();
            mechanicalSound.play();
            animation.use(animation.getShow());
        } else if (((distanceFromPlayer < 0 || distanceFromPlayer > attackDistance) || playerHealth.isDead()) && shouldHide()) {
            mechanicalSound.stop();
            mechanicalSound.play();
            animation.use(animation.getHide());
        }
    }

    private void updateIdleState() {
        if (!isHidden() && !isShooting()) {
            animation.use(animation.getIdle());
        }
    }

    private void updateShoot() {
        if (isShooting() && animation.getCurrent().isFinished()) {
            shootSound.play();
            animation.use(animation.getIdle());
            Game.getScene().addGameObject(new GameObject("turretBullet", new TurretBullet(this.getGameObject())));
        }
        if (isHidden()) {
            return;
        }
        if (!isShooting() && shootInterval <= 0) {
            shootInterval = shootDelay;
            animation.use(animation.getShoot());
        }
        if (isIdle()) {
            shootInterval -= Time.getDelta();
        }
    }

    private boolean isShooting() {
        return animation.getCurrent() == animation.getShoot();
    }

    private boolean isHidden() {
        return animation.getCurrent() == animation.getHide() || (animation.getCurrent() == animation.getShow() && !animation.getCurrent().isFinished());
    }

    private boolean isIdle() {
        return animation.getCurrent() == animation.getIdle();
    }

    private boolean shouldShow() {
        return animation.getCurrent() == animation.getHide() && animation.getCurrent().isFinished();
    }

    private boolean shouldHide() {
        return animation.getCurrent() != animation.getHide() && animation.getCurrent().getIndex() == animation.getCurrent().getBeginIndex();
    }
}