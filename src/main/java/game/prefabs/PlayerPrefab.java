package game.prefabs;

import game.components.player.*;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.physics.BodyType;
import sparkle.physics.Collider;
import sparkle.physics.RigidBody;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class PlayerPrefab extends XMLPrefab {
    private float maxY;

    @Override
    public GameObject instantiate() {
        super.setName("player");
        var gameObject = super.getBlankObject();
        gameObject.setZIndex(500);
        gameObject.getSize().set(32, 59);
        gameObject.getPosition().set(gameObject.getPosition().translateY(8));
        gameObject.addComponent(new PlayerDeathHandler());
        gameObject.addComponent(new PlayerHealth());
        gameObject.addComponent(new PlayerStamina());
        gameObject.addComponent(new RigidBody(BodyType.DYNAMIC, Collider.BOX, 8.5f, 1, 0, 0, true, false));
        var playerAnimation = new PlayerAnimation();
        gameObject.addComponent(playerAnimation);
        gameObject.addComponent(new PlayerMovement(maxY));
        gameObject.addComponent(new PlayerBulletSpawner());
        var texture = playerAnimation.getCurrent().getTexture();
        var spriteSize = texture.getSize().multiply(2);
        var sprite = new Sprite(gameObject.getSize().minus(spriteSize).divide(2).translateY(3), spriteSize, texture);
        sprite.setAnchor(gameObject.getPosition());
        gameObject.addComponent(sprite);
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "player";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        maxY = xmlElement.getNumber("maxY");
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }
}