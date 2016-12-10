package net.medox.blockeditor;

import net.medox.neonengine.components.FreeLook;
import net.medox.neonengine.components.FreeMove;
import net.medox.neonengine.components.FullscreenSetter;
import net.medox.neonengine.components.ScreenshotTaker;
import net.medox.neonengine.components2D.Lock2D;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.Game;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.lighting.DirectionalLight;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Cursor;
import net.medox.neonengine.rendering.Font;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Skybox;
import net.medox.neonengine.rendering.Texture;
import net.medox.neonengine.rendering.Window;

public class TestGame extends Game{
	public static void main(String[] args){
		NeonEngine.enableProfiling(true);
		NeonEngine.enableVSync(true);
		NeonEngine.enableFXAA(true);
		NeonEngine.enableShadows(true);
		NeonEngine.enable2D(true);
		NeonEngine.enableParticles(true);
		NeonEngine.enableBloom(true);
		NeonEngine.setTextureQuality(0);
		NeonEngine.setShadowQuality(0);
		NeonEngine.setRenderQuality(1);
		
		NeonEngine.init(new TestGame(), 60);
		
		Window.setStartTitle("Project Space");
		Window.setStartSize(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("icon16.png", "icon32.png");
		Window.setStartCursor(new Cursor("cursor test red.png", 0, 0));
		
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
		RenderingEngine.setMainSkybox(new Skybox("right.png", "left.png", "top.png", "bottom.png", "front.png", "back.png"));
		
		Entity changeMode = new Entity();
		changeMode.addComponent(new FullscreenSetter()).addComponent(new ScreenshotTaker()).addComponent(new ChangeMode());
		addEntity(changeMode);
		
		Entity camera = new Entity();
		camera.addComponent(new Camera((float)Math.toRadians(65.0f), 0.01f, 400.0f));
		camera.addComponent(new FreeLook(0.15f));
		camera.addComponent(new FreeMove(10.0f));
		addEntity(camera);
		
		Entity directionalLightObject = new Entity();
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, /*8.0f*/16.0f, 1.0f, /*0.7f*/0.2f, 0.000001f);
		directionalLightObject.addComponent(directionalLight);
//		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(/*-45*/-55)));
//		directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
//		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(0.5f, 0.8f, 0.5f), 1));
		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
		directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
		addEntity(directionalLightObject);
		
		Entity world = new Entity();
		World w = new World();
		world.addComponent(w);
		addEntity(world);
		
		Entity2D block = new Entity2D();
		Lock2D blockLock = new Lock2D(4, -64-4, new Vector2f(0, 1));
		block.getTransform().setScale(64);
		BlockShower blockShower = new BlockShower(new Texture("blocks.png", true), w);
		block.addComponent(blockLock);
		block.addComponent(blockShower);
		addEntity2D(block);
	}
	
	@Override
	public void cleanUp(){
		
	}
}
