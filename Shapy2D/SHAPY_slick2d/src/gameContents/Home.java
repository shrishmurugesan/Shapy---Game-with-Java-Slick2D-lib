package gameContents;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.geom.Rectangle;

public class Home extends BasicGameState {

	int state;
	public ColorShiftEffect E;

	// VARIABLES
	static int sound = 1;
	int menu = 0;
	float iX, iY;
	static int screenX, screenY;
	int X, Y;
	float mX, mY;
	// IMAGES
	Image logo, play, resume, options, quit;
	// POLYGONS
	Rectangle playP, resumeP, optionsP, quitP, mBox;

	Main M = new Main();

	boolean originalRes, fs, vs, fp;
	// IMAGES
	Image title, fscreen, fstat, vsync, vstat, fps, fpsstat, back, on, off;
	// POLYGONS
	Rectangle fscreenP, vsyncP, fpsP, backP;
	// GET DEVICE RESOLUTION
	GetResolution resGet;
	// MUSIC
	static Music bgMusic;
	
	// CONSTRUCTOR
	public Home(int state) {
		this.state = state;
	}

	// INIT
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		// DEVICE RESOLUTION
		resGet = new GetResolution();
		X = resGet.width;
		Y = resGet.height;

		// MAIN MENU
		E = new ColorShiftEffect("res/bg", "light", 0.0001f);

		// RESOLUTION SET
		screenX = Main.x;
		screenY = Main.y;

		// IMAGES
		logo = new Image("res/LOGO.png");
		play = new Image("res/buttons/play.png");
		resume = new Image("res/buttons/resume.png");
		options = new Image("res/buttons/options.png");
		quit = new Image("res/buttons/quit.png");

		title = new Image("res/buttons/options.png");
		fscreen = new Image("res/buttons/fscreen.png");
		vsync = new Image("res/buttons/vsync.png");
		fps = new Image("res/buttons/fps.png");
		back = new Image("res/buttons/back.png");
		on = new Image("res/buttons/on.png");
		off = new Image("res/buttons/off.png");

		// STATS
		fs = getStat(1);
		vs = getStat(2);
		fp = getStat(3);
		updateStat();

		// POLYGONS
		playP = new Rectangle(0, 0, play.getWidth(), play.getHeight());
		resumeP = new Rectangle(0, 0, resume.getWidth(), resume.getHeight());
		optionsP = new Rectangle(0, 0, options.getWidth(), options.getHeight());
		quitP = new Rectangle(0, 0, quit.getWidth(), quit.getHeight());
		mBox = new Rectangle(0, 0, 5, 5);

		fscreenP = new Rectangle(0, 0, fscreen.getWidth(), fscreen.getHeight());
		vsyncP = new Rectangle(0, 0, fscreenP.getWidth(), fscreenP.getHeight());
		fpsP = new Rectangle(0, 0, fscreenP.getWidth(), fscreenP.getHeight());
		backP = new Rectangle(0, 0, .8f * back.getWidth(),
				.8f * back.getHeight());
		mBox = new Rectangle(0, 0, 5, 5);

		updateMenu();

		// OPTIONS MENU
		if ((screenX != X) && (screenY != Y))
			originalRes = false;
		else
			originalRes = true;
		
