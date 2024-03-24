package game.scenes;

import sparkle.assets.Audio;
import sparkle.components.AudioSource;
import sparkle.core.Game;
import sparkle.core.Scene;
import sparkle.utils.ResourceLoader;
import sparkle.xml.XMLDocument;

import java.util.Objects;

public final class LevelScene extends Scene {
    private static final XMLDocument levelsDocument = ResourceLoader.getXMLDocument("xml/levels/levels.xml");
    private static final int nbLevels = levelsDocument.getRoot().getElementsByName("level").size();
    private static int currentIndex;
    private Audio music;

    public LevelScene(int index) {
        currentIndex = index;
    }

    public static void goToNextLevel() {
        if (!(Game.getScene() instanceof LevelScene levelScene)) {
            throw new IllegalStateException();
        }
        levelScene.music.stop();
        if (currentIndex + 1 >= nbLevels) {
            quitToMainMenu();
            return;
        }
        Game.setScene(new LevelScene(currentIndex + 1));
    }

    public static void replay() {
        var scene = new LevelScene(currentIndex);
        if (!(Game.getScene() instanceof LevelScene levelScene)) {
            throw new IllegalStateException();
        }
        scene.music = levelScene.music;
        Game.setScene(scene);
    }

    public static void quitToMainMenu() {
        if (!(Game.getScene() instanceof LevelScene levelScene)) {
            throw new IllegalStateException();
        }
        levelScene.music.stop();
        Game.setScene(new MenuScene(ResourceLoader.getXMLDocument("xml/scenes/main-menu.xml")));
    }

    @Override
    protected void initialize() {
        var url = levelsDocument.getRoot().getElementsByName("level").get(currentIndex).getURL("url");
        var levelDocument = new XMLDocument(url);
        super.addGameObjects(levelDocument.instantiateGameObjects());
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
        super.updateState();
    }
}