package game.components.buttons;

import sparkle.components.Bar;
import sparkle.components.MouseHandler;
import sparkle.components.MouseListener;
import sparkle.core.Component;
import sparkle.core.Cursor;
import sparkle.core.Game;
import sparkle.paints.Paint;

import java.util.Objects;

public abstract class SettingButton extends Component implements MouseListener {
    private Bar bar;
    private Paint primary;
    private Paint secondary;

    @Override
    protected void start() {
        bar = Objects.requireNonNull(super.getComponent(Bar.class));
        primary = bar.getForeground();
        secondary = bar.getStroke();
        if (super.getComponent(MouseHandler.class) == null) {
            super.addComponent(new MouseHandler());
        }
    }

    @Override
    protected void update() {
        if (isActive()) {
            bar.setForeground(secondary);
            bar.setTextFill(primary);
            bar.setStroke(primary);
            return;
        }
        bar.setForeground(primary);
        bar.setTextFill(secondary);
        bar.setStroke(secondary);
    }

    @Override
    public void onMouseInside() {
        Game.setCursor((isActive() ? Cursor.DEFAULT : Cursor.HAND));
    }

    @Override
    public void onMouseLeave() {
        Game.setCursor(Cursor.DEFAULT);
    }

    protected abstract boolean isActive();
}