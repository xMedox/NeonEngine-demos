package net.medox.puzzleeditor;

import net.medox.neonengine.components.FreeLook;
import net.medox.neonengine.components.FullscreenSetter;
import net.medox.neonengine.components.Lock2D;
import net.medox.neonengine.components.ScreenshotTaker;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.Game;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.DirectionalLight;
import net.medox.neonengine.rendering.Font;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Skybox;
import net.medox.neonengine.rendering.Texture;
import net.medox.neonengine.rendering.Window;

public class PuzzleEditor extends Game{
	public static void main(String[] args){
		NeonEngine.OPTION_ENABLE_VSYNC = 1;
		NeonEngine.OPTION_ENABLE_FXAA = 0;
		NeonEngine.OPTION_ENABLE_SHADOWS = 1;
		NeonEngine.OPTION_ENABLE_2D = 1;
		NeonEngine.OPTION_ENABLE_PARTICLES = 0;
		NeonEngine.OPTION_ENABLE_BLOOM = 1;
		NeonEngine.OPTION_TEXTURE_QUALITY = 0;
		NeonEngine.OPTION_SHADOW_QUALITY = 0;
		
		NeonEngine.init(new PuzzleEditor(), 60);
		
		Window.setStartTitle("Puzzle Editor");
		Window.setStartDimensions(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("iconShadow16.png", "iconShadow32.png");
		Window.setStartCursor("cursor test.png", 0, 0);
		
		NeonEngine.createWindow();
		
		NeonEngine.start();
	}
	
	@Override
	public void init(){
		RenderingEngine.setMainFont(new Font("font.ttf", 16, false));
		RenderingEngine.setMainSkybox(new Skybox("skyTest.png", "skyTest.png", "skyTopTest.png", "skyBottomTest.png", "skyTest.png", "skyTest.png"));
		
		RenderingEngine.setAmbiet(new Vector3f(0.2f, 0.2f, 0.2f));
		
		Entity changeMode = new Entity();
		changeMode.addComponent(new FullscreenSetter()).addComponent(new ScreenshotTaker());
		addEntity(changeMode);
		
		Entity camera = new Entity();
		camera.addComponent(new Camera((float)Math.toRadians(65.0f), 0.01f, 400.0f));
		camera.addComponent(new FreeLook(0.15f));
		camera.addComponent(new FreeFly(10.0f));
		addEntity(camera);
		
		Entity directionalLightObject = new Entity();
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, /*8.0f*/16.0f, 1.0f, /*0.7f*/0.2f, 0.000001f);
		directionalLightObject.addComponent(directionalLight);
		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-50)));
		directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
		addEntity(directionalLightObject);
		
		Entity world = new Entity();
		World w = new World(camera);
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
}
