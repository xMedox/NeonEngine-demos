package net.medox.puzzle;

import net.medox.neonengine.audio.Sound;
import net.medox.neonengine.components.Progressbar;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Time;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.Box;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.physics.Ray;
import net.medox.neonengine.rendering.Window;

public class PlayerComponent extends EntityComponent{
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	
	private Box box;
	private CharacterController controller;
	
	private float sensitivity;
	
	private float x;
	private float y;
	
	private boolean changed;
	
	private float shadowCooldown;
	private float shadowTimer;
	
	private Entity shadow;
	private Entity shadowShow;
	private boolean shadowSet;
	private Entity2D shadowCooldown2D;
	private Progressbar shadowCooldownProgressbar;
	private boolean shadowCamera;
	
	private Sound shadowSound;
	private Sound shadowPlaceSound;
	
	public PlayerComponent(float sensitivity, Entity shadow, Entity shadowShow, Entity2D shadowCooldown2D){
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
		
		shadowCooldown = 4*Time.getSecond();
		
		shadowCooldownProgressbar = new Progressbar(1, new Vector3f(0.46666666666f, 0.75686274509f, 1));
		shadowCooldown2D.addComponent(shadowCooldownProgressbar);
		
		shadowSound = new Sound("shadowStereo.wav");
		shadowPlaceSound = new Sound("shadowPlaceStereo.wav");
		
		this.sensitivity = sensitivity;
		this.shadow = shadow;
		this.shadowShow = shadowShow;
		this.shadowCooldown2D = shadowCooldown2D;
	}
	
	@Override
	public void input(float delta){
		final Vector2f centerPosition = Window.getCenterPosition();
		
		changed = false;
		
		if(Input.getKeyDown(Input.KEY_ESCAPE) && Input.isGrabbed()){
			Input.setGrabbed(false);
			
			changed = true;
		}else if(Input.getMouseDown(Input.BUTTON_LEFT) && !Input.isGrabbed()){
			Input.setMousePosition(centerPosition);
			Input.setGrabbed(true);
			
			changed = true;
		}
		
		if(Input.isGrabbed()){
			final Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

//			boolean rotY = deltaPos.getX() != 0;
//			boolean rotX = deltaPos.getY() != 0;

			if(deltaPos.getX() == 0){
				y = 0;
			}else{
				y = (float)Math.toRadians(deltaPos.getX() * sensitivity);
				getTransform().rotate(Y_AXIS, y);
				
				Input.setMousePosition(centerPosition);
			}
			
			if(deltaPos.getY() == 0){
				x = 0;
			}else{
				x = (float)-Math.toRadians(deltaPos.getY() * sensitivity);
				
//				float rot = Math.max(-57, Math.min(57, (float)Math.toDegrees(getTransform().getRot().getForward().getY()) + (float)Math.toDegrees(x)));
				
				float rot = 0;
				
				if(x > 0){
					rot = Util.clamp(getTransform().getRot().getBack().getY() + x, -1.1f, 1f);
					
					rot -= getTransform().getRot().getBack().getY();
				}else{
					rot = Util.clamp(getTransform().getRot().getBack().getY() + x, -1f, 1.1f);
					
					rot -= getTransform().getRot().getBack().getY();
				}
				
				
				
//				System.out.println(Math.toDegrees(x) + "|" + Math.toDegrees(rot));
				
//				rot = Util.clamp(getTransform().getRot().getUp().getY() + x, 0.1f, 10000);
//				
//				rot -= getTransform().getRot().getUp().getY();
				
//				if(getTransform().getRot().getUp().getY() + x < 0.1f){
//					x = 0;
//				}
//				
//				System.out.println((float)Math.toDegrees(getTransform().getRot().getForward().getY()));
				
//				System.out.println((float)Math.toRadians(rot) + "|" + x);
				
//				if((float)Math.toDegrees(getTransform().getRot().getUp().getY()) < 0){
////					getTransform().rotate(getTransform().getRot().getRight(), (float)Math.toRadians(-89));
//					getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-89)));
//////					getTransform().setRot(new Quaternion(new Vector3f(-89, rot.getY(), rot.getZ()), (float)Math.toRadians(1)));
//////				}
//////				
//////				if(getTransform().getRot().getForward().getY() < 1){
////////					getTransform().setRot(new Quaternion(new Vector3f((float)Math.toRadians(-89), rot.getY(), rot.getZ()), 1));
//////					getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-89)));
//////////					getTransform().getRot().mul(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-89)));
//				}else{
//					getTransform().rotate(getTransform().getRot().getRight(), x);
//				}
				
				getTransform().rotate(getTransform().getRot().getRight(), rot);
				
				Input.setMousePosition(centerPosition);
			}
			
			float speed = 4;
			float speedForward = 4;
			
			Vector3f dir = new Vector3f(0, 0, 0);
			
			if(Input.getKey(Input.KEY_LEFT_SHIFT)){
				speedForward = 6;
			}
			
			if(Input.getKey(Input.KEY_W) && !Input.getKey(Input.KEY_S)){
				dir = dir.add(getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized().mul(speedForward));
			}
			
			if(Input.getKey(Input.KEY_A) && !Input.getKey(Input.KEY_D)){
				dir = dir.add(getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized().mul(speed));
			}
			
			if(Input.getKey(Input.KEY_S) && !Input.getKey(Input.KEY_W)){
				dir = dir.add(getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized().mul(speed));
			}
			
			if(Input.getKey(Input.KEY_D) && !Input.getKey(Input.KEY_A)){
				dir = dir.add(getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized().mul(speed));
			}
			
			if(Input.getKeyDown(Input.KEY_SPACE)){
				controller.jump();
			}
			
			move(dir);
			
//			System.out.println(getTransform().getRot().getUp().toString());
//			System.out.println(getTransform().getRot().getUp().getY() + "|" + getTransform().getRot().getDown().getY() +  "|" + getTransform().getRot().getForward().getY());
			
//			if(rotY || rotX){
//				Input.setMousePosition(centerPosition);
//			}
		}
	}
	
