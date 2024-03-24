package game.scenes;

import sparkle.core.Game;
import sparkle.core.Key;
import sparkle.core.KeyInput;

public final class GlobalScene {
    public static void update() {
        updateQuit();
    }

    private static void updateQuit() {
        if (KeyInput.isKeyJustPressed(Key.ESCAPE)) {
            Game.stop();
        }
    }
}