		// MUSIC
		bgMusic = new Music("res/sound/bgmusic_"+sound+".ogg");
		bgMusic.play();

	}

	// UPDATE MENU LOCATIONS
	public void updateMenu() {
		iX = screenX / 2;
		iY = screenY / 2;

		playP.setLocation(iX - (play.getWidth() / 2), iY);
		resumeP.setLocation(playP.getX(), playP.getMaxY() + 20);
		optionsP.setLocation(playP.getX(), resumeP.getMaxY() + 20);
		quitP.setLocation(playP.getX(), optionsP.getMaxY() + 20);

		fscreenP.setLocation(iX - (fscreen.getWidth()), iY);
		vsyncP.setLocation(fscreenP.getX(), fscreenP.getMaxY() + 20);
		fpsP.setLocation(vsyncP.getX(), vsyncP.getMaxY() + 20);
		backP.setLocation(fpsP.getX() + (fscreen.getWidth() / 2),
				fpsP.getMaxY() + 20);
	}

	// RENDER
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// BACKGROUND
		E.render(0, 0);
		// LOGO
		logo.draw(iX - (logo.getWidth() / 2), 0, 1);
		// MENU
		switch (menu) {
		case 0: {
			renderMain(gc, sbg, g);
			break;
		}
		case 1: {
			renderOptions(gc, sbg, g);
			break;
		}
		}
		// DEBUG
		g.drawString("Current soundtrack: "+sound ,screenX - 200,  screenY - 30);
	}

	public void renderMain(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// BUTTONS
		play.draw(playP.getX(), playP.getY());
		resume.draw(resumeP.getX(), resumeP.getY());
		options.draw(optionsP.getX(), optionsP.getY());
		quit.draw(quitP.getX(), quitP.getY());
	}

	public void renderOptions(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// TITLE
		title.draw(iX - (title.getWidth()) / 2, logo.getHeight(), 1);

		// OPTIONS
		fscreen.draw(fscreenP.getX(), fscreenP.getY());
		fstat.draw(fscreenP.getX() + fscreen.getWidth(), fscreenP.getY());

		vsync.draw(vsyncP.getX(), vsyncP.getY());
		vstat.draw(vsyncP.getX() + vsync.getWidth(), vsyncP.getY());

		fps.draw(fpsP.getX(), fpsP.getY());
		fpsstat.draw(fpsP.getX() + fps.getWidth(), fpsP.getY());

		back.draw(backP.getX(), backP.getY());

		if (!originalRes) {
			g.setColor(Color.black);
			g.drawString(
					"Press <X> to switch to your device resolution.(Test mode only! Might not work well!)",
					180, screenY - 30);
		} else {
			g.drawString(
					"Press <X> to switch to default resolution.(Works well!)",
					200, screenY - 60);
		}
	}

	// UPDATE
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if(!bgMusic.playing()){
			sound++;
			if(sound>6){
				sound = 1;
			}
			bgMusic = new Music("res/sound/bgmusic_"+sound+".ogg");
			bgMusic.play();
		}
		
		updateStat();
		// BG ANIMATION
		E.update(delta);

		// MOUSE
		Input inp = gc.getInput();
		mX = Mouse.getX();
		mY = screenY - Mouse.getY();
		mBox.setLocation(mX, mY);

		switch (menu) {
		case 0: {
			updateMain(inp, sbg, delta);
			break;
		}
		case 1: {
			updateOptions(inp, sbg, delta);
		}
		}
	}

	public boolean getStat(int i) {
		switch (i) {
		case 1: {
			if (Main.appgc.isFullscreen())
				return true;
			else
				return false;
		}
		case 2: {
			if (Main.appgc.isVSyncRequested())
				return true;
			else
				return false;
		}
		case 3: {
			if (Main.appgc.isShowingFPS())
				return true;
			else
				return false;
		}
		}
		return false;
	}

	public void updateStat() {
		if (fs) {
			fstat = on;
		} else {
			fstat = off;
		}
		if (vs) {
			vstat = on;
		} else {
			vstat = off;
		}
		if (fp) {
			fpsstat = on;
		} else {
			fpsstat = off;
		}
	}

	public void updateMain(Input inp, StateBasedGame sbg, int delta)
			throws SlickException {
		if (mBox.intersects(playP)) {
			play.setImageColor(.3f, .3f, .3f);
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(1);
			}
		} else if (mBox.intersects(resumeP)) {
			resume.setImageColor(.3f, .3f, .3f);
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(M.maxlvl);
			}
		} else if (mBox.intersects(optionsP)) {
			options.setImageColor(.3f, .3f, .3f);
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				enterMenu("Options");
			}
		} else if (mBox.intersects(quitP)) {
			quit.setImageColor(.3f, .3f, .3f);
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				bgMusic.stop();
				System.exit(0);
			}
		} else {
			play.setImageColor(1f, 1f, 1f);
			resume.setImageColor(1f, 1f, 1f);
			options.setImageColor(1f, 1f, 1f);
			quit.setImageColor(1f, 1f, 1f);
		}
	}

	public void updateOptions(Input inp, StateBasedGame sbg, int delta)
			throws SlickException {
		if (inp.isKeyDown(Input.KEY_X)) {
			if (!originalRes) {
				screenX = X;
				screenY = Y;
				updateMenu();
				Main.appgc.setDisplayMode(screenX, screenY, fs);
				originalRes = !originalRes;
			} else {
				screenX = Main.x;
				screenY = Main.y;
				updateMenu();
				Main.appgc.setDisplayMode(screenX, screenY, fs);
				originalRes = !originalRes;
			}
		}
		if (mBox.intersects(fscreenP)) {
			fscreen.setImageColor(.3f, .3f, .3f);
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				Main.appgc.setFullscreen(!fs);
				fs = !fs;
				updateStat();
			}
		} else if (mBox.intersects(vsyncP)) {
			vsync.setImageColor(.3f, .3f, .3f);
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				Main.appgc.setVSync(!vs);
				vs = !vs;
				updateStat();
			}
		} else if (mBox.intersects(fpsP)) {
			fps.setImageColor(.3f, .3f, .3f);
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				Main.appgc.setShowFPS(!fp);
				fp = !fp;
				updateStat();
			}
		} else if (mBox.intersects(backP)) {
			back.setImageColor(.3f, .3f, .3f);
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				enterMenu("Main");
			}
		} else {
			fscreen.setImageColor(1f, 1f, 1f);
			vsync.setImageColor(1f, 1f, 1f);
			fps.setImageColor(1f, 1f, 1f);
			back.setImageColor(1f, 1f, 1f);
		}
	}

	public void enterMenu(String input) {
		switch (input) {
		case "Main": {
			M.curmenu = 0;
			menu = 0;
			break;
		}
		case "Options": {
			M.curmenu = 1;
			menu = 1;
			break;
		}
		default: {
			M.curmenu = 0;
		}
		}
	}

	// GetID
	public int getID() {
		return state;
	}
}
