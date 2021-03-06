package net.medox.puzzle;

import net.medox.neonengine.audio.Sound;
import net.medox.neonengine.components2D.Progressbar;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Time;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.BoxCollider;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.physics.Ray;
import net.medox.neonengine.rendering.Window;

public class PlayerComponent extends EntityComponent{
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	
	private CharacterController controller;
	
	private float sensitivity;
	private boolean invertY;
	
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
	
	private KeyComponent key;
	private boolean carrying;
	
	private Sound shadowSound;
	private Sound shadowPlaceSound;
	
	public PlayerComponent(float sensitivity, boolean invertY, Entity shadow, Entity shadowShow, Entity2D shadowCooldown2D){
		BoxCollider box = new BoxCollider(new Vector3f(0.475f, 0.975f, 0.475f));
		
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
		
		controller.setMaxJumpHeight(4.6f);
//		controller.setJumpSpeed(4.6f);
//		controller.setFallSpeed(100f);
//		controller.setMaxJumpHeight(4.6f);
//		controller.setJumpSpeed(100);
		
		controller.setMaxSlope((float)Math.toRadians(55));
		
		shadowCooldown = 4*Time.getSecond();
		
		shadowCooldownProgressbar = new Progressbar(1, new Vector3f(0.46666666666f, 0.75686274509f, 1));
		shadowCooldown2D.addComponent(shadowCooldownProgressbar);
		
		shadowSound = new Sound("shadowStereo.wav");
		shadowPlaceSound = new Sound("shadowPlaceStereo.wav");
		
		this.sensitivity = sensitivity;
		this.invertY = invertY;
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
				float xSave = 0;
				
				if(invertY){
					xSave = (float)Math.toRadians(deltaPos.getY() * sensitivity);
				}else{
					xSave = (float)-Math.toRadians(deltaPos.getY() * sensitivity);
				}
				
//				float rot = Math.max(-57, Math.min(57, (float)Math.toDegrees(getTransform().getRot().getForward().getY()) + (float)Math.toDegrees(x)));
				
				if(xSave > 0){
					x = Util.clamp(getTransform().getRot().getBack().getY() + xSave, -1.1f, 1f);
					
					x -= getTransform().getRot().getBack().getY();
				}else{
					x = Util.clamp(getTransform().getRot().getBack().getY() + xSave, -1f, 1.1f);
					
					x -= getTransform().getRot().getBack().getY();
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
				
				getTransform().rotate(getTransform().getRot().getRight(), x);
				
				Input.setMousePosition(centerPosition);
			}
			
			float speed = 4;
			
			Vector3f dir = new Vector3f(0, 0, 0);
			boolean changed = false;
			
			if(Input.getKey(Input.KEY_LEFT_SHIFT)){
				speed = 6;
			}
			
			if(Input.getKey(Input.KEY_W) && !Input.getKey(Input.KEY_S)){
				dir = dir.add(getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized());
				changed = true;
			}
			
			if(Input.getKey(Input.KEY_A) && !Input.getKey(Input.KEY_D)){
				dir = dir.add(getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized());
				changed = true;
			}
			
			if(Input.getKey(Input.KEY_S) && !Input.getKey(Input.KEY_W)){
				dir = dir.add(getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized());
				changed = true;
			}
			
			if(Input.getKey(Input.KEY_D) && !Input.getKey(Input.KEY_A)){
				dir = dir.add(getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized());
				changed = true;
			}
			
			if(Input.getKeyDown(Input.KEY_SPACE)){
				controller.jump();
			}
			
			if(changed){
				move(dir.normalized().mul(speed));
			}else{
				move(dir);
			}
			
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
			
			if(!shadowCamera && !carrying){
				boolean creatable = false;
				
				Ray ray = new Ray(getTransform().getTransformedPos(), getTransform().getTransformedPos().add(getTransform().getRot().getForward().mul(8)));
				
				if(ray.hasHit()){
					if(ray.getHitCollider().getGroup() == 0 && ray.getHitNormal().getY() >= 0.5f){
						Ray ray1 = new Ray(ray.getHitPoint().add(new Vector3f(-0.5f, 0.25f, -0.5f)), ray.getHitPoint().add(new Vector3f(0.5f, 0.25f, 0.5f)));
						
						if(!ray1.hasHit()){
							Ray ray2 = new Ray(ray.getHitPoint().add(new Vector3f(0.5f, 0.25f, -0.5f)), ray.getHitPoint().add(new Vector3f(-0.5f, 0.25f, 0.5f)));
							
							if(!ray2.hasHit()){
								Ray ray3 = new Ray(ray.getHitPoint().add(new Vector3f(0.5f, 0.25f, 0.5f)), ray.getHitPoint().add(new Vector3f(-0.5f, 0.25f, -0.5f)));
								
								if(!ray3.hasHit()){
									Ray ray4 = new Ray(ray.getHitPoint().add(new Vector3f(-0.5f, 0.25f, 0.5f)), ray.getHitPoint().add(new Vector3f(0.5f, 0.25f, -0.5f)));
									
									if(!ray4.hasHit()){
										creatable = true;
										
										shadowShow.getTransform().setPos(ray.getHitPoint().add(new Vector3f(0, 1, 0)));
									}else{
										shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
									}
								}else{
									shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
								}
							}else{
								shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
							}
						}else{
							shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
						}
					}else{
						shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
					}
				}else{
					shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
				}
				
				if(Input.getMouseDown(Input.BUTTON_LEFT) && Input.isGrabbed() && !changed && creatable){
					shadowTimer = shadowCooldown;
					
					shadowCooldown2D.addComponent(shadowCooldownProgressbar);
					
					shadowPlaceSound.play();
					
					shadow.getTransform().setPos(shadowShow.getTransform().getTransformedPos());
					shadowSet = true;
					shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
				}
			}else{
				shadowShow.getTransform().setPos(new Vector3f(0, -100000, 0));
			}
		}
		
