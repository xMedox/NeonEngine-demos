package net.medox.puzzleeditor;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.math.Vector3f;

public class FreeFly extends EntityComponent{
	private final InputKey forwardKey;
	private final InputKey backKey;
	private final InputKey leftKey;
	private final InputKey rightKey;
	
	private float speed;
	
	public FreeFly(float speed){
		this(speed, new InputKey(Input.KEYBOARD, Input.KEY_W), new InputKey(Input.KEYBOARD, Input.KEY_S), new InputKey(Input.KEYBOARD, Input.KEY_A), new InputKey(Input.KEYBOARD, Input.KEY_D));
	}

	public FreeFly(float speed, InputKey forwardKey, InputKey backKey, InputKey leftKey, InputKey rightKey){
		this.speed = speed;
		this.forwardKey = forwardKey;
		this.backKey = backKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
	}
	
	@Override
	public void input(float delta){
		final float moveAmt = speed*delta;
		
		if(Input.inputKey(forwardKey)){
			move(getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized(), moveAmt);
		}
		if(Input.inputKey(backKey)){
			move(getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized(), -moveAmt);
		}
		if(Input.inputKey(leftKey)){
			move(getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized(), moveAmt);
		}
		if(Input.inputKey(rightKey)){
			move(getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized(), moveAmt);
		}
		if(Input.getKey(Input.KEY_SPACE)){
			move(new Vector3f(0, 1, 0), moveAmt);
		}
		if(Input.getKey(Input.KEY_LEFT_SHIFT)){
			move(new Vector3f(0, 1, 0), -moveAmt);
		}
	}
	
	public void move(Vector3f dir, float amt){
		getTransform().move(dir.mul(amt));
	}

	public float getSpeed(){
		return speed;
	}

	public void setSpeed(float speed){
		this.speed = speed;
	}
}
