package game.components;

import game.components.player.PlayerHealth;
import sparkle.assets.TextureAtlas;
import sparkle.components.Animation;
import sparkle.core.Component;
import sparkle.core.Game;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.math.Vector2;
import sparkle.physics.ContactListener;
import sparkle.utils.ResourceLoader;

import java.util.Objects;

public final class Heart extends Component implements ContactListener {
    @Override
    public boolean shouldCollide(GameObject contact) {
        if (!contact.getName().equals("player")) {
            return false;
        }
        return ContactListener.super.shouldCollide(contact);
    }

    @Override
    public void onContactBegin(GameObject contact) {
        var health = Objects.requireNonNull(contact.getComponent(PlayerHealth.class));
        if (health.getHealth() == health.getMaxHealth()) {
            return;
        }
        health.heal(25);
        var particle = new GameObject("heartParticle");
        var animation = new Animation(new TextureAtlas(ResourceLoader.getTexture("textures/heart-particle.png"), 2, 6), 0.05f, 0, 11, 1);
        animation.setDestroyOnFinished(true);
        particle.addComponent(animation);
        particle.addComponent(new Sprite(new Vector2(super.position), new Vector2(super.size), animation.getTexture()));
        Game.getScene().removeGameObject(super.getGameObject());
        Game.getScene().addGameObject(particle);
    }
}