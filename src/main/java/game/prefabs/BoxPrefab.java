package game.prefabs;

import game.components.Box;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.physics.BodyType;
import sparkle.physics.RigidBody;
import sparkle.utils.ResourceLoader;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class BoxPrefab extends XMLPrefab {
    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        gameObject.getSize().set(32);
        gameObject.addComponent(new RigidBody(BodyType.STATIC));
        gameObject.addComponent(new Sprite(gameObject.getPosition(), gameObject.getSize(), ResourceLoader.getTexture("textures/box.png")));
        gameObject.addComponent(new Box());
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "box";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {}
}