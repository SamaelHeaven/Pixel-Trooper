package game.components.player;

import game.components.Health;
import game.components.Timer;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.utils.ResourceLoader;
import sparkle.xml.XMLDocument;

import java.util.Objects;

public final class PlayerDeathHandler extends Component {
    private final XMLDocument gameOverDocument = ResourceLoader.getXMLDocument("xml/scenes/game-over.xml");
    private PlayerStamina playerStamina;
    private Health health;

    @Override
    protected void start() {
        playerStamina = Objects.requireNonNull(super.getComponent(PlayerStamina.class));
        health = Objects.requireNonNull(super.getComponent(Health.class));
    }

    @Override
    protected void update() {
        if (health.getHealth() <= 0) {
            playerStamina.setStamina(0);
            Objects.requireNonNull(Game.getScene().getGameObjectByType(Timer.class)).stop();
            Game.getScene().addGameObjects(gameOverDocument.instantiateGameObjects());
            super.removeComponents(PlayerBulletSpawner.class);
            super.removeComponent(this);
        }
    }
}