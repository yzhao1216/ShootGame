package com.tarena.shoot;

import java.awt.image.BufferedImage;

//Flying Object
public abstract class FlyingObject {
	protected BufferedImage image;
	protected int width;// width
	protected int height;// height
	protected int x;// x
	protected int y;// y

	// flying object move one step
	public abstract void step();

	// enemy shot by bullets. this:enemy
	public boolean shootBy(Bullet bullet) {
		int x1 = this.x;
		int x2 = this.x + this.width;
		int y1 = this.y;
		int y2 = this.y + this.height;
		int x = bullet.x;
		int y = bullet.y;

		return x > x1 && x < x2 && y > y1 && y < y2;

	}

	//check out of bounds 
	public abstract boolean outOfBounds();

}
