package game.components;

import game.components.ExplodingEye.EyeExplosionHittable;
import sparkle.assets.Audio;
import sparkle.assets.TextureAtlas;
import sparkle.components.Animation;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.physics.ContactListener;
import sparkle.utils.ResourceLoader;

public final class Box extends Component implements ContactListener, EyeExplosionHittable {
    public static final String DESTROY_TAG = "BOX_DESTROY";
    private static final Audio sound = ResourceLoader.getAudio("audio/box-explosion-sound.ogg", 0.4f);
    private boolean hasExploded;

    @Override
    public void onContactBegin(GameObject contact) {
        if (!contact.getTags().contains(DESTROY_TAG)) {
            return;
        }
        explode();
    }

    @Override
    public void onHitEyeExplosion(GameObject explodingEye) {
        explode();
    }

    private void explode() {
        if (hasExploded) {
            return;
        }
        var animation = new Animation(new TextureAtlas(ResourceLoader.getTexture("textures/smoke-ring.png"), 1, 14), 0.0125f, 0, 13, 1);
        animation.setDestroyOnFinished(true);
        var smokeRing = new GameObject("smokeRing");
        smokeRing.getPosition().set(super.position);
        smokeRing.getSize().set(super.size);
        smokeRing.addComponent(animation);
        smokeRing.addComponent(new Sprite(smokeRing.getPosition(), smokeRing.getSize(), animation.getTexture()));
        Game.getScene().removeGameObject(super.getGameObject());
        Game.getScene().addGameObject(smokeRing);
        sound.play();
        hasExploded = true;
    }
}