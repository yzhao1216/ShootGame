package com.tarena.shoot;

import java.util.Random;

//enemy & flying object
public class Airplane extends FlyingObject implements Enemy {
	private int speed;// steps the AirPlane moves, 2px

	public Airplane() {
		image = ShootGame.airplane; // image
		width = image.getWidth();// width
		height = image.getHeight();// height
		Random ran = new Random();
		x = ran.nextInt(ShootGame.WIDTH - this.width); // x: a random num
		// between 0 to (window
		// width-airplane width)
		// y = 100;
		y = -this.height;
		speed = 2;
	}

	// override method
	public int getScore() {
		return 5;
	}

	// override step() method
	public void step() {
		super.y += speed;
	}

	// override method :out of bounds
	public boolean outOfBounds() {

		return this.y > ShootGame.HEIGHT;
	}
}
