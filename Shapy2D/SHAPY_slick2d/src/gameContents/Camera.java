package gameContents;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

public class Camera {
	
	public static int transX, transY;
	private int mapWidth, mapHeight;
	private static Rectangle viewPort;
	public TiledMap map;
	
	public Camera(TiledMap map, int mapWidth, int mapHeight) {
		
		map = this.map;
		transX = 0;
		transY = 0;
		viewPort = new Rectangle(0, 0, Main.x, Main.y);
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
	}
	
	public void translate (Graphics g, Entity entity) {
		
		if(entity.getX()-Main.x/2+16 < 0)
		    		transX = 0;
		    	else if(entity.getX()+Main.x/2+16 > mapWidth)
		    		transX = -mapWidth+Main.x;
		    	else
		    		transX = (int)-entity.getX()+Main.x/2-16;
		    	if(entity.getY()-Main.y/2+16 < 0)
		    		transY = 0;
		    	else if(entity.getY()+Main.y/2+16 > mapHeight)
		    		transY = -mapHeight+Main.y;
		    	else
		    		transY = (int)-entity.getY()+Main.y/2-16;
		 
		    	g.translate(transX, transY);
		    	viewPort.setX(-transX);
		    	viewPort.setY(-transY);
	}
	
	public static float get_X(){
		return viewPort.getX();
	}
	public static float get_Y(){
		return viewPort.getY();
	}
}
