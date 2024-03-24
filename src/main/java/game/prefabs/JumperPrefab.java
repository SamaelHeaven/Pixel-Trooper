package game.prefabs;

import game.components.jumper.Jumper;
import game.components.jumper.JumperAnimation;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.math.Vector2;
import sparkle.physics.BodyType;
import sparkle.physics.RigidBody;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class JumperPrefab extends XMLPrefab {
    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        var oldPosition = new Vector2(gameObject.getPosition());
        gameObject.addComponent(new RigidBody(BodyType.STATIC));
        gameObject.getSize().set(27, 18);
        gameObject.getPosition().set(gameObject.getPosition().translateY(32 - gameObject.getSize().getY()).translateX((32 - gameObject.getSize().getX()) / 2));
        gameObject.addComponent(new Jumper());
        var animation = new JumperAnimation();
        gameObject.addComponent(new Sprite(oldPosition, new Vector2(32), animation.getCurrent().getTexture()));
        gameObject.addComponent(animation);
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "jumper";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {}
}