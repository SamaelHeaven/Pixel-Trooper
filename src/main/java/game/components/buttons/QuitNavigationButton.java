package game.components.buttons;

import sparkle.core.Game;
import sparkle.core.MouseButton;

public final class QuitNavigationButton extends NavigationButton {
    @Override
    public void onMouseButtonClicked(MouseButton mouseButton) {
        if (mouseButton == MouseButton.PRIMARY) {
            Game.stop();
        }
    }
}