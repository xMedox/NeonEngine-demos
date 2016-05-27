package net.medox.puzzle;

import net.medox.neonengine.audio.Listener;
import net.medox.neonengine.components.FullscreenSetter;
import net.medox.neonengine.components.Lock2D;
import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.components.MeshRenderer2D;
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
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Skybox;
import net.medox.neonengine.rendering.Texture;
import net.medox.neonengine.rendering.Window;

public class Puzzle extends Game{
	public static void main(String[] args){
//		NeonEngine.OPTION_ENABLE_VSYNC = 0;
//		NeonEngine.OPTION_ENABLE_FXAA = 0;
//		NeonEngine.OPTION_ENABLE_SHADOWS = 1;
//		NeonEngine.OPTION_ENABLE_2D = 1;
//		NeonEngine.OPTION_ENABLE_PARTICLES = 0;
//		NeonEngine.OPTION_ENABLE_BLOOM = 1;
//		NeonEngine.OPTION_TEXTURE_QUALITY = 0;
//		NeonEngine.OPTION_SHADOW_QUALITY = 0;
//		
//		NeonEngine.init(new Puzzle(), /*60*/1000);
		
		NeonEngine.OPTION_ENABLE_VSYNC = 1;
		NeonEngine.OPTION_ENABLE_FXAA = 0;
		NeonEngine.OPTION_ENABLE_SHADOWS = 1;
		NeonEngine.OPTION_ENABLE_2D = 1;
		NeonEngine.OPTION_ENABLE_PARTICLES = 0;
		NeonEngine.OPTION_ENABLE_BLOOM = 1;
		NeonEngine.OPTION_TEXTURE_QUALITY = 0;
		NeonEngine.OPTION_SHADOW_QUALITY = 0;
		
		NeonEngine.init(new Puzzle(), 60);
		
		Window.setStartTitle("Shadow Puzzle");
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
		RenderingEngine.setMainSkybox(new Skybox("right.png", "left.png", "top.png", "bottom.png", "front.png", "back.png"));
		
		Entity2D shadowCooldown = new Entity2D();
//		Lock2D shadowCooldownLock = new Lock2D(4, -31, new Vector2f(0, 1));
		Lock2D shadowCooldownLock = new Lock2D(-60, -26, new Vector2f(0.5f, 0.5f));
		shadowCooldown.addComponent(shadowCooldownLock);
		shadowCooldown.getTransform().setScale(new Vector2f(120, 14));
		addEntity2D(shadowCooldown);
		
		Entity shadow = new Entity();
		Material shadowMatterial = new Material();
		shadowMatterial.setDiffuseMap(new Texture("shadow2.png", true));
		shadowMatterial.setEmissiveMap(new Texture("whiteSwordEmissive.png", true));
		shadow.addComponent(new MeshRenderer(new Mesh("block.obj"), shadowMatterial));
		shadow.getTransform().setPos(new Vector3f(0, -100000, 0));
		shadow.getTransform().setScale(new Vector3f(1, 2, 1));
		addEntity(shadow);
		
		Entity shadowShow = new Entity();
		Material shadowShowMatterial = new Material();
		shadowShowMatterial.setDiffuseMap(new Texture("shadowShow.png", true));
		shadowShowMatterial.setEmissiveMap(new Texture("white.png", true));
		shadowShow.addComponent(new MeshRenderer(new Mesh("block.obj"), shadowShowMatterial));
		shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
		shadowShow.getTransform().setScale(new Vector3f(1, 2, 1));
		addEntity(shadowShow);
		
		Entity player = new Entity();
		Entity playerHead = new Entity();
		player.getTransform().setPos(1, 10, 1);
		playerHead.getTransform().setPos(0, /*0.75f*//*0.0125f*/0.7375f, 0);
		Camera camera = new Camera((float)Math.toRadians(65.0f), 0.01f, 400.0f);
		playerHead.addComponent(camera);
		PlayerComponent playerComponent = new PlayerComponent(0.15f, shadow, shadowShow, shadowCooldown);
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
		handMatterial.setEmissiveMap(new Texture("whiteSwordEmissive.png", true));
		hand.addComponent(new MeshRenderer(new Mesh("Hand block.obj"), handMatterial));
		hand.getTransform().setScale(0.5f);
		hand.getTransform().setPos(0.75f, -0.45f, 1.1f);
		handDelay.addChild(hand);
		playerHead.addChild(handDelay);
		
		Entity directionalLightObject = new Entity();
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, /*8.0f*/16.0f, 1.0f, /*0.7f*/0.2f, 0.000001f);
		directionalLightObject.addComponent(directionalLight);
		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
		directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
		addEntity(directionalLightObject);
		
		Mesh meshBlocks = new Mesh("PuzzleBlocks.obj");
		Material materialBlocks = new Material();
		materialBlocks.setDiffuseMap(new Texture("blocksPuzzle.png", true));
		materialBlocks.setEmissiveMap(new Texture("blocksEmissivePuzzle.png", true));
		
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
