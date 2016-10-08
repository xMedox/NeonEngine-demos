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
//		NeonEngine.enableProfiling(true);
//		NeonEngine.enableVSync(false);
//		NeonEngine.enableFXAA(false);
//		NeonEngine.enableShadows(false);
//		NeonEngine.enable2D(true);
//		NeonEngine.enableParticles(false);
//		NeonEngine.enableBloom(false);
//		NeonEngine.setTextureQuality(0);
//		NeonEngine.setShadowQuality(0);
//		
//		NeonEngine.init(new Sandbox2D(), /*60*/1000);
		
		NeonEngine.enableProfiling(true);
		NeonEngine.enableVSync(true);
		NeonEngine.enableFXAA(false);
		NeonEngine.enableShadows(false);
		NeonEngine.enable2D(true);
		NeonEngine.enableParticles(false);
		NeonEngine.enableBloom(false);
		NeonEngine.setTextureQuality(0);
		NeonEngine.setShadowQuality(0);
		
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
		super.init();
//		RenderingEngine.addFilter(new Shader("filterInvert"));
//		RenderingEngine.addFilter(new Shader("filterBits"));
//		RenderingEngine.addFilter(new Shader("filterGrey"));
//		RenderingEngine.addFilter(new Shader("filterFlip"));
		
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
