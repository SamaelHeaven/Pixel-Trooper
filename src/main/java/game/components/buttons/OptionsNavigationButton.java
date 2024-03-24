package game.components.buttons;

import game.scenes.MenuScene;
import sparkle.core.Game;
import sparkle.core.MouseButton;
import sparkle.utils.ResourceLoader;

public final class OptionsNavigationButton extends NavigationButton {
    @Override
    public void onMouseButtonClicked(MouseButton mouseButton) {
        if (mouseButton == MouseButton.PRIMARY) {
            Game.setScene(new MenuScene(ResourceLoader.getXMLDocument("xml/scenes/options.xml")));
        }
    }
}