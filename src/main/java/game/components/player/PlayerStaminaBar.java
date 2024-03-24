package game.components.player;

import sparkle.components.Bar;
import sparkle.core.Component;
import sparkle.core.Game;

import java.util.Locale;
import java.util.Objects;

public final class PlayerStaminaBar extends Component {
    private Bar bar;
    private PlayerStamina playerStamina;

    @Override
    protected void start() {
        bar = super.getComponent(Bar.class);
        var player = Objects.requireNonNull(Game.getScene().getGameObjectByName("player"));
        playerStamina = Objects.requireNonNull(player.getComponent(PlayerStamina.class));
    }

    @Override
    protected void update() {
        bar.setValue(playerStamina.getStamina() / playerStamina.getMaxStamina());
        bar.setText(String.format(Locale.ENGLISH, "Stamina %3s%%", (int) (bar.getValue() * 100)));
    }
}