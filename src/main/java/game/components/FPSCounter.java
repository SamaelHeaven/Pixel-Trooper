package game.components;

import sparkle.components.Bar;
import sparkle.core.Component;
import sparkle.core.Time;

import java.util.Locale;
import java.util.Objects;

public final class FPSCounter extends Component {
    private static final float updateDelay = 0.25f;
    private float updateTimeLeft;
    private Bar bar;

    @Override
    protected void start() {
        bar = Objects.requireNonNull(super.getComponent(Bar.class));
    }

    @Override
    protected void update() {
        updateTimeLeft -= Time.getDelta();
        if (updateTimeLeft <= 0) {
            updateTimeLeft = updateDelay;
            bar.setText(String.format(Locale.ENGLISH, "FPS: %02d", Math.round(Time.getCurrentFPS())));
        }
    }
}