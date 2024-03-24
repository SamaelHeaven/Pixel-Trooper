package game.components;

import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.GameObject;
import sparkle.math.Camera;

import java.util.Objects;

public final class CameraManager extends Component {
    private final float minX;
    private final float maxX;
    private final float maxY;
    private final boolean scrollVertically;
    private GameObject player;
    private Camera camera;

    public CameraManager(float minX, float maxX, float maxY, boolean scrollVertically) {
        this.minX = minX;
        this.maxX = maxX;
        this.maxY = maxY;
        this.scrollVertically = scrollVertically;
    }

    @Override
    protected void start() {
        player = Objects.requireNonNull(Game.getScene().getGameObjectByName("player"));
        camera = Game.getScene().getCamera();
        update();
    }

    @Override
    protected void update() {
        camera.centerOn(player);
        camera.set(camera.clampX(minX, maxX));
        if (scrollVertically) {
            if (camera.getY() >= maxY) {
                camera.setY(maxY);
            }
            return;
        }
        camera.setY(maxY);
    }
}