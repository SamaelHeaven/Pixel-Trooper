package game.components.player;

import sparkle.core.Component;

public final class PlayerStamina extends Component {
    private final float maxStamina;
    private float stamina;

    public PlayerStamina() {
        this.maxStamina = 100;
        this.stamina = maxStamina;
    }

    public float getMaxStamina() {
        return maxStamina;
    }

    public float getStamina() {
        return stamina;
    }

    public void setStamina(float stamina) {
        this.stamina = Math.max(0, Math.min(stamina, maxStamina));
    }
}