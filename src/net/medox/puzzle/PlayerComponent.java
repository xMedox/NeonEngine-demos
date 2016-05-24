package net.medox.puzzle;

import net.medox.neonengine.components.Progressbar;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Time;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.Box;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.physics.Ray;
import net.medox.neonengine.rendering.Camera;

public class PlayerComponent extends EntityComponent{
	private Camera camera;
	private Box box;
	
	private float shadowCooldown;
	private float shadowTimer;
	
	private CharacterController controller;
	
	private Entity shadow;
	private Entity shadowShow;
	private boolean shadowSet;
	private Entity2D shadowCooldown2D;
	private Progressbar shadowCooldownProgressbar;
	
	public PlayerComponent(Camera camera, Entity shadow, Entity shadowShow, Entity2D shadowCooldown2D){
		box = new Box(new Vector3f(0.475f, 0.975f, 0.475f));
		
//		capsule.setMassProps(2.5f, new Vector3f(0, 0, 0));
		box.setMassProps(2.5f);
//		capsule.setRestitution(0f);
//		capsule.setAngularFactor(1f);
		box.setAngularFactor(0);
//		capsule.setFriction(0.5f);
		box.setSleepingThresholds(0, 0);
		
		Transform t = new Transform();
		t.setPos(new Vector3f(1, 10, 1));
		box.setTransform(t);
		
		controller = new CharacterController(box, 0.05f);
		
		controller.setJumpSpeed(4.6f);
//		controller.setFallSpeed(100f);
		controller.setMaxJumpHeight(1f);
//		controller.setJumpSpeed(100);
		
		controller.setMaxSlope((float)Math.toRadians(55));
		
		shadowCooldown = 4*Time.getSecond();
		
		shadowCooldownProgressbar = new Progressbar(1, new Vector3f(0.46666666666f, 0.75686274509f, 1));
		shadowCooldown2D.addComponent(shadowCooldownProgressbar);
		
		this.camera = camera;
		this.shadow = shadow;
		this.shadowShow = shadowShow;
		this.shadowCooldown2D = shadowCooldown2D;
	}
	
	public Box getBox(){
		return box;
	}
	
	@Override
	public void update(float delta){
		getTransform().setPos(controller.getPos());
	}
	
	@Override
	public void input(float delta){
		float speed = 4;
		
//		if(Input.getKeyDown(Input.KEY_L)){
//			Ray ray = new Ray(camera.getTransform().getTransformedPos(), camera.getTransform().getTransformedPos().add(camera.getTransform().getTransformedRot().getForward().mul(5)));
//			
//			if(ray.getHitCollider().equals(collider)){
//				collider.activate(true);
//				collider.applyCentralForce(camera.getTransform().getTransformedRot().getForward().mul(20));
//			}
//		}
//		
//		if(Input.getKeyDown(Input.KEY_K)){
//			collider.setLinearVelocity(new Vector3f(0, 0, 0));
//			collider.setAngularVelocity(new Vector3f(0, 0, 0));
//		}
		
//		if(Input.getMouseDown(Input.BUTTON_MIDDLE)){
//			audio.play();
////			audio.setRolloffFactor(0.25f);
//		}
		
		if(shadowTimer > 0){
			shadowTimer -= delta;
			
			shadowCooldownProgressbar.setProgress(/*(shadowCooldown-shadowTimer)/shadowCooldown*/shadowTimer/shadowCooldown);
		}else{
			shadowCooldownProgressbar.removeSelf();
			
			Ray ray = new Ray(camera.getTransform().getTransformedPos(), camera.getTransform().getTransformedPos().add(camera.getTransform().getRot().getForward().mul(10)));
			
			if(ray.hasHit()){
				shadowShow.getTransform().setPos(ray.getHitPoint().add(new Vector3f(0, 1, 0)));
			}else{
				shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
			}
			
			if(Input.getMouseDown(Input.BUTTON_LEFT) && Input.isGrabbed()){
				shadowTimer = shadowCooldown;
				
				shadowCooldown2D.addComponent(shadowCooldownProgressbar);
				
//				Ray ray = new Ray(camera.getTransform().getTransformedPos(), camera.getTransform().getTransformedPos().add(camera.getTransform().getRot().getForward().mul(10)));
//				
				if(ray.hasHit()){
					shadow.getTransform().setPos(shadowShow.getTransform().getTransformedPos());
					shadowSet = true;
					shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
				}
				
//				if(ray.getHitCollider().getGroup() == 1){
//					WolfComponent wolf = (WolfComponent)ray.getHitCollider().getObject();
//					if(!wolf.isDead()){
//						int r = Util.randomInt(0, 2);
//						
//						Texture t = null;
//						
//						if(r == 0){
//							t = t1;
//						}else if(r == 1){
//							t = t2;
//						}else if(r == 2){
//							t = t3;
//						}
//						
//						wolf.getMeshRenderer().setDiffuseMap(t);
//						wolf.getMeshRenderer().setEmissiveMap(g);
//						
//						wolf.damage(1);
//					}
//				}
			}
		}
		
		if(Input.getMouseDown(Input.BUTTON_RIGHT) && Input.isGrabbed() && shadowSet){
			controller.setPos(shadow.getTransform().getTransformedPos());
			shadow.getTransform().setPos(new Vector3f(0, -100000, 0));
			shadowSet = false;
		}
		
		if(Input.getKey(Input.KEY_LEFT_SHIFT)){
			speed = 6;
		}
		
		Vector3f dir = new Vector3f(0, 0, 0);
		
		if(Input.getKey(Input.KEY_W) && !Input.getKey(Input.KEY_S)){
			dir = dir.add(camera.getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized());
		}
		
		if(Input.getKey(Input.KEY_A) && !Input.getKey(Input.KEY_D)){
			dir = dir.add(camera.getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized());
		}
		
		if(Input.getKey(Input.KEY_S) && !Input.getKey(Input.KEY_W)){
			dir = dir.add(camera.getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized());
		}
		
		if(Input.getKey(Input.KEY_D) && !Input.getKey(Input.KEY_A)){
			dir = dir.add(camera.getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized());
		}
		
		if(Input.getKeyDown(Input.KEY_SPACE)){
			controller.jump();
		}
		
		move(dir.mul(speed));
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
