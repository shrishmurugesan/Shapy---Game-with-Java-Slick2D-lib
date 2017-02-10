package gameContents;
import java.awt.Dimension;
import java.awt.Toolkit;

public class GetResolution {
	
	public int height,width;
	
	GetResolution(){
		Toolkit toolkit =  Toolkit.getDefaultToolkit ();
		Dimension dim = toolkit.getScreenSize();
		height=dim.height;
		width=dim.width;
	}
}
