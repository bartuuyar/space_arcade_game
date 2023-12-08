package SpaceInvaders;

import java.awt.Rectangle;

public class BulletE {
    static final int BULLET_SIZE = 5;
    static final int BULLET_SPEED = 3;

    int x;
    int y;

    public BulletE(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += BULLET_SPEED;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, BULLET_SIZE, BULLET_SIZE);
    }
}
