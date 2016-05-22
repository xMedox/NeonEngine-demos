package net.medox.puzzle;

import net.medox.neonengine.audio.Listener;
import net.medox.neonengine.components.FreeLook;
import net.medox.neonengine.components.FullscreenSetter;
import net.medox.neonengine.components.Lock2D;
import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.components.MeshRenderer2D;
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
//		NeonEngine.init(new TestGame(), /*60*/1000);
		
		NeonEngine.OPTION_ENABLE_VSYNC = 1;
		NeonEngine.OPTION_ENABLE_FXAA = 0;
		NeonEngine.OPTION_ENABLE_SHADOWS = 1;
		NeonEngine.OPTION_ENABLE_2D = 1;
		NeonEngine.OPTION_ENABLE_PARTICLES = 0;
		NeonEngine.OPTION_ENABLE_BLOOM = 1;
		NeonEngine.OPTION_TEXTURE_QUALITY = 0;
		NeonEngine.OPTION_SHADOW_QUALITY = 0;
		
		NeonEngine.init(new Puzzle(), 60);
		
		Window.setStartTitle("Puzzle");
		Window.setStartDimensions(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("icon16.png", "icon32.png");
		Window.setStartCursor("cursor test red.png", 0, 0);
		
		NeonEngine.createWindow();
		
		NeonEngine.start();
	}
	
	@Override
	public void init(){
		RenderingEngine.setMainFont(new Font("font.ttf", 16, false));
		RenderingEngine.setMainSkybox(new Skybox("right.png", "left.png", "top.png", "bottom.png", "front.png", "back.png"));
		
		Entity player = new Entity();
		Entity playerHead = new Entity();
		player.getTransform().setPos(4, 4, 4);
		playerHead.getTransform().setPos(0, /*0.75f*//*0.0125f*/0.7375f, 0);
		Camera camera = new Camera((float)Math.toRadians(65.0f), 0.01f, 400.0f);
		playerHead.addComponent(camera);
		FreeLook look = new FreeLook(0.15f);
		playerHead.addComponent(look);
		PlayerComponent playerComponent = new PlayerComponent(camera);
		playerComponent.getBox().setTransform(player.getTransform());
		player.addComponent(playerComponent);
		Listener listener = new Listener();
		playerHead.addComponent(listener);
		player.addChild(playerHead);
		
		player.addComponent(new FullscreenSetter());
		addEntity(player);
		
		Entity swordDelay = new Entity();
		swordDelay.addComponent(new DelayLook(look));
		Entity sword = new Entity();
		Material swordMatterial = new Material();
		swordMatterial.setDiffuseMap(new Texture("whiteSword.png", true));
		swordMatterial.setEmissiveMap(new Texture("whiteSwordEmissive.png", true));
		sword.addComponent(new MeshRenderer(new Mesh("Sword R Block.obj"), swordMatterial));
		sword.getTransform().setScale(0.5f);
		sword.getTransform().setPos(0.75f, 0, 1.25f);
		sword.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(90));
		swordDelay.addChild(sword);
		playerHead.addChild(swordDelay);
		
		Entity directionalLightObject = new Entity();
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, /*8.0f*/16.0f, 1.0f, /*0.7f*/0.2f, 0.000001f);
		directionalLightObject.addComponent(directionalLight);
		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
		directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
		addEntity(directionalLightObject);
		
		Material material = new Material();
		material.setDiffuseMap(new Texture("blocks.png", true));
		material.setEmissiveMap(new Texture("blocks_glow.png", true));
		
		Mesh mesh = new Mesh("blocks.obj");
		
		for(int x = 0; x < 3; x++){
			for(int z = 0; z < 3; z++){
				Entity world = new Entity();
				world.addComponent(new MeshRenderer(mesh, material));
				world.getTransform().setPos(x*16, 0, z*16);
				
				Entity collision = new Entity();
				world.addComponent(new CollisionAdder("World/blocks_collision.txt", collision, new Vector3f(x*16, 0, z*16)));
				world.addChild(collision);
				addEntity(world);
			}
		}
		
		Entity2D crosshair = new Entity2D();
		MeshRenderer2D crosshairRenderer = new MeshRenderer2D(new Texture("testeroonie2.png", true));
		Lock2D crosshairLock = new Lock2D(-16/2, -16/2, new Vector2f(0.5f, 0.5f));
		crosshair.addComponent(crosshairRenderer);
		crosshair.addComponent(crosshairLock);
		crosshair.getTransform().setPos(Window.getWidth()/2-16/2, Window.getHeight()/2-16/2);
		crosshair.getTransform().setScale(16);
		
		crosshair.addComponent(new FPS());
		addEntity2D(crosshair);
	}
}
