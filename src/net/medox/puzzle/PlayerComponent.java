package net.medox.puzzle;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.Box;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.rendering.Camera;

public class PlayerComponent extends EntityComponent{
	private Camera camera;
	private Box box;
	
	private CharacterController controller;
	
	public PlayerComponent(Camera camera){
		box = new Box(new Vector3f(0.475f, 0.975f, 0.475f));
		
//		capsule.setMassProps(2.5f, new Vector3f(0, 0, 0));
		box.setMassProps(2.5f);
//		capsule.setRestitution(0f);
//		capsule.setAngularFactor(1f);
		box.setAngularFactor(0);
//		capsule.setFriction(0.5f);
		box.setSleepingThresholds(0, 0);
		
//		Transform t = new Transform();
//		t.setPos(new Vector3f(1, 10, 1));
//		box.setTransform(t);
		
		controller = new CharacterController(box, 0.05f);
		
		controller.setJumpSpeed(4.6f);
//		controller.setFallSpeed(100f);
		controller.setMaxJumpHeight(1f);
//		controller.setJumpSpeed(100);
		
		controller.setMaxSlope((float)Math.toRadians(55));
		
		this.camera = camera;
	}
	
	public CharacterController getController(){
		return controller;
	}
	
	@Override
	public void update(float delta){
		getTransform().setPos(controller.getPos());
	}
	
	@Override
	public void input(float delta){
		float speed = 4;
		float speedForward = 4;
		
		Vector3f dir = new Vector3f(0, 0, 0);
		
		if(Input.getKey(Input.KEY_LEFT_SHIFT)){
			speedForward = 6;
		}
		
		if(Input.getKey(Input.KEY_W) && !Input.getKey(Input.KEY_S)){
			dir = dir.add(camera.getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized().mul(speedForward));
		}
		
		if(Input.getKey(Input.KEY_A) && !Input.getKey(Input.KEY_D)){
			dir = dir.add(camera.getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized().mul(speed));
		}
		
		if(Input.getKey(Input.KEY_S) && !Input.getKey(Input.KEY_W)){
			dir = dir.add(camera.getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized().mul(speed));
		}
		
		if(Input.getKey(Input.KEY_D) && !Input.getKey(Input.KEY_A)){
			dir = dir.add(camera.getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized().mul(speed));
		}
		
		if(Input.getKeyDown(Input.KEY_SPACE)){
			controller.jump();
		}
		
		move(dir);
	}
	
	public void move(Vector3f vel){
//		if(controller.onGround()){
			controller.setWalkDirection(vel.mul(0.015f));
//		}else{
//			controller.setWalkDirection(vel.mul(0.0125f));
//		}
	}
	
	@Override
	public void addToEngine(){
		PhysicsEngine.addController(controller);
	}
	
	@Override
	public void cleanUp(){
//		PhysicsEngine.removeObject(cylinder);
		PhysicsEngine.removeController(controller);
	}
}
