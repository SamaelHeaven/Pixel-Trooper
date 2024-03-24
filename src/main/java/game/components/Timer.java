package game.components;

import sparkle.components.Bar;
import sparkle.core.Component;
import sparkle.core.Time;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class Timer extends Component {
    private Bar bar;
    private long startTime;
    private boolean stopped;

    @Override
    protected void start() {
        bar = Objects.requireNonNull(super.getComponent(Bar.class));
        startTime = Time.getTicks();
    }

    @Override
    protected void update() {
        if (stopped) {
            return;
        }
        var time = Time.getTicks() - startTime;
        var hours = TimeUnit.NANOSECONDS.toHours(time);
        time -= TimeUnit.HOURS.toNanos(hours);
        var minutes = TimeUnit.NANOSECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toNanos(minutes);
        var seconds = TimeUnit.NANOSECONDS.toSeconds(time);
        bar.setText(String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds));
    }

    public void stop() {
        stopped = true;
    }
}