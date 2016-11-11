package net.medox.puzzle;

import net.medox.neonengine.audio.Listener;
import net.medox.neonengine.components.FullscreenSetter;
import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.components.ScreenshotTaker;
import net.medox.neonengine.components2D.Lock2D;
import net.medox.neonengine.components2D.MeshRenderer2D;
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
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Skybox;
import net.medox.neonengine.rendering.Texture;
import net.medox.neonengine.rendering.Window;

public class Puzzle extends Game{
	public static void main(String[] args){
//		NeonEngine.enableProfiling(true);
//		NeonEngine.enableVSync(false);
//		NeonEngine.enableFXAA(false);
//		NeonEngine.enableShadows(true);
//		NeonEngine.enable2D(true);
//		NeonEngine.enableParticles(false);
//		NeonEngine.enableBloom(true);
//		NeonEngine.setTextureQuality(0);
//		NeonEngine.setShadowQuality(0);
//		
//		NeonEngine.init(new Puzzle(), /*60*/1000);
		
		NeonEngine.enableProfiling(true);
		NeonEngine.enableVSync(true);
		NeonEngine.enableFXAA(false);
		NeonEngine.enableShadows(true);
		NeonEngine.enable2D(true);
		NeonEngine.enableParticles(false);
		NeonEngine.enableBloom(true);
		NeonEngine.setTextureQuality(0);
		NeonEngine.setShadowQuality(0);
		
		NeonEngine.init(new Puzzle(), 60);
		
		Window.setStartTitle("Shadow Puzzle");
		Window.setStartDimensions(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("iconShadow16.png", "iconShadow32.png");
		Window.setStartCursor(new Cursor("cursor test.png", 0, 0));
		
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
		RenderingEngine.setMainSkybox(new Skybox("skyTest.png", "skyTest.png", "skyTopTest.png", "skyBottomTest.png", "skyTest.png", "skyTest.png"));
		
		RenderingEngine.setAmbiet(new Vector3f(0.2f, 0.2f, 0.2f));
		
		Entity2D shadowCooldown = new Entity2D();
//		Lock2D shadowCooldownLock = new Lock2D(4, -31, new Vector2f(0, 1));
		Lock2D shadowCooldownLock = new Lock2D(-60, -26, new Vector2f(0.5f, 0.5f));
		shadowCooldown.addComponent(shadowCooldownLock);
		shadowCooldown.getTransform().setScale(new Vector2f(120, 14));
		addEntity2D(shadowCooldown);
		
		Entity shadow = new Entity();
		Material shadowMatterial = new Material();
		shadowMatterial.setDiffuseMap(new Texture("shadow2.png", true));
		shadowMatterial.setEmissiveMap(new Texture("whiteSwordEmissive2.png", true));
		shadow.addComponent(new MeshRendererShadow(new Mesh("block.obj"), shadowMatterial));
		shadow.getTransform().setPos(new Vector3f(0, -100000, 0));
		shadow.getTransform().setScale(new Vector3f(1, 2, 1));
		addEntity(shadow);
		
		Entity shadowShow = new Entity();
		Material shadowShowMatterial = new Material();
		shadowShowMatterial.setDiffuseMap(new Texture("shadowShow.png", true));
		shadowShowMatterial.setEmissiveMap(new Texture("white.png", true));
		shadowShow.addComponent(new MeshRendererShadow(new Mesh("block.obj"), shadowShowMatterial));
		shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
		shadowShow.getTransform().setScale(new Vector3f(1, 2, 1));
		addEntity(shadowShow);
		
		Entity player = new Entity();
		Entity playerHead = new Entity();
		player.getTransform().setPos(1, 10, 1);
		playerHead.getTransform().setPos(0, /*0.75f*//*0.0125f*/0.7375f, 0);
		Camera camera = new Camera((float)Math.toRadians(65.0f), 0.01f, 400.0f);
		playerHead.addComponent(camera);
		PlayerComponent playerComponent = new PlayerComponent(0.15f, false, shadow, shadowShow, shadowCooldown);
		playerComponent.getController().setTransform(player.getTransform());
		playerHead.addComponent(playerComponent);
		PlayerUpdater playerUpdater = new PlayerUpdater(playerComponent);
		player.addComponent(playerUpdater);
		Listener listener = new Listener();
		playerHead.addComponent(listener);
		player.addChild(playerHead);
		player.addComponent(new FullscreenSetter()).addComponent(new ScreenshotTaker());
		addEntity(player);
		
		Entity handDelay = new Entity();
		handDelay.addComponent(new DelayLook(playerComponent));
		Entity hand = new Entity();
		Material handMatterial = new Material();
		handMatterial.setDiffuseMap(new Texture("shadow2.png", true));
		handMatterial.setEmissiveMap(new Texture("whiteSwordEmissive2.png", true));
		hand.addComponent(new MeshRendererShadow(new Mesh("Hand block.obj"), handMatterial));
		hand.getTransform().setScale(0.5f);
		hand.getTransform().setPos(0.75f, -0.45f, 1.1f);
		handDelay.addChild(hand);
		playerHead.addChild(handDelay);
		
		Entity key = new Entity();
		key.addComponent(new KeyComponent());
		Material redKeyMatterial = new Material();
		redKeyMatterial.setDiffuseMap(new Texture("redKey2.png", true));
		redKeyMatterial.setEmissiveMap(new Texture("white.png", true));
		key.addComponent(new MeshRendererShadow(new Mesh("block.obj"), redKeyMatterial));
		key.getTransform().setScale(0.25f);
		addEntity(key);
		
		Entity key2 = new Entity();
		key2.addComponent(new KeyComponent());
		Material blueKeyMatterial = new Material();
		blueKeyMatterial.setDiffuseMap(new Texture("blueKey2.png", true));
		blueKeyMatterial.setEmissiveMap(new Texture("white.png", true));
		key2.addComponent(new MeshRendererShadow(new Mesh("block.obj"), blueKeyMatterial));
		key2.getTransform().setScale(0.25f);
		addEntity(key2);
		
//		Entity cube2 = new Entity();
//		cube2.addComponent(new CubeComponent());
//		cube2.addComponent(new MeshRenderer(new Mesh("block.obj"), shadowShowMatterial));
//		addEntity(cube2);
		
		Entity directionalLightObject = new Entity();
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, /*8.0f*/16.0f, 1.0f, /*0.7f*/0.2f, 0.000001f);
		directionalLightObject.addComponent(directionalLight);
		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-50)));
		directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
		addEntity(directionalLightObject);
		
		Mesh meshBlocks = new Mesh("PuzzleBlocks.obj");
		Material materialBlocks = new Material();
		materialBlocks.setDiffuseMap(new Texture("blocksPuzzle.png", true));
		materialBlocks.setEmissiveMap(new Texture("blocksEmissivePuzzle.png", true));
