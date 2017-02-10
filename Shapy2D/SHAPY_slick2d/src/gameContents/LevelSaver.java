package gameContents;

import java.io.File;

public class LevelSaver {

	public int level;
	public int score[];
	
	public LevelSaver(){
		File file = new File("C:/GameSaveFile.sav");
		if(!file.exists()){
			
		}
		score = new int[level];
	}
	public static void main(String[] args) {
		
	}
}
