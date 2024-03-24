package game.components.player;

import sparkle.components.HitBox;
import sparkle.core.*;
import sparkle.drawables.Sprite;
import sparkle.math.Bounds;
import sparkle.math.Vector2;
import sparkle.math.Vector2Base;
import sparkle.physics.ContactListener;
import sparkle.physics.RigidBody;
import sparkle.prefabs.HitBoxPrefab;
import sparkle.utils.InputAxis;

import java.util.Objects;

public final class PlayerMovement extends Component implements ContactListener {
    private final Vector2 jumpImpulse;
    private final Vector2 lastPosition;
    private final float maxY;
    private PlayerAnimation animation;
    private RigidBody rigidBody;
    private Sprite sprite;
    private HitBox lowerHitbox;
    private PlayerStamina playerStamina;
    private PlayerHealth health;
    private boolean grounded;
    private boolean loaded;
    private boolean running;
    private boolean jumped;

    public PlayerMovement(float maxY) {
        this.maxY = maxY;
        jumpImpulse = new Vector2(0, -140);
        lastPosition = new Vector2();
    }

    @Override
    public boolean shouldCollide(GameObject contact) {
        if (contact.getName().contains("player")) {
            return false;
        }
        if (health.isDead()) {
            return contact.getBounds().intersectsWith(new Bounds(super.position.plus(super.size).minus(0, 1), new Vector2(size.getX(), 1)));
        }
        return ContactListener.super.shouldCollide(contact);
    }

    @Override
    protected void start() {
        animation = super.getComponent(PlayerAnimation.class);
        var hitBoxPrefab = new HitBoxPrefab();
        hitBoxPrefab.setName("playerLowerHitBox");
        hitBoxPrefab.setGameObject(super.getGameObject());
        hitBoxPrefab.setType(HitBox.Type.BOTTOM);
        hitBoxPrefab.setIgnoreTrigger(true);
        var lowerHitBoxObject = hitBoxPrefab.instantiate();
        lowerHitbox = lowerHitBoxObject.getComponent(HitBox.class);
        Game.getScene().addGameObject(lowerHitBoxObject);
        hitBoxPrefab.setName("playerUpperHitBox");
        hitBoxPrefab.setType(HitBox.Type.TOP);
        Game.getScene().addGameObject(hitBoxPrefab.instantiate());
        rigidBody = Objects.requireNonNull(super.getComponent(RigidBody.class));
        sprite = Objects.requireNonNull(super.getComponent(Sprite.class));
        playerStamina = Objects.requireNonNull(super.getComponent(PlayerStamina.class));
        health = Objects.requireNonNull(super.getComponent(PlayerHealth.class));
        lastPosition.set(super.position);
    }

    @Override
    protected void update() {
        if (!loaded) {
            loaded = true;
            return;
        }
        rigidBody.getLinearVelocity().setX(0);
        grounded = isGrounded();
        handleHasFallen();
        if (health.isDead()) {
            if (grounded) {
                animation.use(animation.getDeath());
            }
            return;
        }
        handleHorizontalMovement();
        handleJump();
        handleStamina();

    }

    @Override
    protected void fixedUpdate() {
        if (health.isDead()) {
            return;
        }
        handleAnimationState();
        lastPosition.set(super.position);
    }

    public void jump(Vector2Base jumpImpulse) {
        if (jumped) {
            return;
        }
        jumped = true;
        rigidBody.addLinearImpulse(jumpImpulse);
        grounded = false;
    }

    public boolean isCrouched() {
        return animation.getCurrent() == animation.getCrouch() && animation.getCurrent().getIndex() != 0;
    }

    private boolean isGrounded() {
        return !lowerHitbox.getColliders().isEmpty() && Math.round(rigidBody.getLinearVelocity().getY()) == 0;
    }

    private void handleHasFallen() {
        if (super.position.getY() > maxY && health.getHealth() > 0) {
            health.setHealth(0);
            super.removeComponents(RigidBody.class);
        }
    }

    private void handleHorizontalMovement() {
        var horizontalAxis = InputAxis.HORIZONTAL.get();
        running = false;
        var speed = getXSpeed();
        if (horizontalAxis == -1) {
            moveLeft(speed);
        }
        if (horizontalAxis == 1) {
            moveRight(speed);
        }
    }

    private void moveLeft(float speed) {
        rigidBody.getLinearVelocity().setX(-(speed));
        sprite.setFlippedHorizontally(true);
    }

    private void moveRight(float speed) {
        rigidBody.getLinearVelocity().setX(speed);
        sprite.setFlippedHorizontally(false);
    }

    private void handleAnimationState() {
        var horizontalAxis = InputAxis.HORIZONTAL.get();
        if (grounded && isCrouchPressed() && horizontalAxis == 0) {
            animation.use(animation.getCrouch());
            return;
        }
        if (Math.round(lastPosition.getX()) == Math.round(position.getX()) && grounded) {
            animation.use(animation.getIdle());
            return;
        }
        if (horizontalAxis != 0 && grounded) {
            animation.use(animation.getRun());
        }
    }

    private void handleJump() {
        jumped = false;
        if (isJumpedPressed() && grounded) {
            jump(jumpImpulse);
            animation.use(animation.getJump());
        }
        if (!grounded && animation.getCurrent() != animation.getJump()) {
            sprite.setTexture(animation.getJump().getTextureAtlas().getTexture(0));
        }
    }

    private void handleStamina() {
        if (running && animation.getCurrent() == animation.getRun()) {
            var runningCost = 40;
            playerStamina.setStamina(playerStamina.getStamina() - (runningCost * Time.getDelta()));
            return;
        }
        if (grounded && (!isRunPressed() || animation.getCurrent() == animation.getIdle())) {
            var regen = 10;
            playerStamina.setStamina(playerStamina.getStamina() + (regen * Time.getDelta()));
        }
    }

    private float getXSpeed() {
        var walkSpeed = 260;
        var runSpeed = walkSpeed + 110;
        if (isRunPressed() && grounded && playerStamina.getStamina() > 0) {
            if (InputAxis.HORIZONTAL.get() != 0) {
                running = true;
            }
            animation.getRun().setDelay(0.1f);
            return runSpeed;
        }
        animation.getRun().setDelay(0.15f);
        return walkSpeed;
    }

    private boolean isRunPressed() {
        return Gamepad.getGamepads().getFirst().isButtonPressed(GamepadButton.X) || KeyInput.isKeyPressed(Key.Z) || KeyInput.isKeyPressed(Key.K);
    }

    private boolean isCrouchPressed() {
        return InputAxis.VERTICAL.get() == 1;
    }

    private boolean isJumpedPressed() {
        return Gamepad.getGamepads().getFirst().isButtonJustPressed(GamepadButton.A) || KeyInput.isKeyJustPressed(Key.UP) || KeyInput.isKeyJustPressed(Key.W) || KeyInput.isKeyJustPressed(Key.SPACE);
    }
}