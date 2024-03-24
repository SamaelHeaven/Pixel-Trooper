package game.components.buttons;

import game.scenes.LevelScene;
import game.scenes.MenuScene;
import sparkle.core.Game;
import sparkle.core.MouseButton;
import sparkle.utils.ResourceLoader;

public final class MenuNavigationButton extends NavigationButton {
    @Override
    public void onMouseButtonClicked(MouseButton mouseButton) {
        if (mouseButton == MouseButton.PRIMARY) {
            if (Game.getScene() instanceof LevelScene) {
                LevelScene.quitToMainMenu();
                return;
            }
            Game.setScene(new MenuScene(ResourceLoader.getXMLDocument("xml/scenes/main-menu.xml")));
        }
    }
}