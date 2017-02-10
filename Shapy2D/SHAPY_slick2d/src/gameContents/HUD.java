package gameContents;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class HUD {
	
	public int score, sCount, time;
	public int width,height;
	
	
	public HUD(int w, int h){
		score = 0;
		sCount = 0;
		width = w;
		height = h;
	}
	
	public void render(StateBasedGame sbg, Graphics g){
		
	}
	
	public void addScore(){
		score +=100;
	}
	
	public void addCount(){
		sCount++;
	}
	
	public void reset(){
		score = 0;
		sCount = 0;
	}
}
