package game.prefabs;

import game.components.VictoryTrigger;
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

public final class VictoryTriggerPrefab extends XMLPrefab {
    private Color color;

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        if (color != null && !color.equals(Color.TRANSPARENT)) {
            var rectangle = new Rectangle(null, new Vector2(1), color);
            gameObject.addComponent(new Sprite(gameObject.getPosition(), gameObject.getSize(), rectangle.toTexture()));
        }
        gameObject.addComponent(new RigidBody(BodyType.STATIC, Collider.BOX, 0, 0, 0, 0, true, true));
        gameObject.addComponent(new VictoryTrigger());
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "victoryTrigger";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        color = xmlElement.getColor("color");
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}