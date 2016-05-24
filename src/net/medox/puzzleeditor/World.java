package net.medox.puzzleeditor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Shader;

public class World extends EntityComponent{
	private static int worldWidth = 64;
	private static int worldHeight = 64;
	private static int worldLenght = 64;
	
	private Block[][][] blocks;
	
	private List<CollisionObject> collisionObjects;
	
	private int selX = 0;
	private int selY = 0;
	private int selZ = 0;
	
	private int selXColl = -1;
	private int selYColl = -1;
	private int selZColl = -1;
	
	private int[] selectedTexture;
	
	private boolean renderCollision;
	
	private int wait;
	
	private Entity camera;
	private float distance = 4;
	
	public World(Entity camera){
		blocks = new Block[worldWidth][worldHeight][worldLenght];
		
		collisionObjects = new ArrayList<CollisionObject>();
		
		for(int x = 0; x < worldWidth; x++){
			for(int y = 0; y < worldHeight; y++){
				for(int z = 0; z < worldLenght; z++){
					int[] texture = {-1, 0};
					Transform transform = new Transform();
					transform.setPos(x, y, z);
					
					blocks[x][y][z] = new Block(texture, transform, false);
					
					if(x == 0 && y == 0 && z == 0){
						blocks[x][y][z].seleted = true;
					}
				}
			}
		}
		
		selectedTexture = new int[]{0, 0};
		
		this.camera = camera;
	}
	
	public void load(){
		 try{
	    	FileInputStream fstream = new FileInputStream("World/PuzzleBlocks.txt");
	    	DataInputStream in = new DataInputStream(fstream);
	    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    	String strLine = br.readLine();
	    	
	    	int x = 0;
	    	int y = 0;
	    	int z = 0;
		
	    	Pattern p = Pattern.compile(",");
            String[] article2 = p.split(strLine);
	    	
	    	for(int a2 = 0; a2 < article2.length; a2++){
                String article = article2[a2];
                
    	    	int test = Integer.parseInt(article);
    	    	
    	    	int a = -1;
    	    	int b = -1;
    	    	boolean solid = false;
    	    	
				if(test == 0){
					a = -1;
					b = 0;
					
					solid = false;
				}else if(test == 1){
					a = 0;
					b = 0;
					
					solid = true;
				}else if(test == 2){
					a = 1;
					b = 0;
					
					solid = true;
				}else if(test == 3){
					a = 2;
					b = 0;
					
					solid = true;
				}else if(test == 4){
					a = 3;
					b = 0;
					
					solid = true;
				}else if(test == 5){
					a = 4;
					b = 0;
					
					solid = true;
				}else if(test == 6){
					a = 5;
					b = 0;
					
					solid = true;
				}else if(test == 7){
					a = 6;
					b = 0;
					
					solid = true;
				}else if(test == 8){
					a = 7;
					b = 0;
					
					solid = true;
				}else if(test == 9){
					a = 8;
					b = 0;
					
					solid = true;
				}else if(test == 10){
					a = 9;
					b = 0;
					
					solid = true;
				}else if(test == 11){
					a = 0;
					b = 1;
					
					solid = true;
				}else if(test == 12){
					a = 1;
					b = 1;
					
					solid = true;
				}else if(test == 13){
					a = 2;
					b = 1;
					
					solid = true;
				}else if(test == 14){
					a = 3;
					b = 1;
					
					solid = true;
				}else if(test == 15){
					a = 4;
					b = 1;
					
					solid = true;
				}else if(test == 16){
					a = 5;
					b = 1;
					
					solid = true;
				}else if(test == 17){
					a = 6;
					b = 1;
					
					solid = true;
				}else if(test == 18){
					a = 7;
					b = 1;
					
					solid = true;
				}else if(test == 19){
					a = 8;
					b = 1;
					
					solid = true;
				}
    	    	
    	    	int[] ab = {a, b};
    	    	
    	    	blocks[x][y][z].texture = ab;
    	    	blocks[x][y][z].solid = solid;
        	    
    	    	if(z < worldWidth){
    	    		z ++;
    	    	}

    	    	if(z == worldWidth){
    	    		z = 0;
    	    		
          	    	if(y < worldHeight){
        	    		y ++;
        	    	}

          	    	if(y == worldHeight){
        	    		y = 0;
        	    		
              	    	if(x < worldLenght){
            	    		x ++;
            	    	}

              	    	if(x == worldLenght){
            	    		x = 0;
            	    	}
        	    	}
    	    	}
	    	}
	    	in.close();
	    }catch (Exception e){
	    	
		}
	}
	