	public void move(Vector3f vel){
//		if(controller.onGround()){
			controller.setWalkDirection(vel.mul(0.015f));
//		}else{
//			controller.setWalkDirection(vel.mul(0.0125f));
//		}
	}
	
	@Override
	public void update(float delta){
		if(Input.getMouse(Input.BUTTON_MIDDLE) && Input.isGrabbed() && !changed && shadowSet){
			Ray ray = new Ray(shadow.getTransform().getTransformedPos(), shadow.getTransform().getTransformedPos().add(getTransform().getTransformedRot().getBack().mul(4)));
			
			if(ray.hasHit()){
				Vector3f pos = controller.getTransform().getTransformedPos();
				Vector3f pos2 = ray.getHitPoint().sub(getTransform().getTransformedRot().getBack().mul(0.02f));
				getTransform().setPos(-pos.getX()+pos2.getX(), -pos.getY()+pos2.getY(), -pos.getZ()+pos2.getZ());
			}else{
				Vector3f pos = controller.getTransform().getTransformedPos();
				Vector3f pos2 = shadow.getTransform().getTransformedPos().add(getTransform().getTransformedRot().getBack().mul(4));
				getTransform().setPos(-pos.getX()+pos2.getX(), -pos.getY()+pos2.getY(), -pos.getZ()+pos2.getZ());
			}
			
			shadowCamera = true;
		}else{
			if(shadowCamera){
				getTransform().setPos(0, /*0.75f*//*0.0125f*/0.7375f, 0);
			}
			shadowCamera = false;
		}
		
		if(shadowTimer > 0){
			shadowTimer -= delta;
			
			shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
			
			shadowCooldownProgressbar.setProgress(/*(shadowCooldown-shadowTimer)/shadowCooldown*/shadowTimer/shadowCooldown);
		}else{
			shadowCooldownProgressbar.removeSelf();
			
			if(!shadowCamera){
				Ray ray = new Ray(getTransform().getTransformedPos(), getTransform().getTransformedPos().add(getTransform().getRot().getForward().mul(8)));
				
				if(ray.hasHit()){
					shadowShow.getTransform().setPos(ray.getHitPoint().add(new Vector3f(0, 1, 0)));
				}else{
					shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
				}
				
				if(Input.getMouseDown(Input.BUTTON_LEFT) && Input.isGrabbed() && !changed){
					shadowTimer = shadowCooldown;
					
					shadowCooldown2D.addComponent(shadowCooldownProgressbar);
					
					if(ray.hasHit()){
						shadowPlaceSound.play();
						
						shadow.getTransform().setPos(shadowShow.getTransform().getTransformedPos());
						shadowSet = true;
						shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
					}
				}
			}else{
				shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
			}
		}
		
		if(Input.getMouseDown(Input.BUTTON_RIGHT) && Input.isGrabbed() && !changed && shadowSet){
			shadowSound.play();
			
			controller.setPos(shadow.getTransform().getTransformedPos());
			shadow.getTransform().setPos(new Vector3f(0, -100000, 0));
			shadowSet = false;
		}
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}

	public float getSensitivity(){
		return sensitivity;
	}

	public void setSensitivity(float sensitivity){
		this.sensitivity = sensitivity;
	}
	
	public CharacterController getController(){
		return controller;
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
