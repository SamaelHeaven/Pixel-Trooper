package game.components.buttons;

import sparkle.core.DisplayMode;
import sparkle.core.Game;
import sparkle.core.MouseButton;

import java.util.Locale;

public final class DisplayModeButton extends SettingButton {
    private DisplayMode displayMode;

    @Override
    protected void start() {
        super.start();
        displayMode = DisplayMode.valueOf(super.getGameObject().getName().toUpperCase(Locale.ENGLISH).trim());
    }

    @Override
    protected boolean isActive() {
        return Game.getDisplayMode() == displayMode;
    }

    @Override
    public void onMouseButtonClicked(MouseButton mouseButton) {
        if (mouseButton == MouseButton.PRIMARY) {
            Game.setDisplayMode(displayMode);
        }
    }
}