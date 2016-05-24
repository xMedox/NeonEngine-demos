package net.medox.puzzleeditor;

import net.medox.neonengine.core.Transform;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Texture;

public class CollisionObject{
	private static Mesh mesh;
	private static Material material;
	
	private Transform transform;
	
	private int x;
	private int y;
	private int z;
	
	private int width;
	private int height;
	private int lenght;
	
	public CollisionObject(int x, int y, int z, int width, int height, int lenght){
		if(mesh == null){
			mesh = new Mesh("block.obj");
			
			material = new Material();
			material.setDiffuseMap(new Texture("white.png", true));
		}
		
		transform = new Transform();
		
		transform.setPos(x+0.5f*width-0.5f, y+0.5f*height-0.5f, z+0.5f*lenght-0.5f);
		transform.setScale(width, height, lenght);
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.width = width;
		this.height = height;
		this.lenght = lenght;
	}
	
	public void render(Shader shader, Camera camera){
		if(mesh.inFrustum(transform, camera)){
			RenderingEngine.renderMesh(shader, transform, mesh, material, camera);
		}
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getZ(){
		return z;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getLenght(){
		return lenght;
	}
}
