package game.prefabs;

import game.components.Spike;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.math.Vector2;
import sparkle.physics.BodyType;
import sparkle.physics.RigidBody;
import sparkle.utils.ResourceLoader;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class SpikePrefab extends XMLPrefab {
    private boolean flippedVertically;

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        gameObject.getSize().set(12, 26);
        var texture = ResourceLoader.getTexture("textures/spike.png");
        var sprite = new Sprite(new Vector2(gameObject.getPosition()), new Vector2(32), texture);
        sprite.setFlippedVertically(flippedVertically);
        var spriteCenter = sprite.getPosition().plus(sprite.getSize().divide(2));
        var objectCenter = gameObject.getPosition().plus(gameObject.getSize().divide(2));
        var move = spriteCenter.minus(objectCenter);
        gameObject.getPosition().set(gameObject.getPosition().plus(move).translateY((sprite.getSize().getY() - gameObject.getSize().getY()) / 2));
        if (sprite.isFlippedVertically()) {
            gameObject.getPosition().set(gameObject.getPosition().translateY(gameObject.getSize().getY() - sprite.getSize().getY()));
        }
        gameObject.getPosition().set(gameObject.getPosition().round());
        gameObject.addComponent(new RigidBody(BodyType.STATIC));
        gameObject.addComponent(new Spike());
        gameObject.addComponent(sprite);
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "spike";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        flippedVertically = xmlElement.getBoolean("flippedVertically");
    }

    public boolean isFlippedVertically() {
        return flippedVertically;
    }

    public void setFlippedVertically(boolean flippedVertically) {
        this.flippedVertically = flippedVertically;
    }
}