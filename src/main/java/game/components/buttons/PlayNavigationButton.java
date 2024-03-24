package game.components.buttons;

import game.components.RotatingTitle;
import game.scenes.LevelScene;
import game.scenes.MenuScene;
import sparkle.core.Game;
import sparkle.core.MouseButton;

public final class PlayNavigationButton extends NavigationButton {
    @Override
    public void onMouseButtonClicked(MouseButton mouseButton) {
        RotatingTitle.reset();
        MenuScene.reset();
        Game.setScene(new LevelScene(0));
    }
}