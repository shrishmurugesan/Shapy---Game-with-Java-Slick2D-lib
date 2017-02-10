package gameContents;

import java.io.File;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class ColorShiftEffect {
	
	protected String src,sel;
	protected Image imgBuf[];
	protected float rate;
	protected int flag=0;
	protected float w,h;
	// PARTICLES
	private Image particleImg;
	private ParticleSystem system;
	private ConfigurableEmitter emitter;
	
	ColorShiftEffect(String src, String sel, float rate) throws SlickException{
		particleImg = new Image("res/particles/plus_violet.png");
		this.src = src;
		this.sel = sel;
		this.rate = rate;
		imgBuf = new Image[7];
		switch(sel){
		case "light":{
			for(int i=0;i<7;i++){
				imgBuf[i]=new Image(src+"/L"+(i+1)+".png");
				if(i==0){
					imgBuf[i].setAlpha(1);
				}else{
					imgBuf[i].setAlpha(0);
				}
			}
			break;
		}
		case "dark":{
			for(int i=0;i<7;i++){
				imgBuf[i]=new Image(src+"/D"+(i+1)+".png");
				if(i==0){
					imgBuf[i].setAlpha(1);
				}else{
					imgBuf[i].setAlpha(0);
				}
			}
			break;
		}
		}
		setParticle(0,0);
	}
	
	public void setOffset(float width,float height){
		if(Main.x>width){
			w=Main.x;
		}else{
			w=width;
		}
		if(Main.y>height){
			h=Main.y;
		}else{
			h=height;
		}
		emitter.xOffset.setMin(0);
		emitter.xOffset.setMax(w);
		emitter.yOffset.setMin(0);
		emitter.yOffset.setMax(h);
	}
	
	public void setParticle(float x,float y){
		try {
			system = new ParticleSystem(particleImg, 1500);
			File xmlFile = new File("res/particles/bg_emitter.xml");
			emitter = ParticleIO.loadEmitter(xmlFile);
			emitter.setPosition(x, y);
			system.addEmitter(emitter);
		} catch (Exception e) {
			System.exit(0);
		}
		system.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
	}
	
	public void render(float x, float y){
		for(int i=0;i<7;i++){
			imgBuf[i].draw(x,y);
		}
		system.render();
	}
	
	public void update(int delta){
		system.update(delta);
		
		switch(flag){
		case 0:{
			if (imgBuf[0].getAlpha() <= 0) {
				flag = 1;
			} else {
				imgSwitcher(imgBuf[0], imgBuf[1], delta);
			}
			break;
		}
		case 1:{
			if (imgBuf[1].getAlpha() <= 0) {
				flag = 2;
			} else {
				imgSwitcher(imgBuf[1], imgBuf[2], delta);
			}
			break;
		}
		case 2:{
			if (imgBuf[2].getAlpha() <= 0) {
				flag = 3;
			} else {
				imgSwitcher(imgBuf[2], imgBuf[3], delta);
			}
			break;
		}
		case 3:{
			if (imgBuf[3].getAlpha() <= 0) {
				flag = 4;
			} else {
				imgSwitcher(imgBuf[3], imgBuf[4], delta);
			}
			break;
		}
		case 4:{
			if (imgBuf[4].getAlpha() <= 0) {
				flag = 5;
			} else {
				imgSwitcher(imgBuf[4], imgBuf[5], delta);
			}
			break;
		}
		case 5:{
			if (imgBuf[5].getAlpha() <= 0) {
				flag = 6;
			} else {
				imgSwitcher(imgBuf[5], imgBuf[6], delta);
			}
			break;
		}
		case 6:{
			if (imgBuf[6].getAlpha() <= 0) {
				flag = 0;
			} else {
				imgSwitcher(imgBuf[6], imgBuf[0], delta);
			}
			break;
		}
		}
	}

	public void imgSwitcher(Image x, Image y, int delta) {
		x.setAlpha(x.getAlpha() - rate*delta);
		y.setAlpha(y.getAlpha() + rate*delta);
	}
}
