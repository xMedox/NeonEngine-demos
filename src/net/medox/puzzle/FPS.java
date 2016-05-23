package net.medox.puzzle;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class FPS extends Entity2DComponent{
	private Vector3f color;
	
	public FPS(){
		color = new Vector3f(0.46666666666f, 0.75686274509f, 1);
	}
	
	@Override
	public void render(){
		RenderingEngine.drawString(7, 3, Integer.toString(RenderingEngine.getFPS()), color.sub(0.6f), 2, 2);
		RenderingEngine.drawString(5, 5, Integer.toString(RenderingEngine.getFPS()), color, 2, 2);
	}
}