//		materialBlocks.setDiffuseMap(new Texture("blocksPuzzle2.png", true));
//		materialBlocks.setEmissiveMap(new Texture("blocksEmissivePuzzle2.png", true));
		
		Entity world = new Entity();
		for(int x = 0; x < 3; x++){
			for(int z = 0; z < 3; z++){
				Entity chunk = new Entity();
				chunk.addComponent(new MeshRenderer(meshBlocks, materialBlocks));
				chunk.getTransform().setPos(x*16, 0, z*16);
				
				CollisionAdder.load("World/PuzzleBlocksCollision.txt", chunk, new Vector3f(x*16, 0, z*16));
				world.addChild(chunk);
			}
		}
		addEntity(world);
		
		Entity key2d = new Entity();
		Material blueKeyMatteriald = new Material();
		blueKeyMatteriald.setDiffuseMap(new Texture("stallTexture.png"));
		key2d.addComponent(new MeshRenderer(new Mesh("stall.obj"), blueKeyMatteriald));
		key2d.getTransform().setPos(10, 5, 10);
		addEntity(key2d);
		
		Entity2D crosshair = new Entity2D();
		MeshRenderer2D crosshairRenderer = new MeshRenderer2D(new Texture("crosshair.png", true));
		Lock2D crosshairLock = new Lock2D(-8, -8, new Vector2f(0.5f, 0.5f));
		crosshair.addComponent(crosshairRenderer);
		crosshair.addComponent(crosshairLock);
		crosshair.getTransform().setScale(16);
		
		crosshair.addComponent(new FPS());
		addEntity2D(crosshair);
	}
}
