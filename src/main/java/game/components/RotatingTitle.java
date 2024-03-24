package game.components;

import sparkle.core.Component;
import sparkle.core.Time;
import sparkle.drawables.Sprite;

import java.util.Objects;

public final class RotatingTitle extends Component {
    private static float rotationSpeed = 10;
    private static float currentRotation;
    private Sprite sprite;

    public static void reset() {
        currentRotation = 0;
    }

    @Override
    protected void start() {
        sprite = Objects.requireNonNull(super.getComponent(Sprite.class));
        sprite.setRotation(currentRotation);
    }

    @Override
    protected void update() {
        var nextRotation = (rotationSpeed * Time.getDelta());
        var maxAngle = 10;
        if (sprite.getRotation() + nextRotation > maxAngle) {
            sprite.setRotation(maxAngle);
            rotationSpeed = -rotationSpeed;
            return;
        }
        if (sprite.getRotation() + nextRotation < -maxAngle) {
            sprite.setRotation(-maxAngle);
            rotationSpeed = -rotationSpeed;
            return;
        }
        sprite.setRotation(sprite.getRotation() + nextRotation);
        currentRotation = sprite.getRotation();
    }
}