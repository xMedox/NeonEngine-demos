package net.medox.puzzle;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import net.medox.neonengine.components.StaticPhysicsComponent;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.Box;

public class CollisionAdder extends EntityComponent{
	public CollisionAdder(String file, Entity e){
		load(file, e);
	}
	
	private void load(String file, Entity e){
		try{
	    	FileInputStream fstream = new FileInputStream(file);
	    	DataInputStream in = new DataInputStream(fstream);
	    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    	String strLine = br.readLine();
	    	
	    	Pattern p = Pattern.compile(";");
            String[] article = p.split(strLine);
            
	    	Pattern p2 = Pattern.compile(",");
	    	
	    	for(int a = 0; a < article.length; a++){
                String[] article2 = p2.split(article[a]);
                
                int x = Integer.parseInt(article2[0]);
                int y = Integer.parseInt(article2[1]);
                int z = Integer.parseInt(article2[2]);
                
                int width = Integer.parseInt(article2[3]);
                int height = Integer.parseInt(article2[4]);
                int lenght = Integer.parseInt(article2[5]);
                
                addCollision(x+0.5f*width-0.5f, y+0.5f*height-0.5f, z+0.5f*lenght-0.5f, width, height, lenght, e);
	    	}
	    	
//	    	int x = 0;
//	    	int y = 0;
//	    	int z = 0;
//	    	
//	    	for(int i = 0; i < strLine.length(); i++){
//    	    	int test = Integer.parseInt(String.valueOf(strLine.charAt(i)));
//    	    	
//				if(test == 1){
//					addCollision(x+(int)pos.getX(), y+(int)pos.getY(), z+(int)pos.getZ(), e);
//				}
//        	    
//    	    	if(z < worldWidth){
//    	    		z ++;
//    	    	}
//
//    	    	if(z == worldWidth){
//    	    		z = 0;
//    	    		
//          	    	if(y < worldHeight){
//        	    		y ++;
//        	    	}
//
//          	    	if(y == worldHeight){
//        	    		y = 0;
//        	    		
//              	    	if(x < worldLenght){
//            	    		x ++;
//            	    	}
//
//              	    	if(x == worldLenght){
//            	    		x = 0;
//            	    	}
//        	    	}
//    	    	}
//	    	}
	    	in.close();
	    }catch (Exception e1){
	    	
		}
	}
	
	private void addCollision(float x, float y, float z, float width, float height, float lenght, Entity e){
		Box box = new Box(new Vector3f(0.5f, 0.5f, 0.5f));
		box.setMassProps(0);
		
		Transform t = new Transform();
		t.setPos(new Vector3f(x+0.5f, y+0.5f, z+0.5f));
		t.setScale(width, height, lenght);
		
		box.setTransform(t);
				
		e.addComponent(new StaticPhysicsComponent(box));
	}
}
