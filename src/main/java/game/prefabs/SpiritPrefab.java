package game.prefabs;

import game.components.ExplodingEye.ExplodingEye;
import game.components.spirit.Spirit;
import game.components.spirit.SpiritAnimation;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.physics.BodyType;
import sparkle.physics.Collider;
import sparkle.physics.RigidBody;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class SpiritPrefab extends XMLPrefab {
    private static final int DEFAULT_AGGRESSION_DISTANCE = 490;
    private int aggressionDistance = DEFAULT_AGGRESSION_DISTANCE;

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        gameObject.getTags().add(Spirit.IGNORE_COLLISION_TAG);
        gameObject.getTags().add(ExplodingEye.IGNORE_COLLISION_TAG);
        gameObject.getSize().set(64);
        var animation = new SpiritAnimation();
        var sprite = new Sprite(null, animation.getCurrent().getTexture().getSize().multiply(2), animation.getCurrent().getTexture());
        var rigidBody = new RigidBody(BodyType.DYNAMIC, Collider.CIRCLE, 0, 0, 0, 0, true, false);
        var spirit = new Spirit(aggressionDistance);
        gameObject.addComponents(animation, sprite, rigidBody, spirit);
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "spirit";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        aggressionDistance = (int) xmlElement.getNumber("aggressionDistance", DEFAULT_AGGRESSION_DISTANCE);
    }

    public int getAggressionDistance() {
        return aggressionDistance;
    }

    public void setAggressionDistance(int aggressionDistance) {
        this.aggressionDistance = aggressionDistance;
    }
}