		if(Input.getMouseDown(Input.BUTTON_RIGHT) && Input.isGrabbed() && !changed && shadowSet){
			shadowSound.play();
			
			if(carrying){
				dropCube();
			}
			
			controller.setPos(shadow.getTransform().getTransformedPos());
			shadow.getTransform().setPos(new Vector3f(0, -100000, 0));
			shadowSet = false;
		}
		
		boolean carryChanged = false;
		
		if(Input.getKeyDown(Input.KEY_E) && !carrying){
			Ray ray = new Ray(getTransform().getTransformedPos(), getTransform().getTransformedPos().add(getTransform().getRot().getForward().mul(3)));
			
			if(ray.hasHit()){
				if(ray.getHitCollider().getGroup() == 1){
					key = (KeyComponent)ray.getHitCollider().getObject();
					
					key.getTransform().setRot(key.getController().getRot());
					
					key.remove();
					
					carrying = true;
					carryChanged = true;
				}
			}
		}
		
		if(carrying){
//			key.getBox().activate(true);
//			
//			key.getBox().setGravity(0);
//			
////			cube.getBox().setLinearVelocity(new Vector3f(0, 0, 0));
//			key.getBox().setAngularVelocity(new Vector3f(0, 0, 0));
//			
////			cube.getBox().setPos(getTransform().getTransformedPos().add(getTransform().getRot().getForward().mul(3)));
////			cube.getBox().setRot(getTransform().getTransformedRot());
//			
////			Ray ray = new Ray(key.getBox().getTransform().getTransformedPos(), getTransform().getTransformedPos().add(getTransform().getRot().getForward().mul(3.5f)));
////			
////			if(ray.hasHit()){
////				if(key.getBox().getTransform().getTransformedPos().sub(ray.getHitPoint()).length() >= 0.65f){
////					key.getBox().setLinearVelocity(getTransform().getTransformedPos().add(getTransform().getRot().getForward().mul(3)).sub(key.getBox().getTransform().getTransformedPos()).mul(15));
////				}else{
////					Vector3f dir = key.getBox().getTransform().getTransformedPos().sub(getTransform().getTransformedPos().add(getTransform().getRot().getForward().mul(3.5f)));
////					
////					key.getBox().setLinearVelocity(ray.getHitPoint().add(dir.normalized().mul(0.51f)).sub(key.getBox().getTransform().getTransformedPos()).mul(15));
////				}
////			}else{
//				key.getBox().setLinearVelocity(getTransform().getTransformedPos().add(getTransform().getRot().getForward().mul(3)).sub(key.getBox().getTransform().getTransformedPos()).mul(15));
////			}
			
			Ray ray = new Ray(getTransform().getTransformedPos(), getTransform().getTransformedPos().add(getTransform().getRot().getForward().mul(2f)));
//			Ray ray1 = new Ray(getTransform().getTransformedPos(), getTransform().getTransformedPos().add(getTransform().getRot().getForward().add(getTransform().getRot().getForward().mul(new Vector3f(0.125f, 0, 0))).mul(2f)));
//			Ray ray2 = new Ray(getTransform().getTransformedPos(), getTransform().getTransformedPos().add(getTransform().getRot().getForward().add(getTransform().getRot().getForward().mul(new Vector3f(-0.125f, 0, 0))).mul(2f)));
//			Ray ray3 = new Ray(getTransform().getTransformedPos(), getTransform().getTransformedPos().add(getTransform().getRot().getForward().add(getTransform().getRot().getForward().mul(new Vector3f(0, 0, 0.125f))).mul(2f)));
//			Ray ray4 = new Ray(getTransform().getTransformedPos(), getTransform().getTransformedPos().add(getTransform().getRot().getForward().add(getTransform().getRot().getForward().mul(new Vector3f(0, 0, -0.125f))).mul(2f)));
			
			if(ray.hasHit()/* || ray1.hasHit() || ray2.hasHit() || ray3.hasHit() || ray4.hasHit()*/){
				key.getTransform().setPos(ray.getHitPoint().add(ray.getHitNormal().mul(0.125f)));
			}else{
				key.getTransform().setPos(getTransform().getTransformedPos().add(getTransform().getRot().getForward().mul(/*0.125f*/2)));
			}
			
//			key.getTransform().setRot(getTransform().getTransformedRot());
			
			if(Input.getKeyDown(Input.KEY_E) && !carryChanged){
				dropCube();
			}
			
//			Vector3f dir = getTransform().getTransformedPos().sub(key.getTransform().getTransformedPos());
//			
//			if(dir.length() >= 3){
//				dropCube();
//			}
		}
	}
	
	public void dropCube(){
		carrying = false;
		
		key.add();
		
		key.getController().setTransform(key.getTransform());
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
