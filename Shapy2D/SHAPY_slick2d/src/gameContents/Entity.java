package gameContents;

import java.io.File;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class Entity {

	TiledMap map;
	public Vector2f pos;
	protected Rectangle box, target;
	protected Image particleImg, sprite, circleImg, circleImgP, triImg, triImgP, squareImg, squareImgP, pentImg, pentImgP,
			allImg;
	
	protected Image goal[]={new Image("res/levels/goal_1.png"),
			new Image("res/levels/goal_2.png"),
			new Image("res/levels/goal_3.png"),
			new Image("res/levels/goal_4.png")};
	
	protected Animation targetAnim;
	
	protected int mode;
	int layers, width, height;
	public int TileSize = 32;
	public boolean complete, gameover,jumping, jready, all[][], circle[][], tri[][],dang[][],
			square[][], pent[][];
	protected float v=0.0f, a = 0.01f, j = 1;
	public boolean debug = false;
	float mX,mY;
	
	// COLLECTIBLE POINTS
	public boolean coll[][];
	Random rand;
	int collAvail;
	protected Image collImg[] = {new Image("res/levels/coll_1.png"),
			new Image("res/levels/coll_2.png"),
			new Image("res/levels/coll_3.png"),
			new Image("res/levels/coll_4.png")};
	protected Animation collAnim;
	public int score,totalScore;
	
	// PARTICLES
	private ParticleSystem system;
	private ConfigurableEmitter emitter;

	public Entity(float x, float y, TiledMap map, int mode, Rectangle goal)
			throws SlickException {
		score = 0;
		totalScore = 0;
		complete = false;
		gameover=false;
		jumping = false;
		jready = true;
		pos = new Vector2f(x, y);
		box = new Rectangle(x, y, TileSize, TileSize);
		circleImg = new Image("res/sprites/circle.png");
		circleImgP = new Image("res/levels/circle.png"); //particle image
		triImg = new Image("res/sprites/tri.png");
		triImgP = new Image("res/levels/tri.png"); //particle image
		squareImg = new Image("res/sprites/square.png");
		squareImgP = new Image("res/levels/square.png"); //particle image
		pentImg = new Image("res/sprites/pent.png");
		pentImgP = new Image("res/levels/pent.png"); //particle image
		allImg = new Image("res/sprites/all.png");
		this.map = map;
		this.mode = mode;
		
		target = goal;
		targetAnim = new Animation(this.goal, 150);
		targetAnim.setPingPong(true);
		
		setSprite();
		
		layers = map.getLayerCount();
		width = map.getWidth();
		height = map.getHeight();
		
		all = new boolean[width][height];
		circle = new boolean[width][height];
		tri = new boolean[width][height];
		square = new boolean[width][height];
		pent = new boolean[width][height];
		dang = new boolean[width][height];
		
		// COLLECTIBLES
		rand = new Random(10);
		collAvail = 3;
		coll = new boolean[width][height];
		collAnim = new Animation(collImg, 80);
		collAnim.setPingPong(true);
		
		// COLLISION MAPS
		for (int i = 0; i < layers; i++) {
			constructCollisionMap(i);
		}
		constructCollisionMap(-1);

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (coll[i][j]) {
					totalScore ++;
				}
			}
		}
	}

	public void constructCollisionMap(int index) {

		boolean tmp;
		switch (index) {
		case 0: {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					//System.out.println("x,y: " + x + "," + y + "TILE LOADED");
					if (map.getTileImage(x, y, index) != null) {
						if(y!=0){
							if(rand.nextInt()<collAvail){
								coll[x][y-1]=true;
							}else{
								coll[x][y-1]=false;
							}
						}
						circle[x][y] = true;
					} else {
						circle[x][y] = false;
					}
				}
			}
			break;
		}
		case 1: {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (map.getTileImage(x, y, index) != null) {
						if(y!=0){
							if(rand.nextInt()<collAvail){
								coll[x][y-1]=true;
							}
						}
						tri[x][y] = true;
					} else {
						tri[x][y] = false;
					}
				}
			}
			break;
		}
		case 2: {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (map.getTileImage(x, y, index) != null) {
						if(y!=0){
							if(rand.nextInt()<collAvail){
								coll[x][y-1]=true;
							}
						}
						square[x][y] = true;
					} else {
						square[x][y] = false;
					}
				}
			}
			break;
		}
		case 3: {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (map.getTileImage(x, y, index) != null) {
						if(y!=0){
							if(rand.nextInt()<collAvail){
								coll[x][y-1]=true;
							}else{
								coll[x][y-1]=false;
							}
						}
						pent[x][y] = true;
					} else {
						pent[x][y] = false;
					}
				}
			}
			break;
		}
		case 4: {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (map.getTileImage(x, y, index) != null) {
						dang[x][y] = true;
					} else {
						dang[x][y] = false;
					}
				}
			}
			break;
		}
		case -1: {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					tmp = circle[x][y] || tri[x][y] || square[x][y] || pent[x][y];
					if (tmp) {
						all[x][y] = true;
					} else {
						all[x][y] = false;
					}
				}
			}
			break;
		}
		}
		
		//MAKE SURE NO OVERLAPS ARE PRESENT
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (all[x][y] && coll[x][y]) {
					coll[x][y] = false;
				}
			}
		}
	}

	public boolean collides(Vector2f bx, int mode) {
		Vector2f tl[];
		tl = new Vector2f[4];
		tl[0] = bx; // TOP LEFT
		tl[1] = new Vector2f(tl[0].x + TileSize, tl[0].y); // TOP RIGHT
		tl[2] = new Vector2f(tl[0].x, tl[0].y + TileSize); // BOTTOM LEFT
		tl[3] = new Vector2f(tl[1].x, tl[2].y); // BOTTOM RIGHT

		boolean result = false;
		switch (mode) {
		case 0: {
			for (int i = 0; i < 4; i++) {
				if (circle[(int) tl[i].x / TileSize][(int) tl[i].y / TileSize]) {
					if (box.intersects(new Rectangle(((int) tl[i].x / TileSize)
							* TileSize, ((int) tl[i].y / TileSize) * TileSize,
							TileSize, TileSize))) {
						result = true;
					}
				}
			}
			break;
		}
		case 1: {
			for (int i = 0; i < 4; i++) {
				if (tri[(int) tl[i].x / TileSize][(int) tl[i].y / TileSize]) {
					if (box.intersects(new Rectangle(((int) tl[i].x / TileSize)
							* TileSize, ((int) tl[i].y / TileSize) * TileSize,
							TileSize, TileSize))) {
						result = true;
					}
				}
			}
			break;
		}
		case 2: {
			for (int i = 0; i < 4; i++) {
				if (square[(int) tl[i].x / TileSize][(int) tl[i].y / TileSize]) {
					if (box.intersects(new Rectangle(((int) tl[i].x / TileSize)
							* TileSize, ((int) tl[i].y / TileSize) * TileSize,
							TileSize, TileSize))) {
						result = true;
					}
				}
			}
			break;
		}
		case 3: {
			for (int i = 0; i < 4; i++) {
				if (pent[(int) tl[i].x / TileSize][(int) tl[i].y / TileSize]) {
					if (box.intersects(new Rectangle(((int) tl[i].x / TileSize)
							* TileSize, ((int) tl[i].y / TileSize) * TileSize,
							TileSize, TileSize))) {
						result = true;
					}
				}
			}
			break;
		}
		case 4: {
			for (int i = 0; i < 4; i++) {
				if (dang[(int) tl[i].x / TileSize][(int) tl[i].y / TileSize]) {
					if (box.intersects(new Rectangle(((int) tl[i].x / TileSize)
							* TileSize, ((int) tl[i].y / TileSize) * TileSize,
							TileSize, TileSize))) {
						result = true;
					}
				}
			}
			break;
		}
		case -1: {
			for (int i = 0; i < 4; i++) {
				if (all[(int) tl[i].x / TileSize][(int) tl[i].y / TileSize]) {
					if (box.intersects(new Rectangle(((int) tl[i].x / TileSize)
							* TileSize, ((int) tl[i].y / TileSize) * TileSize,
							TileSize, TileSize))) {
						result = true;
					}
				}
			}
			break;
		}
		case -2:{
			for (int i = 0; i < 4; i++) {
				if (coll[(int) tl[i].x / TileSize][(int) tl[i].y / TileSize]) {
					if (box.intersects(new Rectangle(((int) tl[i].x / TileSize)
							* TileSize, ((int) tl[i].y / TileSize) * TileSize,
							TileSize, TileSize))) {
						result = true;
						coll[(int) tl[i].x / TileSize][(int) tl[i].y / TileSize] = false;
					}
				}
			}
			break;
		}
		}
		return result;
	}

	public void update(GameContainer gc, int mapWidth, int mapHeight,float CamX, float CamY, int delta) {
		Vector2f trans = new Vector2f(0, 0);
		Input input = gc.getInput();
		mX = Mouse.getX();
		mY = Main.y-Mouse.getY();
		
		//PARTICLE SYSTEM UPDATE
		system.update(delta);

		if (jready && input.isKeyPressed(Input.KEY_UP)) {
			jready = false;
			if (!jumping) {
				j = 1;
				jumping = true;
			}
		}
		
		//DEBUGGING
		if(input.isKeyPressed(Input.KEY_RSHIFT)){
			debug = !debug;
		}
		if(debug){
			//CHEAT
			if (gc.getInput().isKeyPressed(Input.KEY_W)) {
				complete = true;
			}
		}
		
		//MOVEMENT INPUTS
		if (input.isKeyDown(Input.KEY_LEFT)) {
			if (!jready)
				trans.x = -0.4f * delta;
			else
				trans.x = -0.5f * delta;
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			if (!jready)
				trans.x = 0.4f * delta;
			else
				trans.x = 0.5f * delta;
		}

		if (input.isKeyPressed(Input.KEY_SPACE)) {
			setMode(-1);
			setSprite();
		}
		
		//JUMP CODE
		if (jumping) {
			trans.y = (-2.0f/j) * delta; //divide by j to decelerate jump speed as it reaches the max height
			if (j >= 10) {
				j = 1;
				jumping = false;
			} else {
				j += 0.01f *delta;
			}
		}
		
		//GRAVITY
		v+=a/2; //velocity = initial velocity + cumulative acceleration
		trans.y += (v + 0.4f * delta);
		
		if (pos.x + trans.x > 0 && pos.x + trans.x < (mapWidth - TileSize)) {
			pos.x += trans.x;
			box.setLocation(pos);
			//GAMEOVER situation
			if(collides(pos,4)){
				gameover=true;
			}
			//COLLECTIBLES handling
			if(collides(pos,-2)){
				score++;
			}
			jready = false;
			if (collides(pos, mode)) {
				j=10;
				jumping = false;
				v = a;
				if(!jready)
					jready = true;
				pos.x -= trans.x;
			}
		}
		if (pos.y + trans.y > 0 && pos.y + trans.y < (mapHeight - TileSize)) {
			pos.y += trans.y;
			box.setLocation(pos);
			if(collides(pos,4)){
				gameover=true;
			}
			jready = false;
			if (collides(pos, mode)) {
				j=10;
				jumping = false;
				v = a;
				if(!jready)
					jready = true;
				pos.y -= trans.y;
			}
		}
		
		if (trans.x != 0 && trans.y != 0) {
			trans.set(trans.x / 1.5f, trans.y / 1.5f);
		}
		trans.y += 0.1f * delta;
		
		if(debug && input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
			pos.set(CamX+mX, CamY+mY);
		}
		
		box.setLocation(pos);
		emitter.setPosition(pos.x+16, pos.y+16, false);
		
		//GOAL
		if(box.intersects(target)){
			complete = true;
		}
	}

	public void renderPoints(Graphics g){

		g.drawAnimation(targetAnim, target.getX(), target.getY());
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(coll[x][y]){
					g.drawAnimation(collAnim, x*32, y*32);
				}
			}
		}
		
	}
	
	public void render(Graphics g) {
		
		sprite.draw(pos.x, pos.y);
		
		// DEBUG INFO
		if(debug){
		Rectangle t1 = new Rectangle(((int) pos.x / 32) * 32,
				((int) pos.y / 32) * 32, 32, 32);
		Rectangle t2 = new Rectangle(((int) pos.x / 32) * 32 + 32,
				((int) pos.y / 32) * 32, 32, 32);
		Rectangle t3 = new Rectangle(((int) pos.x / 32) * 32,
				((int) pos.y / 32) * 32 + 32, 32, 32);
		Rectangle t4 = new Rectangle(((int) pos.x / 32) * 32 + 32,
				((int) pos.y / 32) * 32 + 32, 32, 32);
		
		g.draw(box);
		g.draw(t1);
		g.draw(t2);
		g.draw(t3);
		g.draw(t4);
		}
		
		system.render();
	}

	public void setMode(int m) {
		if (m == -1) {
			mode++;
		} else {
			mode = m;
		}
		if (mode > 3) {
			mode = 0;
		}
		setSprite();
	}

	public void setSprite() {
		switch (mode) {
		case 0: {
			sprite = circleImg;
			particleImg = circleImgP;
			break;
		}
		case 1: {
			sprite = triImg;
			particleImg = triImgP;
			break;
		}
		case 2: {
			sprite = squareImg;
			particleImg = squareImgP;
			break;
		}
		case 3: {
			sprite = pentImg;
			particleImg = pentImgP;
			break;
		}
		case -1: {
			sprite = allImg;
			particleImg = allImg;
			break;
		}
		}
		setParticle();
	}

	public void setParticle(){
		try {
			system = new ParticleSystem(particleImg, 1500);

			File xmlFile = new File("res/particles/sprite_emitter.xml");
			emitter = ParticleIO.loadEmitter(xmlFile);
			emitter.setPosition(pos.x+16, pos.y+16);
			system.addEmitter(emitter);
		} catch (Exception e) {
			System.exit(0);
		}
		system.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
	}

	public Vector2f getPos() {
		return pos;
	}

	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}

	public void setPos(Vector2f pos) {
		this.pos = pos;
	}

	public Rectangle getBox() {
		return box;
	}

	public void setBox(Rectangle box) {
		this.box = box;
	}
}
