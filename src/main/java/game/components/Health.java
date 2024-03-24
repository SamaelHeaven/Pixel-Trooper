package game.components;

import sparkle.core.Component;

public class Health extends Component {
    private float maxHealth;
    private float health;

    public Health(float maxHealth) {
        this.maxHealth = maxHealth;
        setHealth(this.maxHealth);
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        setHealth(health);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = Math.min(health, maxHealth);
    }

    public void takeDamage(float damage) {
        setHealth(health - damage);
    }

    public boolean isDead() {
        return health <= 0;
    }
}