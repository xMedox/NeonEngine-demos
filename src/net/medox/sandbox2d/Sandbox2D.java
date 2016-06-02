package net.medox.sandbox2d;

import net.medox.neonengine.components.ScreenshotTaker;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.Game;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Font;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Window;

public class Sandbox2D extends Game{
	public static void main(String[] args){
//		NeonEngine.OPTION_ENABLE_VSYNC = 0;
//		NeonEngine.OPTION_ENABLE_MSAA = 1;
//		NeonEngine.OPTION_ENABLE_SHADOWS = 0;
//		NeonEngine.OPTION_ENABLE_2D = 1;
//		NeonEngine.OPTION_ENABLE_PARTICLES = 0;
//		NeonEngine.OPTION_ENABLE_BLOOM = 0;
//		NeonEngine.OPTION_TEXTURE_QUALITY = 0;
//		NeonEngine.OPTION_SHADOW_QUALITY = 0;
//		
//		NeonEngine.init(new Sandbox2D(), /*60*/1000);
		
		NeonEngine.OPTION_ENABLE_VSYNC = 1;
		NeonEngine.OPTION_ENABLE_FXAA = 0;
		NeonEngine.OPTION_ENABLE_SHADOWS = 0;
		NeonEngine.OPTION_ENABLE_2D = 1;
		NeonEngine.OPTION_ENABLE_PARTICLES = 0;
		NeonEngine.OPTION_ENABLE_BLOOM = 0;
		NeonEngine.OPTION_TEXTURE_QUALITY = 0;
		NeonEngine.OPTION_SHADOW_QUALITY = 0;
		
		NeonEngine.init(new Sandbox2D(), 60);
		
		Window.setStartTitle("Sandbox 2D");
		Window.setStartDimensions(World.worldWidth*World.blockSize, World.worldHeight*World.blockSize);
		Window.setStartFullscreen(false);
		Window.setStartResizable(false);
		Window.setStartIcon("icon16.png", "icon32.png");
//		Window.setStartCursor("cursor test red.png", 0, 0);
//		Window.setStartSizeLimits(256, 256, 854, 480);
		
		NeonEngine.createWindow();
		
		NeonEngine.start();
	}
	
	@Override
	public void init(){
		RenderingEngine.setMainFont(new Font("font.ttf", 16, false));
		
		addEntity(new Entity().addComponent(new Camera(0, 0, 0)).addComponent(new ScreenshotTaker()));
		
//		Entity2D player = new Entity2D();
//		MeshRenderer2D playerRenderer = new MeshRenderer2D(new Texture("wolf.png", true));
//		Player playerComponent = new Player();
//		player.addComponent(playerComponent);
//		player.addComponent(playerRenderer);
//		player.getTransform().setScale(new Vector2f(32, 64));
//		addEntity2D(player);
		
		Entity2D worldEntity = new Entity2D();
		World world = new World();
		worldEntity.addComponent(world);
		addEntity2D(worldEntity);
	}
	
	@Override
	public void cleanUp(){
		
	}
}
