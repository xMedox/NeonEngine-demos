package net.medox.puzzle;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.Box;
import net.medox.neonengine.physics.PhysicsEngine;

public class CubeComponent extends EntityComponent{
	private Box box;
	
	public CubeComponent(){
		box = new Box(new Vector3f(0.5f, 0.5f, 0.5f));
		
		box.setMassProps(0.00000001f);
		
		box.setPos(new Vector3f(7, 10, 7));
		
		box.setGroup(1);
		box.setObject(this);
	}
	
	@Override
	public void update(float delta){
		getTransform().setPos(box.getPos());
		getTransform().setRot(box.getRot());
	}
	
	public Box getBox(){
		return box;
	}
	
	@Override
	public void addToEngine(){
		PhysicsEngine.addObject(box);
	}
	
	@Override
	public void cleanUp(){
		PhysicsEngine.removeObject(box);
	}
}
