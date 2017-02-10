package gameContents;

import java.awt.Font;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class Play extends BasicGameState {

	// STATE details
	public static boolean paused = true, lost = false;
	public int state;

	// MAP parameters
	public TiledMap map;
	public static int mapHeight, mapWidth;

	// PLAYER initialization
	public Entity player;

	// SCORE STUFF
	public int score, totalScore, lives;
	Image stars, scoreBoard;

	// CAMERA initialization
	public Camera camera;

	// BG PARTICLE object
	public ColorShiftEffect E;
	public Main M = new Main();

	// POSITIONAL variables
	float pauseMenuX, pauseMenuY;
	float mX, mY, mYt;
	int initX, initY;
	Vector2f goalPos[], hudPos;

	// IMAGES initialization
	Image end, hint, alpha, hud1, hud2, hud3, hud4, nextlvl, mmenu, resume,
			options;

	// POLYS initialization
	Rectangle mBox, nextlvlP, mmenuP, resumeP, optionsP, goalP, starsP;

	// FONT
	TrueTypeFont hudFont, resultFont;

	public Play(int state) { // Constructor
		this.state = state;
	}

	public void initGoals() {
		// LEVEL targets
		int n = 6;
		goalPos = new Vector2f[n];
		for (int i = 0; i < n; i++) {
			goalPos[i] = new Vector2f(0, 0);
		}
		goalPos[0].set(2, 26);
		goalPos[1].set(2, 26);
		goalPos[2].set(48, 16);
		goalPos[3].set(1, 26);
		goalPos[4].set(136, 92);
	}

	public void levelHandler(String str) throws SlickException { // Handles
																	// levels
																	// intelligently
		switch (str) {
		case "start": {
			M.curlvl = 1;
			hint = new Image("res/levels/lvl1.png");
			lives = 5;
			break;
		}
		case "next": {
			M.curlvl++;
			if (M.curlvl == 2) {
				hint = new Image("res/levels/lvl2.png");
			}
			lives += 3;
			break;
		}
		case "goto": {
			M.curlvl = 3; // CHEAT
			break;
		}
		case "reset": {
			if (lost) {
				M.curlvl = 1;
				lives = 5;
				lost = false;
			} else {
				lives--;
			}
			break;
		}
		}

		hint.setAlpha(0.6f);

		map = new TiledMap("res/levels/lvl" + M.curlvl + ".tmx");
		// Map size = Tile Size * No. of Tiles
		mapWidth = map.getWidth() * map.getTileWidth();
		mapHeight = map.getHeight() * map.getTileHeight();
		E.setOffset(mapWidth, mapHeight);

		initX = 50;
		initY = 10;

		goalP.setLocation(goalPos[M.curlvl - 1].x * 32,
				goalPos[M.curlvl - 1].y * 32);

		player = new Entity(initX, initY, map, 0, goalP);
		score = 0;
		totalScore = player.totalScore * 100;
		System.out.println("TOTAL SCORE AVAILABLE: " + totalScore);

		camera = new Camera(map, mapWidth, mapHeight);
		stars = new Image("res/results/stars.png");
		paused = false;
	}

	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		E = new ColorShiftEffect("res/bg", "dark", 0.00007f);
		goalP = new Rectangle(0, 0, 32, 32);

		initGoals();
		levelHandler("start");
		scoreBoard = new Image("res/hud/score.png");

		hudPos = new Vector2f(Camera.get_X(), Camera.get_Y());

		pauseMenuX = Home.screenX / 2;
		pauseMenuY = Home.screenY / 3;

		end = new Image("res/lost.png");
		alpha = new Image("res/alpha.png");
		hud1 = new Image("res/hud/tleft.png");
		hud2 = new Image("res/hud/tright.png");
		hud3 = new Image("res/hud/bleft.png");
		hud4 = new Image("res/hud/bright.png");

		nextlvl = new Image("res/buttons/nextlvl.png");
		mmenu = new Image("res/buttons/mainmenu.png");
		resume = new Image("res/buttons/resume.png");
		options = new Image("res/buttons/options.png");

		mBox = new Rectangle(0, 0, 5, 5);
		nextlvlP = new Rectangle(0, 0, nextlvl.getWidth(), nextlvl.getHeight());
		mmenuP = new Rectangle(0, 0, mmenu.getWidth(), mmenu.getHeight());
		resumeP = new Rectangle(0, 0, resume.getWidth(), resume.getHeight());
		optionsP = new Rectangle(0, 0, options.getWidth(), options.getHeight());
		starsP = new Rectangle(0, 0, stars.getWidth(), stars.getHeight());

		starsP.setLocation(pauseMenuX - (stars.getWidth() / 2), 0);
		nextlvlP.setLocation(pauseMenuX - (nextlvl.getWidth() / 2), pauseMenuY);
		resumeP.setLocation(nextlvlP.getX(), nextlvlP.getY());
		mmenuP.setLocation(nextlvlP.getX(), resumeP.getMaxY() + 20);
		optionsP.setLocation(nextlvlP.getX(), mmenuP.getMaxY() + 20);

		Font awtFont1 = new Font("Tohoma", Font.PLAIN, 32);
		resultFont = new TrueTypeFont(awtFont1, true);
		Font awtFont2 = new Font("Veranda", Font.PLAIN, 44);
		hudFont = new TrueTypeFont(awtFont2, true);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.setColor(Color.darkGray);
		g.setFont(resultFont);
		camera.translate(g, player);
		E.render(Camera.get_X(), Camera.get_Y());
		map.render(0, 0);
		player.renderPoints(g);
		if (M.curlvl <= 2) {
			hint.draw(0, 0);
		}
		
		// HUD
		hud1.draw(Camera.get_X(), Camera.get_Y(), 1.5f);
		hud2.draw(Camera.get_X() + (Home.screenX - hud2.getWidth()),
				Camera.get_Y(), 1.5f);
		hud3.draw(Camera.get_X(),
				Camera.get_Y() + (Home.screenY - hud3.getHeight()), 1.5f);
		hud4.draw(Camera.get_X() + (Home.screenX - hud2.getWidth()),
				Camera.get_Y() + (Home.screenY - hud3.getHeight()), 1.5f);
		scoreBoard.draw(hudPos.x, hudPos.y);

		// ALPHA LAYER (darken the screen)
		alpha.draw(Camera.get_X(), Camera.get_Y());
		player.render(g);

		if (paused && !player.gameover && !player.complete) {

			resume.draw(resumeP.getX(), resumeP.getY());
			mmenu.draw(mmenuP.getX(), mmenuP.getY());
			options.draw(optionsP.getX(), optionsP.getY());
		}

		// SCOREBOARD
		g.setFont(hudFont);
		g.drawString("" + score, hudPos.x + 215, hudPos.y + 2);
		g.drawString("" + lives, hudPos.x + 215, hudPos.y + 47);

		if (player.complete) {

			// MENU
			nextlvl.draw(nextlvlP.getX(), nextlvlP.getY());
			mmenu.draw(mmenuP.getX(), mmenuP.getY());
			options.draw(optionsP.getX(), optionsP.getY());

			// RESULTS
			g.setFont(resultFont);
			g.setColor(Color.lightGray);

			stars.draw(starsP.getX(), starsP.getY());
			g.drawString("" + score, starsP.getX() + 380, starsP.getY() + 184);
			g.drawString("" + lives, starsP.getX() + 380, starsP.getY() + 223);
		}

		if (player.gameover) {
			if (lost) {
				// EXTRA ALPHA LAYER
				alpha.draw(Camera.get_X(), Camera.get_Y());
				end.drawCentered(Camera.get_X() + Home.screenX / 2, Camera.get_Y()
						+ Home.screenY / 2);
			}
			g.setColor(Color.white);
			g.drawString("Press <R> to reset!", pauseMenuX + Camera.get_X()
					- 110, Camera.get_Y() + 60);
		}

		// DEBUG
		/*
		 * g.drawString("viewportX: "+Camera.get_X()+"\tviewportY: "+Camera.get_Y
		 * (), Camera.get_X()+50, Camera.get_Y()+120);
		 * g.drawString("mX: "+mBox.getX
		 * ()+"\tmY: "+mBox.getY(),Camera.get_X()+50,Camera.get_Y()+140);
		 * g.drawString
		 * ("pX: "+mmenuP.getX()+"\tpY: "+mmenuP.getY(),Camera.get_X(
		 * )+50,Camera.get_Y()+160);
		 */
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {

		if(!Home.bgMusic.playing()){
			Home.sound++;
			if(Home.sound>6){
				Home.sound = 1;
			}
			Home.bgMusic = new Music("res/sound/bgmusic_"+Home.sound+".ogg");
			Home.bgMusic.play();
		}
		pauseMenuX = Home.screenX / 2;
		pauseMenuY = Home.screenY / 3;

		// MOUSE
		Input inp = gc.getInput();
		mX = Camera.get_X() + Mouse.getX();
		mY = (Camera.get_Y() + Home.screenY) - Mouse.getY();
		mBox.setLocation(mX, mY);

		hudPos.x = Camera.get_X();
		hudPos.y = Camera.get_Y() + Home.screenY - scoreBoard.getHeight();

		starsP.setLocation(Camera.get_X()
				+ (pauseMenuX - (stars.getWidth() / 2)), Camera.get_Y() + 20);
		nextlvlP.setLocation(Camera.get_X() + pauseMenuX
				- (mmenu.getWidth() / 2), Camera.get_Y() + pauseMenuY + 80);
		resumeP.setLocation(nextlvlP.getX(), nextlvlP.getY());
		mmenuP.setLocation(nextlvlP.getX(), resumeP.getMaxY() + 20);
		optionsP.setLocation(nextlvlP.getX(), mmenuP.getMaxY() + 20);

		if (player.complete) {
			// SCORE CALC
			if (score >= .3f * totalScore) {
				stars = new Image("res/results/star1.png");
			}
			if (score >= .6f * totalScore) {
				stars = new Image("res/results/star2.png");
			}
			if (score >= .85f * totalScore) {
				stars = new Image("res/results/star3.png");
			}

			alpha.setAlpha(1);
			paused = true;
			if (mBox.intersects(nextlvlP)) {
				nextlvl.setImageColor(.2f, .2f, .2f);
				if (inp.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
					levelHandler("next");
				}
			} else {
				nextlvl.setImageColor(1f, 1f, 1f);
			}
		}

		if (!paused && !player.gameover) {
			alpha.setAlpha(0);
			E.update(delta);
			player.update(gc, mapWidth, mapHeight, Camera.get_X(),
					Camera.get_Y(), delta);
			mX = Mouse.getX();
			mY = Mouse.getY();
			mYt = Main.y - mY;
			if (gc.getInput().isKeyPressed(Input.KEY_O)) {
				E.setParticle(0, 0);
			}
			if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
				paused = true;
				alpha.setAlpha(1);
			}
			score = player.score * 100;
		}

		if (player.gameover) {
			alpha.setAlpha(1);
			if (lives == 0) {
				lost = true;
			}
			if (inp.isKeyDown(Input.KEY_R)) {
				levelHandler("reset");
			}
		}

		if (paused) {
			if (mBox.intersects(mmenuP)) {
				mmenu.setImageColor(.2f, .2f, .2f);
				if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					levelHandler("start");
					paused = true;
					M.curmenu = 0;
					sbg.enterState(0);
				}
			} else if (mBox.intersects(optionsP)) {
				options.setImageColor(.2f, .2f, .2f);
				if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					M.curmenu = 1;
					sbg.enterState(0);
				}
			} else if (mBox.intersects(resumeP)) {
				resume.setImageColor(.2f, .2f, .2f);
				if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					paused = false;
				}
			} else {
				mmenu.setImageColor(1f, 1f, 1f);
				options.setImageColor(1f, 1f, 1f);
				resume.setImageColor(1f, 1f, 1f);
			}
			if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
				paused = false;
			}
		}
	}

	public int getID() {
		return state;
	}
}
