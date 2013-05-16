package com.danvegeto.ants;

import java.applet.*;
import java.awt.*;

//Simple Java graphics animation
public class Runner extends Applet implements Runnable {

	private static final long serialVersionUID = 7711322287491354414L;

	private int width, height;

	private Image backbuffer;
	private Graphics backg;
	private Thread t = null;

	private Game game;

	public void init() {
		
		width = getSize().width;
		height = getSize().height;

		backbuffer = createImage(width, height);
		backg = backbuffer.getGraphics();
		backg.setColor(Color.black);

		game = new Game();
	}

	public void start() {

		if (t == null) {
			t = new Thread(this);
			t.start();
		} else {
			synchronized (this) {
				notify();
			}
		}
	}

	private int tileSize() {
		return Math.min(
				getSize().width / Game.cols, 
				getSize().height / Game.rows);
	}

	public void update(Graphics g) {

		game.iterate();

		backbuffer = createImage(width, height);
		backg = backbuffer.getGraphics();
		backg.setColor(Color.black);

		for (int i = 0; i < Game.rows; i++) {
			for (int j = 0; j < Game.cols; j++) {
				Tile t = game.tiles[i][j];
				backg.setColor(t.color());
				backg.fillRect(j * tileSize(), i * tileSize(), tileSize(),
						tileSize());

				if (t.hasFood()) {
					backg.setColor(Color.green);
					int size = (int) (tileSize() * t.food);
					backg.fillOval(j * tileSize(), i * tileSize(), size, size);
				}

				if (t.hasAnt()) {
					backg.setColor(t.ants.get(0).color);
					backg.fillOval(j * tileSize(), i * tileSize(), tileSize(),
							tileSize());
				}
			}
		}

		g.drawImage(backbuffer, 0, 0, this);
	}

	public void paint(Graphics g) {
		update(g);
	}

	public void run() {
		try {
			while (true) {

				repaint();
				Thread.sleep(20); // interval given in milliseconds
			}
		} catch (InterruptedException e) {}
	}
}