package game.components;

import game.scenes.LevelScene;
import sparkle.components.Aligner;
import sparkle.core.*;
import sparkle.drawables.Text;
import sparkle.math.Alignment;
import sparkle.math.Vector2;
import sparkle.paints.Color;
import sparkle.physics.ContactListener;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class VictoryTrigger extends Component implements ContactListener {
    private static final Key key = Key.SPACE;
    private static final GamepadButton button = GamepadButton.START;
    private GameObject label;
    private Text labelText;
    private boolean activated;

    @Override
    protected void start() {
        var player = Objects.requireNonNull(Game.getScene().getGameObjectByName("player"));
        label = new GameObject("label");
        label.setZIndex(player.getZIndex() + 1);
        labelText = new Text(label.getPosition());
        labelText.setFont(ResourceLoader.getFont("fonts/joystix-monospace.ttf"));
        labelText.setFill(Color.WHITE);
        labelText.setFontSize(16);
        label.getSize().set(labelText.getSize());
        var aligner = new Aligner(Alignment.TOP_CENTER, player, new Vector2(0, -32));
        label.addComponents(labelText, aligner);
    }

    @Override
    protected void update() {
        labelText.setText("[" + (Gamepad.getGamepads().getFirst().isConnected() ? button.toString() : key.toString()) + "]");
        label.getSize().set(labelText.getSize());
        if (activated && isNextLevelPressed()) {
            LevelScene.goToNextLevel();
        }
    }

    @Override
    public void onContactBegin(GameObject contact) {
        if (contact.getName().equals("player")) {
            activated = true;
            Game.getScene().addGameObject(label);
        }
    }

    @Override
    public void onContactEnd(GameObject contact) {
        if (contact.getName().equals("player")) {
            activated = false;
            Game.getScene().removeGameObject(label);
        }
    }

    private boolean isNextLevelPressed() {
        if (Gamepad.getGamepads().getFirst().isButtonJustPressed(button)) {
            return true;
        }
        return KeyInput.isKeyJustPressed(key);
    }
}