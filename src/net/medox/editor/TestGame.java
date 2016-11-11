package net.medox.editor;

import net.medox.neonengine.audio.Listener;
import net.medox.neonengine.components.FreeLook;
import net.medox.neonengine.components.FreeMove;
import net.medox.neonengine.components2D.Lock2D;
import net.medox.neonengine.components2D.MeshRenderer2D;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.Game;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Cursor;
import net.medox.neonengine.rendering.Font;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Skybox;
import net.medox.neonengine.rendering.Texture;
import net.medox.neonengine.rendering.Window;

public class TestGame extends Game{
	public static void main(String[] args){
		NeonEngine.init(new TestGame(), 60);
		
		Window.setStartTitle("NeonEngine Editor");
		Window.setStartDimensions(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("icon_16.png", "icon_32.png");
		Window.setStartCursor(new Cursor("cursor test 2.png", 0, 0));
		
		NeonEngine.createWindow();
		
//		System.out.println("-----------------------------------------------");
//		System.out.println("OS name:        " + System.getProperty("os.name"));
//		System.out.println("OS version:     " + System.getProperty("os.version"));
//		System.out.println("OS arch:        " + System.getProperty("os.arch"));
//		System.out.println("OS arch:        " + System.getProperty("sun.arch.data.model"));
//		System.out.println("Java version:   " + System.getProperty("java.version"));
//		System.out.println("LWJGL version:  " + Version.getVersion());
//		System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
//		System.out.println("-----------------------------------------------");
		
		NeonEngine.start();
	}
	
	@Override
	public void init(){
		RenderingEngine.setMainFont(new Font("font.ttf", 16, false));
		RenderingEngine.setMainSkybox(new Skybox("right.png", "left.png", "top.png", "bottom.png", "front.png", "back.png"));
		
		Entity player = new Entity();
				
		player.addComponent(new Camera((float)Math.toRadians(65.0f), 0.01f, 1000.0f));
		player.addComponent(new FreeLook(0.15f));
		player.addComponent(new FreeMove(5));
		player.addComponent(new Listener());
		
		addEntity(player);
		
		
//		Entity skyboxEntity = new Entity();
//		Skybox skybox = new Skybox("white.png", "white.png", "white.png", "white.png", "white.png", "white.png");
//		
//		skyboxEntity.addComponent(skybox);
//		addEntity(skyboxEntity);
		
		
		Entity adder = new Entity();
		
		adder.addComponent(new AddComponent());
		
		addEntity(adder);
		
		
		init2D();
	}
	
	public void init2D(){
		Entity2D entity = new Entity2D();
		
		MeshRenderer2D meshRenderer2D = new MeshRenderer2D(new Texture("testeroonie2.png", true));
		Lock2D lock2D = new Lock2D(-16/2, -16/2, new Vector2f(0.5f, 0.5f));
		entity.addComponent(meshRenderer2D);
		entity.addComponent(lock2D);
		
		entity.getTransform().setPos(new Vector2f(Window.getWidth()/2-16/2, Window.getHeight()/2-16/2));
		entity.getTransform().setScale(new Vector2f(16, 16));
		
		addEntity2D(entity);
	}
	
	@Override
	public void cleanUp(){
		
	}
}
