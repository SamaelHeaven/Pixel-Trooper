package game.components.buttons;

import sparkle.core.FPSTarget;
import sparkle.core.Game;
import sparkle.core.MouseButton;

public final class FPSTargetButton extends SettingButton {
    private FPSTarget fpsTarget;

    @Override
    protected void start() {
        super.start();
        fpsTarget = new FPSTarget(Integer.parseInt(super.getGameObject().getName().trim()));
    }

    @Override
    protected boolean isActive() {
        return Game.getFPSTarget().equals(fpsTarget);
    }

    @Override
    public void onMouseButtonClicked(MouseButton mouseButton) {
        if (mouseButton == MouseButton.PRIMARY) {
            Game.setFPSTarget(fpsTarget);
        }
    }
}