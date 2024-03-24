package game.components.player;

import game.components.ExplodingEye.ExplodingEye;
import game.components.ExplodingEye.EyeExplosionHittable;
import game.components.Health;
import sparkle.assets.Audio;
import sparkle.core.GameObject;
import sparkle.core.Time;
import sparkle.drawables.Sprite;
import sparkle.paints.Color;
import sparkle.utils.ResourceLoader;

public final class PlayerHealth extends Health implements EyeExplosionHittable {
    private final static float invincibleDelay = 0.6f;
    private final static float invincibleFlickingDelay = 0.1f;
    private final Audio hitSound = ResourceLoader.getAudio("audio/player-hit-sound.ogg", 0.35f);
    private final Audio healSound = ResourceLoader.getAudio("audio/player-heal-sound.ogg", 0.4f);
    private float invincibleFlickerTimeLeft;
    private float invincibleTimeLeft;
    private boolean invincible;
    private Sprite sprite;

    public PlayerHealth() {
        super(100);
    }

    @Override
    protected void start() {
        sprite = super.getComponent(Sprite.class);
    }

    @Override
    protected void update() {
        invincibleTimeLeft -= Time.getDelta();
        if (invincibleTimeLeft <= 0) {
            invincibleTimeLeft = 0;
            invincible = false;
            sprite.setColor(Color.TRANSPARENT);
            sprite.setAlpha(1);
            return;
        }
        invincibleFlickerTimeLeft -= Time.getDelta();
        if (invincibleFlickerTimeLeft <= 0) {
            invincibleFlickerTimeLeft = invincibleFlickingDelay;
            if (sprite.getColor().equals(Color.TRANSPARENT)) {
                sprite.setColor(new Color(127, 0, 0, 127));
                sprite.setAlpha(0.5f);
                return;
            }
            sprite.setColor(Color.TRANSPARENT);
            sprite.setAlpha(1);
        }
    }

    @Override
    public void takeDamage(float damage) {
        if (invincible) {
            return;
        }
        if (!super.isDead()) {
            hitSound.play();
        }
        super.setHealth(super.getHealth() - damage);
        if (super.isDead()) {
            return;
        }
        invincible = true;
        invincibleTimeLeft = invincibleDelay;
        invincibleFlickerTimeLeft = invincibleFlickingDelay;
    }

    @Override
    public void onHitEyeExplosion(GameObject explodingEye) {
        takeDamage(ExplodingEye.getDamage());
    }

    public void heal(float amount) {
        if (isDead() || super.getHealth() == super.getMaxHealth()) {
            return;
        }
        super.setHealth(getHealth() + amount);
        healSound.play();
    }
}