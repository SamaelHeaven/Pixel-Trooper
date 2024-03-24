package game.prefabs;

import game.components.Heart;
import sparkle.assets.TextureAtlas;
import sparkle.components.Animation;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.physics.BodyType;
import sparkle.physics.Collider;
import sparkle.physics.RigidBody;
import sparkle.utils.ResourceLoader;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class HeartPrefab extends XMLPrefab {
    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        var texture = ResourceLoader.getTexture("textures/heart.png");
        var animation = new Animation(new TextureAtlas(texture, 2, 3), 0.15f, 0, 5, Animation.INDEFINITE);
        var sprite = new Sprite(gameObject.getPosition(), gameObject.getSize(), animation.getTexture());
        gameObject.addComponent(new RigidBody(BodyType.STATIC, Collider.BOX, 0, 0, 0, 0, true, true));
        gameObject.addComponent(animation);
        gameObject.addComponent(sprite);
        gameObject.addComponent(new Heart());
        gameObject.getSize().set(32);
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "heart";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {}
}