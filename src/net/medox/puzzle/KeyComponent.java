package net.medox.puzzle;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.Box;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.PhysicsEngine;

public class KeyComponent extends EntityComponent{
	private CharacterController controller;
	
	private boolean grabbed;
	
	public KeyComponent(){
		Box box = new Box(new Vector3f(0.125f, 0.125f, 0.125f));
		
		box.setMassProps(0.5f);
		
		box.setPos(new Vector3f(7, 10, 7));
		
		box.setGroup(1);
		box.setObject(this);
		
		box.setSleepingThresholds(0, 0);
		
		controller = new CharacterController(box, 0.05f);
	}
	
	@Override
	public void update(float delta){
		if(!grabbed){
			getTransform().setPos(controller.getPos());
			getTransform().setRot(controller.getRot());
		}
	}
	
	public CharacterController getController(){
		return controller;
	}
	
	public void add(){
		grabbed = false;
		
		PhysicsEngine.addController(controller);
	}
	
	public void remove(){
		grabbed = true;
		
		PhysicsEngine.removeController(controller);
	}
	
	@Override
	public void addToEngine(){
		PhysicsEngine.addController(controller);
	}
	
	@Override
	public void cleanUp(){
		PhysicsEngine.removeController(controller);
	}
}