	public void save(){
		String output = "";
		
		boolean start = true;
		int save;
		String s;
    	
		for(int x = 0; x < worldWidth; x++){
			for(int y = 0; y < worldHeight; y++){
				for(int z = 0; z < worldLenght; z++){
					save = (blocks[x][y][z].texture[0]+1)+(blocks[x][y][z].texture[1]*10);
					
					if(start){
						s = String.valueOf(save);
						
						start = false;
					}else{
						s = "," + String.valueOf(save);
					}
					
					output += s;
				}
			}
    	}
		
		Util.saveToFile("World/PuzzleBlocks.txt", output);
	}
	
	public void loadCollision(){
		try{
	    	FileInputStream fstream = new FileInputStream("World/PuzzleBlocksCollision.txt");
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
                
                collisionObjects.add(new CollisionObject(x, y, z, width, height, lenght));
	    	}
	    	in.close();
	    }catch (Exception e1){
	    	
		}
	}
	
	public void saveCollision(){
		String output = "";
		
//		for(int x = 0; x < worldWidth; x++){
//			for(int y = 0; y < worldHeight; y++){
//				for(int z = 0; z < worldLenght; z++){
//					int save = 0;
//					
////					if(blocks[x][y][z].solid){
////						save = 1;
////					}else{
////						save = 0;
////					}
//					
//					output += String.valueOf(save);
//				}
//			}
//    	}
		
		boolean start = true;
		
		for(CollisionObject collisionObject : collisionObjects){
			if(start){
				output += String.valueOf(collisionObject.getX()) + ",";
				output += String.valueOf(collisionObject.getY()) + ",";
				output += String.valueOf(collisionObject.getZ()) + ",";
				
				output += String.valueOf(collisionObject.getWidth()) + ",";
				output += String.valueOf(collisionObject.getHeight()) + ",";
				output += String.valueOf(collisionObject.getLenght());
				
				start = false;
			}else{
				output += ";" + String.valueOf(collisionObject.getX()) + ",";
				output += String.valueOf(collisionObject.getY()) + ",";
				output += String.valueOf(collisionObject.getZ()) + ",";
				
				output += String.valueOf(collisionObject.getWidth()) + ",";
				output += String.valueOf(collisionObject.getHeight()) + ",";
				output += String.valueOf(collisionObject.getLenght());
			}
		}
		
		Util.saveToFile("World/PuzzleBlocksCollision.txt", output);
	}
	
	public void saveModel(){
		String output = "";
		
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Integer[]> faces = new ArrayList<Integer[]>();
    	
		for(int x = 0; x < worldWidth; x++){
			for(int y = 0; y < worldHeight; y++){
				for(int z = 0; z < worldLenght; z++){
					if(blocks[x][y][z].solid){
//						int texture = (blocks[x][y][z].texture[0]+1)*(blocks[x][y][z].texture[1]+1)*4-4;
						int texture = ((blocks[x][y][z].texture[0]+1)+(blocks[x][y][z].texture[1])*10)*4-4;
						
						if(x > 0){
							if(!blocks[x-1][y][z].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x, y, z));
								vertices.add(new Vector3f(x, y+1, z));
								vertices.add(new Vector3f(x, y, z+1));
								vertices.add(new Vector3f(x, y+1, z+1));
								
								Integer[] fa = {id+3, 4+texture, 5, id+1, 3+texture, 5, id, 2+texture, 5};
								Integer[] fb = {id, 2+texture, 5, id+2, 1+texture, 5, id+3, 4+texture, 5};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x, y, z));
							vertices.add(new Vector3f(x, y+1, z));
							vertices.add(new Vector3f(x, y, z+1));
							vertices.add(new Vector3f(x, y+1, z+1));
							
							Integer[] fa = {id+3, 4+texture, 5, id+1, 3+texture, 5, id, 2+texture, 5};
							Integer[] fb = {id, 2+texture, 5, id+2, 1+texture, 5, id+3, 4+texture, 5};
							
							faces.add(fa);
							faces.add(fb);
						}
						
						if(x < worldWidth-1){
							if(!blocks[x+1][y][z].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x+1, y, z));
								vertices.add(new Vector3f(x+1, y+1, z));
								vertices.add(new Vector3f(x+1, y, z+1));
								vertices.add(new Vector3f(x+1, y+1, z+1));
								
								Integer[] fa = {id, 1+texture, 3, id+1, 4+texture, 3, id+3, 3+texture, 3};
								Integer[] fb = {id+3, 3+texture, 3, id+2, 2+texture, 3, id, 1+texture, 3};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x+1, y, z));
							vertices.add(new Vector3f(x+1, y+1, z));
							vertices.add(new Vector3f(x+1, y, z+1));
							vertices.add(new Vector3f(x+1, y+1, z+1));
							
							Integer[] fa = {id, 1+texture, 3, id+1, 4+texture, 3, id+3, 3+texture, 3};
							Integer[] fb = {id+3, 3+texture, 3, id+2, 2+texture, 3, id, 1+texture, 3};
							
							faces.add(fa);
							faces.add(fb);
						}
						
						if(y > 0){
							if(!blocks[x][y-1][z].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x, y, z));
								vertices.add(new Vector3f(x+1, y, z));
								vertices.add(new Vector3f(x, y, z+1));
								vertices.add(new Vector3f(x+1, y, z+1));
								
								Integer[] fa = {id, 4+texture, 1, id+1, 3+texture, 1, id+3, 2+texture, 1};
								Integer[] fb = {id+3, 2+texture, 1, id+2, 1+texture, 1, id, 4+texture, 1};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x, y, z));
							vertices.add(new Vector3f(x+1, y, z));
							vertices.add(new Vector3f(x, y, z+1));
							vertices.add(new Vector3f(x+1, y, z+1));
							
							Integer[] fa = {id, 4+texture, 1, id+1, 3+texture, 1, id+3, 2+texture, 1};
							Integer[] fb = {id+3, 2+texture, 1, id+2, 1+texture, 1, id, 4+texture, 1};
							
							faces.add(fa);
							faces.add(fb);
						}
						
						if(y < worldHeight-1){
							if(!blocks[x][y+1][z].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x, y+1, z));
								vertices.add(new Vector3f(x+1, y+1, z));
								vertices.add(new Vector3f(x, y+1, z+1));
								vertices.add(new Vector3f(x+1, y+1, z+1));
								
								Integer[] fa = {id+3, 3+texture, 2, id+1, 2+texture, 2, id, 1+texture, 2};
								Integer[] fb = {id, 1+texture, 2, id+2, 4+texture, 2, id+3, 3+texture, 2};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x, y+1, z));
							vertices.add(new Vector3f(x+1, y+1, z));
							vertices.add(new Vector3f(x, y+1, z+1));
							vertices.add(new Vector3f(x+1, y+1, z+1));
							
							Integer[] fa = {id+3, 3+texture, 2, id+1, 2+texture, 2, id, 1+texture, 2};
							Integer[] fb = {id, 1+texture, 2, id+2, 4+texture, 2, id+3, 3+texture, 2};
							
							faces.add(fa);
							faces.add(fb);
						}
						
						if(z > 0){
							if(!blocks[x][y][z-1].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x, y, z));
								vertices.add(new Vector3f(x+1, y, z));
								vertices.add(new Vector3f(x, y+1, z));
								vertices.add(new Vector3f(x+1, y+1, z));
								
								Integer[] fa = {id+3, 3+texture, 6, id+1, 2+texture, 6, id, 1+texture, 6};
								Integer[] fb = {id, 1+texture, 6, id+2, 4+texture, 6, id+3, 3+texture, 6};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x, y, z));
							vertices.add(new Vector3f(x+1, y, z));
							vertices.add(new Vector3f(x, y+1, z));
							vertices.add(new Vector3f(x+1, y+1, z));
							
							Integer[] fa = {id+3, 3+texture, 6, id+1, 2+texture, 6, id, 1+texture, 6};
							Integer[] fb = {id, 1+texture, 6, id+2, 4+texture, 6, id+3, 3+texture, 6};
							
							faces.add(fa);
							faces.add(fb);
						}
						
						if(z < worldLenght-1){
							if(!blocks[x][y][z+1].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x, y, z+1));
								vertices.add(new Vector3f(x+1, y, z+1));
								vertices.add(new Vector3f(x, y+1, z+1));
								vertices.add(new Vector3f(x+1, y+1, z+1));
								
								Integer[] fa = {id, 2+texture, 4, id+1, 1+texture, 4, id+3, 4+texture, 4};
								Integer[] fb = {id+3, 4+texture, 4, id+2, 3+texture, 4, id, 2+texture, 4};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x, y, z+1));
							vertices.add(new Vector3f(x+1, y, z+1));
							vertices.add(new Vector3f(x, y+1, z+1));
							vertices.add(new Vector3f(x+1, y+1, z+1));
							
							Integer[] fa = {id, 2+texture, 4, id+1, 1+texture, 4, id+3, 4+texture, 4};
							Integer[] fb = {id+3, 4+texture, 4, id+2, 3+texture, 4, id, 2+texture, 4};
							
							faces.add(fa);
							faces.add(fb);
						}
					}
				}
			}
		}
		
		String s = "";
		
		for(int i = 0; i < vertices.size(); i++){
			s = "v " + vertices.get(i).getX() + " " + vertices.get(i).getY() + " " + vertices.get(i).getZ() + "\n";
			
			output += s;
		}
		
		s = "vt 0.0 0.9\nvt 0.1 0.9\nvt 0.1 1.0\nvt 0.0 1.0\n";
		output += s;
		s = "vt 0.1 0.9\nvt 0.2 0.9\nvt 0.2 1.0\nvt 0.1 1.0\n";
		output += s;
		s = "vt 0.2 0.9\nvt 0.3 0.9\nvt 0.3 1.0\nvt 0.2 1.0\n";
		output += s;
		s = "vt 0.3 0.9\nvt 0.4 0.9\nvt 0.4 1.0\nvt 0.3 1.0\n";
		output += s;
		s = "vt 0.4 0.9\nvt 0.5 0.9\nvt 0.5 1.0\nvt 0.4 1.0\n";
		output += s;
		s = "vt 0.5 0.9\nvt 0.6 0.9\nvt 0.6 1.0\nvt 0.5 1.0\n";
		output += s;
		s = "vt 0.6 0.9\nvt 0.7 0.9\nvt 0.7 1.0\nvt 0.6 1.0\n";
		output += s;
		s = "vt 0.7 0.9\nvt 0.8 0.9\nvt 0.8 1.0\nvt 0.7 1.0\n";
		output += s;
		s = "vt 0.8 0.9\nvt 0.9 0.9\nvt 0.9 1.0\nvt 0.8 1.0\n";
		output += s;
		s = "vt 0.9 0.9\nvt 1.0 0.9\nvt 1.0 1.0\nvt 0.9 1.0\n";
		output += s;
		s = "vt 0.0 0.8\nvt 0.1 0.8\nvt 0.1 0.9\nvt 0.0 0.9\n";
		output += s;
		s = "vt 0.1 0.8\nvt 0.2 0.8\nvt 0.2 0.9\nvt 0.1 0.9\n";
		output += s;
		s = "vt 0.2 0.8\nvt 0.3 0.8\nvt 0.3 0.9\nvt 0.2 0.9\n";
		output += s;
		s = "vt 0.3 0.8\nvt 0.4 0.8\nvt 0.4 0.9\nvt 0.3 0.9\n";
		output += s;
		s = "vt 0.4 0.8\nvt 0.5 0.8\nvt 0.5 0.9\nvt 0.4 0.9\n";
		output += s;
		s = "vt 0.5 0.8\nvt 0.6 0.8\nvt 0.6 0.9\nvt 0.5 0.9\n";
		output += s;
		s = "vt 0.6 0.8\nvt 0.7 0.8\nvt 0.7 0.9\nvt 0.6 0.9\n";
		output += s;
		s = "vt 0.7 0.8\nvt 0.8 0.8\nvt 0.8 0.9\nvt 0.7 0.9\n";
		output += s;
		s = "vt 0.8 0.8\nvt 0.9 0.8\nvt 0.9 0.9\nvt 0.8 0.9\n";
		output += s;
		
		s = "vn 0.0 -1.0 0.0\nvn 0.0 1.0 0.0\nvn 1.0 0.0 0.0\nvn 0.0 0.0 1.0\nvn -1.0 0.0 0.0\nvn 0.0 0.0 -1.0\ns off\n";
		output += s;
		
		for(int i = 0; i < faces.size(); i++){
			s = "f " + faces.get(i)[0] + "/" + faces.get(i)[1] + "/" + faces.get(i)[2] + " " +
				faces.get(i)[3] + "/" + faces.get(i)[4] + "/" + faces.get(i)[5] + " " +
				faces.get(i)[6] + "/" + faces.get(i)[7] + "/" + faces.get(i)[8] +
				"\n";
			
			output += s;
		}
		
		Util.saveToFile("res/models/PuzzleBlocks.obj", output);
	}
	
	@Override
	public void input(float delta){
		if(wait == 0){
//			if(Input.getKeyDown(Input.KEY_LEFT_CONTROL)){
//				if(selY > 0){
//					blocks[selX][selY][selZ].seleted = false;
//					
//					selY -= 1;
//					
//					blocks[selX][selY][selZ].seleted = true;
//				}
//			}else if(Input.getKeyDown(Input.KEY_SPACE)){
//				if(selY < worldHeight-1){
//					blocks[selX][selY][selZ].seleted = false;
//					
//					selY += 1;
//					
//					blocks[selX][selY][selZ].seleted = true;
//				}
//			}else if(Input.getKeyDown(Input.KEY_LEFT)){
//				if(selX > 0){
//					blocks[selX][selY][selZ].seleted = false;
//					
//					selX -= 1;
//					
//					blocks[selX][selY][selZ].seleted = true;
//				}
//			}else if(Input.getKeyDown(Input.KEY_RIGHT)){
//				if(selX < worldWidth-1){
//					blocks[selX][selY][selZ].seleted = false;
//					
//					selX += 1;
//					
//					blocks[selX][selY][selZ].seleted = true;
//				}
//			}else if(Input.getKeyDown(Input.KEY_UP)){
//				if(selZ < worldLenght-1){
//					blocks[selX][selY][selZ].seleted = false;
//					
//					selZ += 1;
//					
//					blocks[selX][selY][selZ].seleted = true;
//				}
//			}else if(Input.getKeyDown(Input.KEY_DOWN)){
//				if(selZ > 0){
//					blocks[selX][selY][selZ].seleted = false;
//					
//					selZ -= 1;
//					
//					blocks[selX][selY][selZ].seleted = true;
//				}
//			}
			
			if(Input.getKeyDown(Input.KEY_UP)){
				distance += 0.5f;
			}else if(Input.getKeyDown(Input.KEY_DOWN)){
				distance -= 0.5f;
			}
			
			Vector3f pos = camera.getTransform().getTransformedPos().add(camera.getTransform().getRot().getForward().mul(distance));
			
			blocks[selX][selY][selZ].seleted = false;
			
			if(Math.round(pos.getX()) >= 0 && Math.round(pos.getX()) < worldWidth){
				selX = Math.round(pos.getX());
			}else if(Math.round(pos.getX()) < 0){
				selX = 0;
			}else if(Math.round(pos.getX()) >= worldWidth){
				selX = worldWidth-1;
			}
			
			if(Math.round(pos.getY()) >= 0 && Math.round(pos.getY()) < worldHeight){
				selY = Math.round(pos.getY());
			}else if(Math.round(pos.getY()) < 0){
				selY = 0;
			}else if(Math.round(pos.getY()) >= worldHeight){
				selY = worldHeight-1;
			}
			
			if(Math.round(pos.getZ()) >= 0 && Math.round(pos.getZ()) < worldLenght){
				selZ = Math.round(pos.getZ());
			}else if(Math.round(pos.getZ()) < 0){
				selZ = 0;
			}else if(Math.round(pos.getZ()) >= worldLenght){
				selZ = worldLenght-1;
			}
			
			blocks[selX][selY][selZ].seleted = true;
			
			if(!renderCollision){
				if(Input.getMouse(Input.BUTTON_LEFT)){
					blocks[selX][selY][selZ].texture = new int[]{-1, 0};
					blocks[selX][selY][selZ].solid = false;
				}else if(Input.getMouse(Input.BUTTON_RIGHT)){
					blocks[selX][selY][selZ].texture = selectedTexture;
					blocks[selX][selY][selZ].solid = true;
				}else if(Input.getMouse(Input.BUTTON_MIDDLE)){
					if(blocks[selX][selY][selZ].texture[0] != -1){
						selectedTexture = blocks[selX][selY][selZ].texture;
					}
				}
			}else{
				if(Input.getMouseDown(Input.BUTTON_LEFT)){
					if(selXColl == -1){
						selXColl = selX;
						selYColl = selY;
						selZColl = selZ;
					}else{
						int width;
						int height;
						int lenght;
						
	//					if(selX-selXColl > 0){
	//						width = selX-selXColl;
	//					}else{
	//						width = selXColl-selX;
	//					}
	//					
	//					if(selYColl-selYColl > 0){
	//						height = selY-selYColl;
	//					}else{
	//						height = selYColl-selY;
	//					}
	//					
	//					if(selZ-selZColl > 0){
	//						lenght = selZ-selZColl;
	//					}else{
	//						lenght = selZColl-selZ;
	//					}
						
						width = selX-selXColl+1;
						height = selY-selYColl+1;
						lenght = selZ-selZColl+1;
						
						if(width != 0 || height != 0 || lenght != 0){
							collisionObjects.add(new CollisionObject(selXColl, selYColl, selZColl, width, height, lenght));
						}
						
						selXColl = -1;
						selYColl = -1;
						selZColl = -1;
					}
				}
			}
			
			if(Input.getKeyDown(Input.KEY_Q)){
				renderCollision = !renderCollision;
			}
			
			if(Input.getKeyDown(Input.KEY_1)){
				selectedTexture = new int[]{0, 0};
			}else if(Input.getKeyDown(Input.KEY_2)){
				selectedTexture = new int[]{1, 0};
			}else if(Input.getKeyDown(Input.KEY_3)){
				selectedTexture = new int[]{2, 0};
			}else if(Input.getKeyDown(Input.KEY_4)){
				selectedTexture = new int[]{3, 0};
			}else if(Input.getKeyDown(Input.KEY_5)){
				selectedTexture = new int[]{4, 0};
			}else if(Input.getKeyDown(Input.KEY_6)){
				selectedTexture = new int[]{5, 0};
			}else if(Input.getKeyDown(Input.KEY_7)){
				selectedTexture = new int[]{6, 0};
			}else if(Input.getKeyDown(Input.KEY_8)){
				selectedTexture = new int[]{7, 0};
			}else if(Input.getKeyDown(Input.KEY_9)){
				selectedTexture = new int[]{8, 0};
			}else if(Input.getKeyDown(Input.KEY_0)){
				selectedTexture = new int[]{9, 0};
			}
			
			if(Input.getKeyDown(Input.KEY_I)){
				selectedTexture = new int[]{0, 1};
			}else if(Input.getKeyDown(Input.KEY_O)){
				selectedTexture = new int[]{1, 1};
			}else if(Input.getKeyDown(Input.KEY_P)){
				selectedTexture = new int[]{2, 1};
			}else if(Input.getKeyDown(Input.KEY_J)){
				selectedTexture = new int[]{3, 1};
			}else if(Input.getKeyDown(Input.KEY_K)){
				selectedTexture = new int[]{4, 1};
			}else if(Input.getKeyDown(Input.KEY_L)){
				selectedTexture = new int[]{5, 1};
			}else if(Input.getKeyDown(Input.KEY_X)){
				selectedTexture = new int[]{6, 1};
			}else if(Input.getKeyDown(Input.KEY_C)){
				selectedTexture = new int[]{7, 1};
			}else if(Input.getKeyDown(Input.KEY_V)){
				selectedTexture = new int[]{8, 1};
			}
			
			if(Input.getMouseWheelValue() == Input.WHEEL_UP){
				if(selectedTexture[0] == 8 && selectedTexture[1] == 1){
					
				}else{
					if(selectedTexture[0] == 9){
						selectedTexture = new int[]{0, selectedTexture[1]+1};
					}else{
						selectedTexture = new int[]{selectedTexture[0]+1, selectedTexture[1]};
					}
				}
			}else if(Input.getMouseWheelValue() == Input.WHEEL_DOWN){
				if(selectedTexture[0] == 0 && selectedTexture[1] == 0){
					
				}else{
					if(selectedTexture[0] == 0){
						selectedTexture = new int[]{0, selectedTexture[1]-1};
					}else{
						selectedTexture = new int[]{selectedTexture[0]-1, selectedTexture[1]};
					}
				}
			}
			
			if(Input.getKeyDown(Input.KEY_N)){
				wait = 2;
				
				collisionObjects = new ArrayList<CollisionObject>();
				
				Thread thread = new Thread("load"){
					public void run(){
						load();
						System.out.println("DONE LOADING");
						wait -= 1;
					}
				};
				
				thread.start();
				
				Thread thread2 = new Thread("loadCollision"){
					public void run(){
						loadCollision();
						System.out.println("DONE LOADING COLLISION");
						wait -= 1;
					}
				};
				
				thread2.start();
			}
			
			if(Input.getKeyDown(Input.KEY_M)){
				wait = 3;
				
				Thread thread = new Thread("save"){
					public void run(){
						save();
						System.out.println("DONE SAVING");
						wait -= 1;
					}
				};
				
				thread.start();
				
				Thread thread2 = new Thread("saveModel"){
					public void run(){
						saveModel();
						System.out.println("DONE SAVING MODEL");
						wait -= 1;
					}
				};
				
				thread2.start();
				
				Thread thread3 = new Thread("saveCollision"){
					public void run(){
						saveCollision();
						System.out.println("DONE SAVING COLLISION");
						wait -= 1;
					}
				};
				
				thread3.start();
			}
		}else{
			System.out.println("STILL PROCESSING");
		}
	}
	
	public int[] getSelectedTexture(){
		return selectedTexture;
	}
	
	@Override
	public void render(Shader shader, Camera camera){
		if(!renderCollision){
			for(int x = 0; x < worldWidth; x++){
				for(int y = 0; y < worldHeight; y++){
					for(int z = 0; z < worldLenght; z++){
						blocks[x][y][z].render(shader, camera);
					}
				}
			}
		}else{
			for(CollisionObject collisionObject : collisionObjects){
				collisionObject.render(shader, camera);
			}
		}
	}
}
