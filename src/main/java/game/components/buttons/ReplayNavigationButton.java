package game.components.buttons;

import game.scenes.LevelScene;
import sparkle.core.Game;
import sparkle.core.MouseButton;

public final class ReplayNavigationButton extends NavigationButton {
    @Override
    public void onMouseButtonClicked(MouseButton mouseButton) {
        if (mouseButton == MouseButton.PRIMARY) {
            if (Game.getScene() instanceof LevelScene) {
                LevelScene.replay();
            }
        }
    }
}