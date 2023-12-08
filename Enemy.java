package SpaceInvaders;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Enemy {
	static  int ENEMY_SIZE;
	static  int ENEMY_SPEED;
	int enemyX;
    int enemyY;
    Rectangle r;
    int health;
    Enemy(Rectangle r,int health){
    	this.r = r;
    	this.health = health;
    }
    public void takeDamage() {
    	this.health--;
    }
    public boolean isDead() {
    	if(this.health == 0) {
    		return true;
    	}
		return false;
    }
}
class Enemy1 extends Enemy{
	static  int ENEMY_SIZE = 50;
	static  int ENEMY_SPEED = 1;
	Enemy1(Rectangle r) {
		super(r, 3);
	}
	
}
class Enemy2 extends Enemy {
	static  int ENEMY_SIZE = 50;
	static  int ENEMY_SPEED = 1;
	private  int  canShoot = 1;
	int  isPrintible = 1;
    private List<BulletE> bullets;

    public Enemy2(Rectangle r) {
        super(r, 2);
        bullets = new ArrayList<>();
    }

    public void shoot() {
    	if(canShoot==1) {
    		int bulletX = r.x + (ENEMY_SIZE / 2) - (BulletE.BULLET_SIZE / 2);
            int bulletY = r.y + ENEMY_SIZE;
            BulletE bullet = new BulletE(bulletX, bulletY);
            bullets.add(bullet);
    	}      
    }
    public void makeInvincible() {
    	canShoot = 0;
    	isPrintible = 0;
    }

    public List<BulletE> getBullets() {
        return bullets;
    }
}
class Enemy3 extends Enemy{
	static  int ENEMY_SIZE = 70;
	static  int ENEMY_SPEED = 1;
	Enemy3(Rectangle r) {
		super(r, 6);
	}
	
}
