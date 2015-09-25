package com.tarena.shoot;

import java.util.Random;

//bee: both FlyingObject and Award 
public class Bee extends FlyingObject implements Award {
	// move inclined,both x and y axis change
	private int xSpeed; // steps in x axis
	private int ySpeed;// steps in y axis
	private int awardType;// awardType:0 or 1;

	public Bee() {
		image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		Random ran = new Random();
		x = ran.nextInt(ShootGame.WIDTH - this.width); // x: a random num
		// between 0 to (window
		// width-bee width)
		// y = 100;
		y = -this.height;
		xSpeed = 1;
		ySpeed = 2;
		awardType = ran.nextInt(2);// random num between 0-1;
	}

	// override method
	public int getType() {
		// double fire
		return awardType;

	}

	// override step() method
	public void step() {
		x += xSpeed;
		y += ySpeed;
		if (x > ShootGame.WIDTH - this.width) {
			xSpeed = -1;
		}
		if (x < 0) {
			xSpeed = 1;
		}
	}

	// override method :out of bounds
	public boolean outOfBounds() {

		return this.y > ShootGame.HEIGHT;

	}
}
