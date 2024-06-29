package game.prefabs;

import game.components.ExplodingEye.ExplodingEye;
import game.components.boss.Boss;
import game.components.boss.BossAnimation;
import game.components.spirit.Spirit;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.physics.BodyType;
import sparkle.physics.Collider;
import sparkle.physics.RigidBody;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class BossPrefab extends XMLPrefab {
    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        gameObject.getTags().add(ExplodingEye.IGNORE_COLLISION_TAG);
        gameObject.getTags().add(Spirit.IGNORE_COLLISION_TAG);
        gameObject.getSize().set(48, 112);
        var animation = new BossAnimation();
        var sprite = new Sprite(null, animation.getCurrent().getTexture().getSize().multiply(2), animation.getCurrent().getTexture());
        var rigidBody = new RigidBody(BodyType.DYNAMIC, Collider.BOX, 0, 0, 0, 0, true, true);
        var boss = new Boss();
        gameObject.addComponents(animation, sprite, rigidBody, boss);
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "boss";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {}
}
