package com.tarena.shoot;

//flyingObject only
public class Bullet extends FlyingObject {

	private int speed = 3;// steps that Bullet moves

	// constructor
	public Bullet(int x, int y) {
		// width = image.getWidth();
		// height = image.getHeight();
		this.x = x;// move alone with hero
		this.y = y;// move alone with hero
		image = ShootGame.bullet;

		// speed = 3;
	}

	// override step() method
	public void step() {
		y -= speed;
	}

	// override method :out of bounds
	public boolean outOfBounds() {

		return this.y < -this.height;

	}
}
