package com.tarena.shoot;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShootGame extends JPanel {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 654;

	public static final int START = 0; // program startup
	public static final int RUNNING = 1;// program is running
	public static final int PAUSE = 2; // program pause
	public static final int GAMEOVER = 3; // game over
	private int state = 0; // use to record the statfe at this moment
	
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;

	// initiate one hero object,and enemy array and bullet array
	private Hero hero = new Hero(); // one hero
	private FlyingObject[] flyings = {}; // enemies(airplane + bee)
	private Bullet[] bullets = {};// bullets

	// initiate static data in static segment
	static {
		// catch exception
		try {
			// initiate background using imageIO
			background = ImageIO.read(ShootGame.class
					.getResource("background.png"));
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO
					.read(ShootGame.class.getResource("gameover.png"));
			airplane = ImageIO
					.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// create enemy object(airplane or bee)
	public static FlyingObject nextOne() {
		Random ran = new Random();
		int type = ran.nextInt(20);
		if (type == 0) {
			return new Bee();
		} else {
			return new Airplane();
		}

	}

	int flyEnteredIndex = 0;

	// enemy move in every 10ms
	public void enterAction() {
		flyEnteredIndex++;
		if (flyEnteredIndex % 40 == 0) { // 
			FlyingObject obj = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;
		}
	}

	// flying objects move one step every 10ms
	public void stepAction() {
		hero.step();
		for (int i = 0; i < flyings.length; i++) {
			flyings[i].step();
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
	}

	int shootIndex = 0;

	// hero shoot bullets
	public void shootAction() {
		shootIndex++;
		if (shootIndex % 30 == 0) {// move every 300ms(30*10)
			Bullet[] bs = hero.shoot();

			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
			// new bullets store into the bullets array which has been enlarged.
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length,
					bs.length);
			
			
			
		}

	}

	int score = 0;

	// bullets meet enemies
	public void bangAction() {
		for (int i = 0; i < bullets.length; i++) {
			bang(bullets[i]);
		}
	}

	// delete those flying objects out of bounds
	public void outOfBoundsAction() {
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds()) {
				flyingLives[index] = f;
				index++;
			}

		}
		flyings = Arrays.copyOf(flyingLives, index);
		index = 0;
		Bullet[] bulletsLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletsLives[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletsLives, index);
	}

	// check whether game is over
	public void checkGameOverAction() {
		if (isGameOver()) {
			state = GAMEOVER;

		}
	}

	// decide whether game is over
	public boolean isGameOver() {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (hero.hit(f)) {
				hero.setDoubleFire(0);
				hero.subtractLife();
				FlyingObject t = flyings[i];
				flyings[i] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length - 1);
			}
		}
		return hero.getLife() <= 0;
	}

	// one bullet meet all enemies
	public void bang(Bullet b) {

		int index = -1;
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (f.shootBy(b)) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			FlyingObject one = flyings[index];
			if (one instanceof Enemy) {
				Enemy e = (Enemy) one;
				score += e.getScore();

			}
			if (one instanceof Award) {
				Award a = (Award) one;
				int type = a.getType();
				switch (type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;

				}

			}
			// delete shot enemies
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length - 1];
			flyings[flyings.length - 1] = t;
			flyings = Arrays.copyOf(flyings, flyings.length - 1);

		}

	}

	private Timer timer;
	private int intervel = 10;// ms

	// startup program
	public void action() {
		// hero move with mouse
		MouseAdapter l = new MouseAdapter() {

			// mouse move event
			public void mouseMoved(MouseEvent e) {
				if (state == RUNNING) {
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}

			// mouse click event
			public void mouseClicked(MouseEvent e) {
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case GAMEOVER:
					score = 0;
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					break;
				}
			}

			// mouse out of bound
			public void mouseExited(MouseEvent e) {
				if (state == RUNNING) {
					state = PAUSE;
				}
			}

			// mouse move in
			public void mouseEntered(MouseEvent e) {
				if (state == PAUSE) {
					state = RUNNING;
				}
			}

		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);

		timer = new Timer();
		
		
		timer.schedule(new TimerTask() {
			public void run() { // 10ms
				if (state == RUNNING) {
					enterAction();// enemy(airplane+bee) move in
					stepAction();// flying object move one step
					shootAction();// hero shoot bullets
					bangAction(); // check if bullets meet enemies
					outOfBoundsAction();// delete those flying objects that
					// out
					// of
					// bound.
					checkGameOverAction();
				}
				repaint();

			}
		}, intervel, intervel);
	}

	// override method :paint()
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintHero(g);// draw hero
		paintFlyingObjects(g);// draw enemies
		paintBullets(g);// draw bullets
		paintScore(g);
		paintState(g);
	}

	
	public void paintHero(Graphics g) {
		g.drawImage(hero.image, hero.x, hero.y, null);// draw hero
		// object(movable)
	}

	// draw enemy objects(airplane + bee)
	public void paintFlyingObjects(Graphics g) {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.image, f.x, f.y, null);
		}
	}

	// draw bullets objects
	public void paintBullets(Graphics g) {
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.image, b.x, b.y, null);
		}
	}

	// draw score and life
	public void paintScore(Graphics g) {
		g.setColor(new Color(255, 0, 0));
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		g.drawString("Score: " + score, 10, 20);
		g.drawString("Life: " + hero.getLife(), 10, 40);
	}

	// draw state
	public void paintState(Graphics g) {

		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAMEOVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}

	}

	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly"); // create a window(JFrame) object
		ShootGame game = new ShootGame(); // create a JPanel object
		frame.add(game); // add the panel to frame
		frame.setSize(WIDTH, HEIGHT); // set size for the frame
		frame.setAlwaysOnTop(true);// set window always on top
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// program
		// shutdown when
		// window is
		// closed
		frame.setLocationRelativeTo(null);// set the window to the middle of
		// the screen
		frame.setVisible(true);// 1,make window visible 2, call paint() ASAP

		game.action();

	}
}
