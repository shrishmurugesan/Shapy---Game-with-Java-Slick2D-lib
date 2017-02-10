package gameContents;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Main extends StateBasedGame{
	
	//GAME CONTAINER
	public static AppGameContainer appgc;
	
	//RESOLUTION
	public static int x=1280,y=720;
	
	//GAMENAME
	static String gamename="SHAPY v1.0 Final";
	
	//STATES
	public int curlvl,curmenu =0, maxlvl=1, home=0, play=1;
	
	Main(String gamename){
		super(gamename);
		this.addState(new Home(home));
		this.addState(new Play(play));
	}
	
	public Main() {
		super(gamename);// TODO Auto-generated constructor stub
	}
	
	public void initStatesList(GameContainer gc) throws SlickException{
		this.getState(home).init(gc, this);
		this.getState(play).init(gc, this);
		this.enterState(home);
	}
	
	public static void main(String[] args) {
		
		try{
			appgc = new AppGameContainer(new Main(gamename));
			appgc.setDisplayMode(x, y, false);
			appgc.setVSync(true);
			appgc.setShowFPS(false);
			appgc.setUpdateOnlyWhenVisible(true);
			appgc.setSmoothDeltas(true);
			appgc.start();
		}catch(SlickException e){
			e.printStackTrace();
		}
	}

}
