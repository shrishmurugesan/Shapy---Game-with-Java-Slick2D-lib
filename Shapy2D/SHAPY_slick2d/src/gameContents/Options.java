package gameContents;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.geom.Rectangle;

public class Options extends BasicGameState {
	
	int state;
	public static boolean paused = true;
	ColorShiftEffect E;
	
	// POSITIONS
	float iX, iY, mX, mY;
	// STATS
	boolean fs, vs, fp;
	// IMAGES
	Image logo, title, fscreen, fstat, vsync, vstat, fps, fpsstat, back, on,
			off;
	// POLYGONS
	Rectangle fscreenP, vsyncP, fpsP, backP, mBox;
	
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
	
	public Options(int state) {
		this.state = state;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		paused = false;
		E = new ColorShiftEffect("res/bg", "light", 0.0001f);

		iX = Main.x / 2;
		iY = Main.y / 2;

		// IMAGES
		logo = new Image("res/LOGO.png");
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

		// POLYS
		fscreenP = new Rectangle(0, 0,fscreen.getWidth(),
				fscreen.getHeight());
		vsyncP = new Rectangle(0, 0, fscreenP.getWidth(), fscreenP.getHeight());
		fpsP = new Rectangle(0, 0, fscreenP.getWidth(), fscreenP.getHeight());
		backP = new Rectangle(0, 0, .8f * back.getWidth(),
				.8f * back.getHeight());
		mBox = new Rectangle(0, 0, 5, 5);

		fscreenP.setLocation(iX - (fscreen.getWidth()), iY);
		vsyncP.setLocation(fscreenP.getX(), fscreenP.getMaxY() + 20);
		fpsP.setLocation(vsyncP.getX(), vsyncP.getMaxY() + 20);
		backP.setLocation(fpsP.getX() + (fscreen.getWidth()/2), fpsP.getMaxY() + 20);
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if (!paused) {
			// BACKGROUND
			E.render(0, 0);

			// LOGO
			logo.draw(iX - (logo.getWidth() / 2), 0, 1);

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
		}
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		if (!paused) {
			// BG ANIMATION
			E.update(delta);

			// MOUSE
			Input inp = gc.getInput();
			mX = Mouse.getX();
			mY = Main.y - Mouse.getY();
			mBox.setLocation(mX, mY);

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
					paused = true;
					sbg.enterState(0);
				}
			} else {
				fscreen.setImageColor(1f, 1f, 1f);
				vsync.setImageColor(1f, 1f, 1f);
				fps.setImageColor(1f, 1f, 1f);
				back.setImageColor(1f, 1f, 1f);
			}
		}else{
			if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
				paused = false;
			}
		}
	}
	
	public int getID() {
		return state;
	}
}
