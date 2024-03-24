package game.components.player;

import game.components.Health;
import sparkle.components.Bar;
import sparkle.core.Component;
import sparkle.core.Game;

import java.util.Locale;
import java.util.Objects;

public final class PlayerHealthBar extends Component {
    private Bar bar;
    private Health health;

    @Override
    protected void start() {
        bar = super.getComponent(Bar.class);
        var player = Objects.requireNonNull(Game.getScene().getGameObjectByName("player"));
        health = Objects.requireNonNull(player.getComponent(Health.class));
    }

    @Override
    protected void update() {
        bar.setValue(health.getHealth() / health.getMaxHealth());
        bar.setText(String.format(Locale.ENGLISH, "Health  %3s%%", (int) (bar.getValue() * 100)));
    }
}