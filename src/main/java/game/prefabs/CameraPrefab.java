package game.prefabs;

import game.components.CameraManager;
import sparkle.core.GameObject;
import sparkle.xml.XMLElement;
import sparkle.xml.XMLPrefab;

public final class CameraPrefab extends XMLPrefab {
    private float minX;
    private float maxX;
    private float maxY;
    private boolean scrollVertically;

    @Override
    public GameObject instantiate() {
        var gameObject = super.getBlankObject();
        gameObject.addComponent(new CameraManager(minX, maxX, maxY, scrollVertically));
        gameObject.setZIndex(Integer.MIN_VALUE);
        return gameObject;
    }

    @Override
    protected String getTagName() {
        return "camera";
    }

    @Override
    protected void setProperties(XMLElement xmlElement) {
        minX = xmlElement.getNumber("minX");
        maxX = xmlElement.getNumber("maxX");
        maxY = xmlElement.getNumber("maxY");
        scrollVertically = xmlElement.getBoolean("scrollVertically");
    }

    public float getMinX() {
        return minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public boolean isScrollVertically() {
        return scrollVertically;
    }

    public void setScrollVertically(boolean scrollVertically) {
        this.scrollVertically = scrollVertically;
    }
}