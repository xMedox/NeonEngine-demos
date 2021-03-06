package net.medox.block3rd;

import net.medox.neonengine.audio.Listener;
import net.medox.neonengine.components.FullscreenSetter;
import net.medox.neonengine.components.LookComponent;
import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.components.ScreenshotTaker;
import net.medox.neonengine.components2D.Lock2D;
import net.medox.neonengine.components2D.MeshRenderer2D;
import net.medox.neonengine.components2D.Progressbar;
import net.medox.neonengine.components2D.Slider;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.Game;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Util;
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

public class TestGame extends Game{
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
//		NeonEngine.setRenderQuality(1);
//		
//		NeonEngine.init(new TestGame(), /*60*/1000);
		
		NeonEngine.enableProfiling(true);
		NeonEngine.enableVSync(true);
		NeonEngine.enableFXAA(false);
		NeonEngine.enableShadows(true);
		NeonEngine.enable2D(true);
		NeonEngine.enableParticles(false);
		NeonEngine.enableBloom(true);
		NeonEngine.setTextureQuality(0);
		NeonEngine.setShadowQuality(0);
		NeonEngine.setRenderQuality(1);
		
		NeonEngine.init(new TestGame(), 60);
		
		Window.setStartTitle("Project Robot");
		Window.setStartSize(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
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
		RenderingEngine.setMainSkybox(new Skybox("right.png", "left.png", "top.png", "bottom.png", "front.png", "back.png"));
		
//		String es = Util.encrypt("Just a test");
//		System.out.println(es);
//		es = Util.decrypt(es);
//		System.out.println(es);
//		
//		System.out.println(Util.loadFromFile("test.txt"));
		
		Entity2D life = new Entity2D();
//		Lock2D lifeLock = new Lock2D(4, -80-27, new Vector2f(0, 1));
		Lock2D lifeLock = new Lock2D(4, -80-27+8, new Vector2f(0, 1));
		life.addComponent(lifeLock);
		life.getTransform().setScale(new Vector2f(202, 27));
		addEntity2D(life);
		
		Entity2D sprint = new Entity2D();
//		Lock2D sprintLock = new Lock2D(4, -119-27, new Vector2f(0, 1));
		Lock2D sprintLock = new Lock2D(4, -107-27+8, new Vector2f(0, 1));
		sprint.addComponent(sprintLock);
		sprint.getTransform().setScale(new Vector2f(202, 27));
		addEntity2D(sprint);
		
		Entity2D button = new Entity2D();
		Lock2D buttonLock = new Lock2D(4, -134-27+8, new Vector2f(0, 1));
		button.addComponent(buttonLock);
		button.addComponent(new Slider(0, new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(1, 1, 1), 0, new InputKey(Input.MOUSE, Input.BUTTON_RIGHT)));
		button.getTransform().setScale(new Vector2f(202, 27));
		addEntity2D(button);
		
		Entity player = new Entity();
		Entity playerLook = new Entity();
		Entity playerHead = new Entity();
		player.getTransform().setPos(4, 4, 4);
		playerHead.getTransform().setPos(0.3f, /*0.75f*//*0.0125f*//*0.7375f*/0, -4);
		Camera cam = new Camera((float)Math.toRadians(65.0f), 0.01f, 400.0f);
		playerHead.addComponent(cam);
		LookComponent look = new LookComponent(0.15f);
		playerLook.addComponent(look);
		PlayerComponent p = new PlayerComponent(playerLook);
//		p.getCapsule().setTransform(player.getTransform());
		p.getBox().setTransform(player.getTransform());
		player.addComponent(p);
		Listener listener = new Listener();
		playerHead.addComponent(listener);
		playerLook.addChild(playerHead);
		player.addChild(playerLook);
		addEntity(player);
		
//		ArrayList<Quaternion> quaternions = new ArrayList<Quaternion>();
		
//		Entity swordS = new Entity();
//		swordS.addComponent(new DelayLook(look/*, quaternions, 120/2, false*/));
//		playerHead.addChild(swordS);
		
		Material dragonM = new Material();
		
		Entity2D team = new Entity2D();
		MeshRenderer2D teamRenderer = null;
		if(Util.randomInt(0, 1) == 0){
			Window.setCursor(new Cursor("cursor test red.png", 0, 0));
			addEntity2D(new Entity2D().addComponent(new FPS(new Vector3f(1, 0, 0))));
			life.addComponent(new Progressbar(1, new Vector3f(1, 0, 0)));
			sprint.addComponent(new Progressbar(1, new Vector3f(0.09803921568f, 0.09803921568f, 0.09803921568f)));
			teamRenderer = new MeshRenderer2D(new Texture("RED.png", true));
			
			dragonM.setDiffuseMap(new Texture("redSword.png", true));
			dragonM.setEmissiveMap(new Texture("block60_glow.png", true));
		}else{
			Window.setCursor(new Cursor("cursor test blue.png", 0, 0));
			addEntity2D(new Entity2D().addComponent(new FPS(new Vector3f(0, 0, 1))));
			life.addComponent(new Progressbar(1, new Vector3f(0, 0, 1)));
			sprint.addComponent(new Progressbar(1, new Vector3f(0.89803921568f, 0.89803921568f, 0.89803921568f)));
			teamRenderer = new MeshRenderer2D(new Texture("BLUE.png", true));
			
			dragonM.setDiffuseMap(new Texture("blueSword.png", true));
			dragonM.setEmissiveMap(new Texture("block60_glow.png", true));
		}
		
		Entity dragon = new Entity();
		dragon.addComponent(new MeshRenderer(new Mesh("Dragon.obj"), dragonM));
		dragon.addComponent(new Look3rdPerson(playerLook));
		dragon.getTransform().setScale(0.25f);
//		dragon.getTransform().setPos(3.5f, 1, 3.5f);
		dragon.getTransform().setPos(0, -1, 0);
		player.addChild(dragon);
		
		Lock2D teamLock = new Lock2D(4, -64-4, new Vector2f(0, 1));
		team.addComponent(teamRenderer);
		team.addComponent(teamLock);
//		team.getTransform().setPos(new Vector2f(0, 0));
		team.getTransform().setScale(new Vector2f(64, 64));
		addEntity2D(team);
		
//		Entity2D team2 = new Entity2D();
//		MeshRenderer2D teamRenderer2 = new MeshRenderer2D(new Texture("BLUE.png", true));
//		Lock2D teamLock2 = new Lock2D(64, -64, new Vector2f(0, 1));
//		team2.addComponent(teamRenderer2);
//		team2.addComponent(teamLock2);
////		team2.getTransform().setPos(new Vector2f(0, 0));
//		team2.getTransform().setScale(new Vector2f(64, 64));
//		addEntity2D(team2);
		
		Entity changeMode = new Entity();
		changeMode.addComponent(new FullscreenSetter()).addComponent(new ScreenshotTaker()).addComponent(new ChangeMode());
		addEntity(changeMode);
//		addEntity(new Entity()/*.addComponent(new FullscreenSetter())*/.addComponent(new ScreenshotTaker()).addComponent(new ChangeMode()));
		
//		Entity camera = new Entity();
//		camera.addComponent(new Camera((float)Math.toRadians(65.0f), 0.01f, 400.0f));
//		camera.addComponent(new LookComponent(0.15f));
//		camera.addComponent(new MoveComponent(10.0f));
//		addEntity(camera);
		
		Entity wolf = new Entity();
		Material wolfM = new Material();
		wolfM.setDiffuseMap(new Texture("wolf.png", true));
		MeshRendererWolf wolfR = new MeshRendererWolf(new Mesh("Wolf.obj"), wolfM);
		wolf.addComponent(wolfR);
		wolf.getTransform().setScale(1.5f);
//		wolf.getTransform().setPos(5, 1, 2.5f);
		WolfComponent wolfP = new WolfComponent(wolfR);
		wolf.addComponent(wolfP);
		addEntity(wolf);
		
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
		
		Material mushdM = new Material();
		mushdM.setDiffuseMap(new Texture("mushroom.png", true));
		
		Entity mush = new Entity();
		mush.addComponent(new MeshRenderer(new Mesh("mushroom.obj"), mushdM));
		mush.getTransform().setScale(0.5f);
		mush.getTransform().setPos(3, 1, 3);
		addEntity(mush);
		
		Entity add = new Entity();
		add.addComponent(new AddCollision());
//		add.addComponent(new Tester(p.getBox(), wolfP.getBox()));
		addEntity(add);
		
		Entity2D e = new Entity2D();
		MeshRenderer2D c = new MeshRenderer2D(new Texture("testeroonie2.png", true));
		Lock2D l = new Lock2D(-16/2, -16/2, new Vector2f(0.5f, 0.5f));
		e.addComponent(c);
		e.addComponent(l);
		e.getTransform().setPos(Window.getWidth()/2-16/2, Window.getHeight()/2-16/2);
		e.getTransform().setScale(16);
		addEntity2D(e);
	}
	
	@Override
	public void cleanUp(){
		
	}
}
