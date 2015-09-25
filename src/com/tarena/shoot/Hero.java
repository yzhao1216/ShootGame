package com.tarena.shoot;

import java.awt.image.BufferedImage;

//FlyingObject only
public class Hero extends FlyingObject {
	private int life;
	private int doubleFire;
	private BufferedImage[] images;
	private int index;// control the frequency of images

	public Hero() {
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = 150;
		y = 400;

		life = 3;
		doubleFire = 0; // Fire = 0, means single fire
		images = new BufferedImage[] { ShootGame.hero0, ShootGame.hero1 };
		index = 0;// change frequency

	}

	// override step() method.
	public void step() {
		if (images.length > 0) {
			image = images[index++ / 10 % images.length]; // �л�ͼƬhero0��hero1
		}
		// index++;
		// int a = index / 10;
		// int b = a % 2;
		// image = images[b];
	}

	// hero shoot bullets,single or double fire
	public Bullet[] shoot() {
		int xStep = this.width / 4;
		int yStep = 20;
		if (doubleFire > 0) {
			Bullet[] bullets = new Bullet[2];
			bullets[0] = new Bullet(x + xStep, y - yStep);
			bullets[1] = new Bullet(x + 3 * xStep, y - yStep);
			doubleFire -= 2;
			return bullets;
		} else {
			Bullet[] bullets = new Bullet[1];
			bullets[0] = new Bullet(x + 2 * xStep, y - yStep);
			return bullets;
		}

	}

	// hero move with mouse,x:mouse-x; y:mouse-y
	public void moveTo(int x, int y) {
		this.x = x - this.width / 2;
		this.y = y - this.height / 2;

	}

	// add life
	public void addLife() {
		life++;
	}

	// return life
	public int getLife() {
		return life;
	}

	// substract life
	public void subtractLife() {
		life--;

	}

	//add life
	public void addDoubleFire() {
		doubleFire += 40;
	}

	// set double fire
	public void setDoubleFire(int doubleFire) {
		this.doubleFire = doubleFire;
	}

	// override method :out of bounds
	public boolean outOfBounds() {

		return false;
	}

	// hero hit objects
	public boolean hit(FlyingObject other) {
		int x1 = other.x - this.width / 2;
		int x2 = other.x + other.width + this.width / 2;
		int y1 = other.y - this.height / 2;
		int y2 = other.y + other.height + this.height / 2;

		int hx = this.x + this.width / 2;
		int hy = this.y + this.height / 2;

		return hx > x1 && hx < x2 && hy > y1 && hy < y2;

	}
}
