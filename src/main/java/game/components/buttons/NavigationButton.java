package game.components.buttons;

import sparkle.components.Bar;
import sparkle.components.MouseHandler;
import sparkle.components.MouseListener;
import sparkle.core.Component;
import sparkle.core.Cursor;
import sparkle.core.Game;
import sparkle.paints.Color;
import sparkle.paints.Paint;

import java.util.Objects;

public abstract class NavigationButton extends Component implements MouseListener {
    private static final Paint hoverForeground = new Color(144, 238, 144);
    private static final float hoverStrokeWidth = 5;
    private static final float hoverFontSize = 32;
    private Bar bar;
    private Paint defaultForeground;
    private float defaultStokeWidth;
    private float defaultFontSize;

    @Override
    protected void start() {
        if (super.getComponent(MouseHandler.class) == null) {
            super.addComponent(new MouseHandler());
        }
        bar = Objects.requireNonNull(super.getComponent(Bar.class));
        defaultForeground = bar.getForeground();
        defaultStokeWidth = bar.getStrokeWidth();
        defaultFontSize = bar.getFontSize();
    }

    @Override
    public void onMouseInside() {
        Game.setCursor(Cursor.HAND);
        bar.setForeground(hoverForeground);
        bar.setStrokeWidth(hoverStrokeWidth);
        bar.setFontSize(hoverFontSize);
    }

    @Override
    public void onMouseLeave() {
        Game.setCursor(Cursor.DEFAULT);
        bar.setForeground(defaultForeground);
        bar.setStrokeWidth(defaultStokeWidth);
        bar.setFontSize(defaultFontSize);
    }
}