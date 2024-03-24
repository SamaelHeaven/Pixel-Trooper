package game.prefabs;

import game.components.Health;
import game.components.turret.Turret;
import game.components.turret.TurretAnimation;
import sparkle.core.GameObject;
import sparkle.drawables.Rectangle;
import sparkle.drawables.Sprite;
import sparkle.math.Vector2;
import sparkle.paints.Color;
import sparkle.physics.BodyType;
import sparkle.physics.Collider;
import sparkle.physics.RigidBody;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class TurretPrefab extends XMLPrefab {
    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        gameObject.getSize().set(32, 64);
        var animation = new TurretAnimation();
        var sprite = new Sprite(gameObject.getPosition().translateY(-10).translateX(-16), animation.getCurrent().getTexture().getSize().multiply(2), animation.getCurrent().getTexture());
        var rigidBody = new RigidBody(BodyType.STATIC, Collider.BOX, 0, 0, 0, 0, true, true);
        sprite.setFlippedHorizontally(true);
        gameObject.addComponent(new Rectangle(gameObject.getPosition().translateY(32), new Vector2(32), new Color(154, 155, 153)));
        gameObject.addComponents(rigidBody, animation, sprite, new Turret(), new Health(4));
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "turret";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
    }
}