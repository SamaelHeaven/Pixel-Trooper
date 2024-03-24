package game.components;

import sparkle.assets.Audio;
import sparkle.components.Aligner;
import sparkle.components.Bar;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.MouseButton;
import sparkle.core.MouseInput;
import sparkle.drawables.Sprite;
import sparkle.math.Vector2;

import java.util.Objects;

public final class VolumeSlider extends Component {
    private Bar bar;
    private Aligner aligner;
    private boolean activated;

    @Override
    protected void start() {
        var oval = Objects.requireNonNull(Game.getScene().getGameObjectByName("volumeSliderOval"));
        var sprite = Objects.requireNonNull(oval.getComponent(Sprite.class));
        bar = Objects.requireNonNull(super.getComponent(Bar.class));
        sprite.setAnchor(new Vector2(-(sprite.getSize().getX() / 2), 0));
        aligner = Objects.requireNonNull(oval.getComponent(Aligner.class));
        var value = Audio.getGlobalVolume();
        bar.setValue(value);
        aligner.getOffset().setX(value * super.size.getX());
    }

    @Override
    protected void update() {
        if (!MouseInput.isMousePressed(MouseButton.PRIMARY)) {
            activated = false;
            return;
        }
        var mousePosition = MouseInput.getMousePosition();
        if (super.getGameObject().getBounds().contains(mousePosition)) {
            activated = true;
        }
        if (activated) {
            aligner.getOffset().set(mousePosition.minus(super.position).multiply(1, 0).clampX(0, super.size.getX()));
        }
        var value = aligner.getOffset().getX() / super.size.getX();
        bar.setValue(value);
        Audio.setGlobalVolume(value);
    }
}