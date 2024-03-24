package game.components.boss;

import game.components.ExplodingEye.ExplodingEye;
import game.components.Health;
import game.components.player.PlayerHealth;
import game.components.spirit.Spirit;
import game.prefabs.ExplodingEyePrefab;
import game.prefabs.SpiritPrefab;
import game.prefabs.VictoryTriggerPrefab;
import sparkle.assets.Audio;
import sparkle.components.Bar;
import sparkle.core.*;
import sparkle.drawables.Sprite;
import sparkle.math.Bounds;
import sparkle.math.Vector2;
import sparkle.math.Vector2c;
import sparkle.paints.Color;
import sparkle.physics.ContactListener;
import sparkle.physics.RigidBody;
import sparkle.utils.ResourceLoader;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class Boss extends Component implements ContactListener {
    private static final Vector2c damageDistance = new Vector2c(192, 96);
    private static final int maxHealth = 40;
    private static final int maxPlayerDistance = 100;
    private static final int damage = 45;
    private final Audio attackSound;
    private final Audio summonSound;
    private final Audio deathSound;
    private GameObject player;
    private PlayerHealth playerHealth;
    private BossAnimation animation;
    private RigidBody rigidBody;
    private Sprite sprite;
    private Bar healthBar;
    private Phase phase;
    private Health health;
    private long nextSummonTime;
    private int summonLeft;
    private boolean summoning;
    private boolean shouldAttack;
    private boolean hasSummoned;

    public Boss() {
        attackSound = ResourceLoader.getAudio("audio/boss-attack-sound.ogg", 0.3f);
        summonSound = ResourceLoader.getAudio("audio/boss-summon-sound.ogg", 0.3f);
        deathSound = ResourceLoader.getAudio("audio/boss-death-sound.ogg", 0.1f);
    }

    @Override
    protected void start() {
        health = new Health(maxHealth);
        player = Game.getScene().getGameObjectByName("player");
        playerHealth = player.getComponent(PlayerHealth.class);
        animation = super.getComponent(BossAnimation.class);
        rigidBody = super.getComponent(RigidBody.class);
        sprite = super.getComponent(Sprite.class);
        super.addComponent(health);
        initializeState();
        initializeHealthBar();
    }

    @Override
    protected void update() {
        animation.getCurrent().update();
        sprite.setTexture(animation.getCurrent().getTexture());
        if (playerHealth.isDead()) {
            if (animation.getCurrent().getIndex() == animation.getCurrent().getEndIndex()) {
                animation.use(animation.getIdle());
            }
            return;
        }
        updateDeath();
        if (animation.getCurrent() == animation.getDeath()) {
            if (animation.getCurrent().isFinished()) {
                onDeath();
            }
            return;
        }
        updatePhase();
        updateAttack();
        updateFlippedHorizontally();
        updateSpritePosition();
        updateHealthBar();
        updateAlpha();
        updateSummon();
    }

    @Override
    protected void fixedUpdate() {
        if (playerHealth.isDead() || animation.getCurrent() == animation.getDeath()) {
            return;
        }
        updateVelocity();
        updateAttackAnimation();
    }

    @Override
    public void onContactBegin(GameObject contact) {
        if (summoning || playerHealth.isDead() || animation.getCurrent() == animation.getDeath()) {
            return;
        }
        if (contact.getName().equals("playerBullet")) {
            health.takeDamage(1);
            Game.getScene().removeGameObject(contact);
        }
    }

    private void initializeHealthBar() {
        healthBar = new Bar();
        healthBar.setSize(new Vector2(300, 40));
        healthBar.setFont(ResourceLoader.getFont("/fonts/joystix-monospace.ttf"));
        healthBar.setStrokeWidth(3);
        healthBar.setFontSize(22);
        healthBar.setRenderingMode(RenderingMode.SCREEN);
        healthBar.setBackground(new Color("#331f1f"));
        healthBar.setForeground(new Color("#8b0000"));
        healthBar.setPosition(new Vector2((Game.getWidth() - healthBar.getSize().getX()) / 2, 10));
        healthBar.setTextFill(Color.WHITE);
        healthBar.setStroke(Color.GRAY);
        var gameObject = new GameObject("bossHealthBar");
        gameObject.addComponent(healthBar);
        Game.getScene().addGameObject(gameObject);
        updateHealthBar();
    }

    private void initializeState() {
        phase = Phase.FIRST;
        nextSummonTime = Time.getTicks();
        summonLeft = 0;
        summoning = false;
        shouldAttack = false;
        hasSummoned = true;
    }

    private void updateVelocity() {
        rigidBody.getLinearVelocity().set(0);
        if (summoning || (animation.getCurrent() == animation.getAttack() && !animation.getCurrent().isFinished())) {
            return;
        }
        var distanceFromPlayer = super.position.minus(player.getBounds().getPosition()).translateY(16).translateX(player.getSize().getX() / 2);
        if (Math.abs(distanceFromPlayer.getX()) > maxPlayerDistance) {
            rigidBody.getLinearVelocity().setX((phase.speed) * -distanceFromPlayer.getModifierX());
        }
        if (animation.getCurrent() != animation.getWalk() && rigidBody.getLinearVelocity().getX() != 0) {
            animation.use(animation.getWalk());
        } else if (animation.getCurrent() == animation.getWalk() && rigidBody.getLinearVelocity().getX() == 0) {
            animation.use(animation.getIdle());
        }
    }

    private void updateSpritePosition() {
        sprite.getPosition().set(super.position.translateX((sprite.isFlippedHorizontally() ? -44 : -188)).translateY(-70));
    }

    private void updateHealthBar() {
        healthBar.setValue(health.getHealth() / health.getMaxHealth());
        healthBar.setText(String.format(Locale.ENGLISH, "Boss      %3s%%", (int) (healthBar.getValue() * 100)));
        if (summoning) {
            healthBar.setStroke(new Color("#5f9ea0"));
            return;
        }
        healthBar.setStroke(Color.GRAY);
    }

    private void updateDeath() {
        if (health.isDead()) {
            if (animation.getCurrent() != animation.getDeath()) {
                deathSound.play();
            }
            animation.use(animation.getDeath());
            updateHealthBar();
        }
    }

    private void updateFlippedHorizontally() {
        if (animation.getCurrent() == animation.getAttack() && !animation.getCurrent().isFinished()) {
            return;
        }
        if (animation.getCurrent() == animation.getSummon()) {
            return;
        }
        var distanceFromPlayer = super.position.plus(super.size.divide(2)).minus(player.getBounds().getPosition().plus(player.getBounds().getSize().divide(2)));
        if (distanceFromPlayer.getModifierX() == -1) {
            sprite.setFlippedHorizontally(true);
        } else if (distanceFromPlayer.getModifierX() == 1) {
            sprite.setFlippedHorizontally(false);
        }
    }

    private void updatePhase() {
        if (health.getHealth() <= phase.maxHealth && animation.getCurrent().getIndex() == animation.getCurrent().getEndIndex()) {
            summonLeft = phase.nbSummon;
            phase = Phase.values()[phase.ordinal() + 1];
            summoning = true;
            shouldAttack = false;
            animation.use(animation.getIdle());
        }
    }

    private void updateAttackAnimation() {
        if (summoning) {
            return;
        }
        if (rigidBody.getLinearVelocity().getX() == 0 && (animation.getCurrent() != animation.getAttack() || animation.getCurrent().isFinished())) {
            animation.getAttack().reset();
            animation.use(animation.getAttack());
            attackSound.play();
        }
    }

    private void updateAttack() {
        int maxFrame = 7;
        int minFrame = 4;
        if (!shouldAttack && animation.getCurrent() == animation.getAttack() && animation.getCurrent().getIndex() >= animation.getCurrent().getBeginIndex() + minFrame && animation.getCurrent().getIndex() < animation.getCurrent().getBeginIndex() + maxFrame) {
            shouldAttack = true;
        }
        if (shouldAttack) {
            var distanceFromPlayer = super.position.plus(super.size.divide(2)).minus(player.getBounds().getPosition().plus(player.getBounds().getSize().divide(2)));
            if (Math.abs(distanceFromPlayer.getX()) <= damageDistance.getX() && Math.abs(distanceFromPlayer.getY()) <= damageDistance.getY()) {
                if (distanceFromPlayer.getModifierX() == (sprite.isFlippedHorizontally() ? -1 : 1)) {
                    playerHealth.takeDamage(damage);
                }
            }
        }
        if (animation.getCurrent() == animation.getAttack() && animation.getCurrent().getIndex() >= animation.getCurrent().getBeginIndex() + maxFrame) {
            shouldAttack = false;
        }
    }

    private void updateAlpha() {
        sprite.setAlpha((summoning ? 0.65f : 1));
    }

    private void updateSummon() {
        if (!summoning) {
            return;
        }
        if (animation.getCurrent() == animation.getSummon() && animation.getCurrent().isFinished()) {
            summon();
            animation.use(animation.getIdle());
        }
        if (Time.getTicks() < nextSummonTime) {
            return;
        }
        if (summonLeft != 0) {
            animation.use(animation.getSummon());
            if (hasSummoned) {
                summonSound.play();
                hasSummoned = false;
            }
            return;
        }
        if (Game.getScene().getGameObjectsByType(ExplodingEye.class).isEmpty() && Game.getScene().getGameObjectsByType(Spirit.class).isEmpty()) {
            summoning = false;
        }
    }

    private void summon() {
        nextSummonTime = Time.getTicks() + TimeUnit.SECONDS.toNanos(3);
        summonLeft--;
        hasSummoned = true;
        var random = ThreadLocalRandom.current();
        if (random.nextBoolean()) {
            summonEyes();
            return;
        }
        summonSpirit();
    }

    private void summonEyes() {
        var random = ThreadLocalRandom.current();
        var positions = new ArrayList<Vector2>();
        loop:
        while (positions.size() < 3) {
            var x = super.position.getX() + random.nextInt(-32, (int) (super.size.getX() + 32 + 1));
            var y = super.position.getY() - random.nextInt(32, 128);
            for (var position : positions) {
                if (new Bounds(position.getX(), position.getY(), 32, 32).intersectsWith(new Bounds(x, y, 32, 32))) {
                    positions.clear();
                    continue loop;
                }
            }
            positions.add(new Vector2(x, y));
        }
        var explodingEyePrefab = new ExplodingEyePrefab();
        for (var position : positions) {
            explodingEyePrefab.setAggressionDistance(Integer.MAX_VALUE);
            explodingEyePrefab.getPosition().set(position);
            Game.getScene().addGameObject(explodingEyePrefab.instantiate());
        }
    }

    private void summonSpirit() {
        var x = super.position.getX();
        var y = super.position.getY() - 80;
        var spiritPrefab = new SpiritPrefab();
        spiritPrefab.setAggressionDistance(Integer.MAX_VALUE);
        spiritPrefab.getPosition().set(x, y);
        Game.getScene().addGameObject(spiritPrefab.instantiate());
    }

    private void onDeath() {
        Game.getScene().removeGameObject(super.getGameObject());
        var victoryTriggerPrefab = new VictoryTriggerPrefab();
        victoryTriggerPrefab.getPosition().set(Float.MIN_VALUE);
        victoryTriggerPrefab.getSize().set(Float.MAX_VALUE);
        victoryTriggerPrefab.setColor(Color.TRANSPARENT);
        Game.getScene().addGameObject(victoryTriggerPrefab.instantiate());
        healthBar.setText("Victory!");
    }

    private enum Phase {
        FIRST(110, (int) (Boss.maxHealth * 0.75), 1), SECOND(130, (int) (Boss.maxHealth * 0.5), 3), THIRD(150, (int) (Boss.maxHealth * 0.25), 5), FOURTH(170, 0, 0);

        private final int speed;
        private final int maxHealth;
        private final int nbSummon;

        Phase(int speed, int maxHealth, int nbSummon) {
            this.speed = speed;
            this.maxHealth = maxHealth;
            this.nbSummon = nbSummon;
        }
    }
}