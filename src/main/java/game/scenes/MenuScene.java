package game.scenes;

import sparkle.assets.Audio;
import sparkle.components.AudioSource;
import sparkle.core.*;
import sparkle.math.Vector2;
import sparkle.xml.XMLDocument;
import sparkle.xml.XMLScene;

import java.util.Objects;

public final class MenuScene extends XMLScene {
    private static Audio music;

    public MenuScene(XMLDocument xmlDocument) {
        super(xmlDocument);
    }

    public static void reset() {
        music.stop();
        music = null;
    }

    @Override
    protected void initialize() {
        super.initialize();
        if (music == null) {
            var musicGameObject = Objects.requireNonNull(super.getGameObjectByName("music"));
            music = Objects.requireNonNull(musicGameObject.getComponent(AudioSource.class)).getAudio();
        }
        if (music.getState() == Audio.State.STOPPED) {
            music.play();
        }
    }

    @Override
    protected void update() {
        GlobalScene.update();
        super.update();
    }
}