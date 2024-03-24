package game;

import game.prefabs.*;
import game.scenes.MenuScene;
import sparkle.core.*;
import sparkle.utils.ResourceLoader;

import static sparkle.xml.XMLPrefabRepository.addPrefab;

public final class Launcher {
    public static void main(String[] args) {
        addXMLPrefabs();
        Game.run(buildConfig(), buildMainMenuScene());
    }

    private static GameConfig buildConfig() {
        var icon = ResourceLoader.getTexture("textures/box.png");
        var gameConfig = new GameConfig();
        gameConfig.setDisplayMode(DisplayMode.WINDOW);
        gameConfig.setFPSTarget(FPSTarget.FPS_60);
        gameConfig.setHardwareAccelerated(true);
        gameConfig.setTitle("Pixel Trooper");
        gameConfig.setIcon(icon);
        return gameConfig;
    }

    private static Scene buildMainMenuScene() {
        var menuDocument = ResourceLoader.getXMLDocument("xml/scenes/main-menu.xml");
        return new MenuScene(menuDocument);
    }

    private static void addXMLPrefabs() {
        addPrefab(new CameraPrefab());
        addPrefab(new PlayerPrefab());
        addPrefab(new JumperPrefab());
        addPrefab(new BoxPrefab());
        addPrefab(new SpikePrefab());
        addPrefab(new HeartPrefab());
        addPrefab(new VictoryTriggerPrefab());
        addPrefab(new TurretPrefab());
        addPrefab(new ExplodingEyePrefab());
        addPrefab(new SpiritPrefab());
        addPrefab(new BossPrefab());
    }
}