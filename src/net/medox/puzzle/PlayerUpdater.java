package net.medox.puzzle;

import net.medox.neonengine.core.EntityComponent;

public class PlayerUpdater extends EntityComponent{
	private PlayerComponent playerComponent;
	
	public PlayerUpdater(PlayerComponent playerComponent){
		this.playerComponent = playerComponent;
	}
	
	@Override
	public void update(float delta){
		getTransform().setPos(playerComponent.getController().getPos());
	}
}
