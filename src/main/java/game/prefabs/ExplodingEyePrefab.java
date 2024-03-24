package game.prefabs;

import game.components.ExplodingEye.ExplodingEye;
import game.components.ExplodingEye.ExplodingEyeAnimation;
import game.components.spirit.Spirit;
import sparkle.core.GameObject;
import sparkle.drawables.Sprite;
import sparkle.physics.BodyType;
import sparkle.physics.Collider;
import sparkle.physics.RigidBody;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class ExplodingEyePrefab extends XMLPrefab {
    private static final int DEFAULT_AGGRESSION_DISTANCE = 475;
    private int aggressionDistance = DEFAULT_AGGRESSION_DISTANCE;

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        gameObject.getSize().set(32);
        gameObject.getTags().add(ExplodingEye.IGNORE_COLLISION_TAG);
        gameObject.getTags().add(Spirit.IGNORE_COLLISION_TAG);
        var animation = new ExplodingEyeAnimation();
        var sprite = new Sprite(null, null, animation.getCurrent().getTexture());
        sprite.getPosition().set(gameObject.getPosition());
        sprite.getSize().set(sprite.getSize().multiply(1.5f));
        var explodingEye = new ExplodingEye(aggressionDistance);
        var rigidBody = new RigidBody(BodyType.DYNAMIC, Collider.CIRCLE, 0, 0, 0, 0, true, false);
        gameObject.addComponents(rigidBody, sprite, animation, explodingEye);
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "explodingEye";
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
