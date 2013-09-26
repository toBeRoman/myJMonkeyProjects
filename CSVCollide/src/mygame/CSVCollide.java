
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

 

public class CSVCollide extends SimpleApplication implements PhysicsCollisionListener{
 
    private BulletAppState bulletAppState;
    private RigidBodyControl triPhy1;
    private RigidBodyControl triPhy2;
    
 private int bigDirection = 2;
 private int smallDirection = -2;
 Node big = new Node("Big");
 Node small = new Node("Small");

 Geometry tetrahedronSmall;
 Geometry tetrahedronBig;
 
 
  public static void main(String[] args) {
    CSVCollide app = new CSVCollide();
    app.start();
  }
  
 @Override
    public void simpleUpdate(float tpf) {
   /*  CollisionResults results = new CollisionResults();
  big.collideWith(small, results);
  System.out.println("Number of Collisions between" + 
      big.getName()+ " and " + small.getName() + ": " + results.size());
  // Use the results
  if (results.size() > 0) {
    // how to react when a collision was detected
    CollisionResult closest  = results.getClosestCollision();
    System.out.println("What was hit? " + closest.getGeometry().getName() );
    System.out.println("Where was it hit? " + closest.getContactPoint() );
    System.out.println("Distance? " + closest.getDistance() );
  } else {
    // how to react when no collision occured
  }*/
     
        //triPhy1.applyForce(new Vector3f(0, tpf, 0), triPhy1.getPhysicsLocation());
        big.rotate(bigDirection*tpf, 0, 0);
        big.move(bigDirection*tpf, 0, 0);
        small.rotate(smallDirection*tpf, 0, 0);
        small.move(smallDirection*tpf, 0, 0);
        
 
    }
 
  @Override
  public void simpleInitApp() {      
   bulletAppState = new BulletAppState();
   
   stateManager.attach(bulletAppState);
   
   flyCam.setMoveSpeed(100);
   
   CSVConnect obj = new CSVConnect("C:\\Users\\GIOI Staff\\Desktop\\pyramid.csv");
   obj.run();   
   ArrayList<String[]> csvElements = obj.getCSVArray();  
   Vector3f [] vertices = new Vector3f[3];
   Box floor = new Box(10f, 0.1f, 5f);
   floor.scaleTextureCoordinates(new Vector2f(3, 6));
   Geometry floor_geo = new Geometry("Floor", floor);
   Material floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key3 = new TextureKey("Textures/Terrain/Pond/Pond.jpg");
    key3.setGenerateMips(true);
    Texture tex3 = assetManager.loadTexture(key3);
    tex3.setWrap(Texture.WrapMode.Repeat);
    floor_mat.setTexture("ColorMap", tex3);
   floor_geo.setMaterial(floor_mat);
   floor_geo.setLocalTranslation(0, -0.1f, 0);
    this.rootNode.attachChild(floor_geo);
    /* Make the floor physical with mass 0.0f! */
    RigidBodyControl floor_phy = new RigidBodyControl(0.0f);
    floor_geo.addControl(floor_phy);
    bulletAppState.getPhysicsSpace().add(floor_phy);
   if(csvElements.size()>0){
           
           for(String[] line : csvElements){
                //Vetex coordinates
               
                vertices[0] = new Vector3f(Float.parseFloat(line[0]),
                                           Float.parseFloat(line[1]),
                                           Float.parseFloat(line[2]));           
                
                vertices[1] = new Vector3f(Float.parseFloat(line[3]),
                                           Float.parseFloat(line[4]),
                                           Float.parseFloat(line[5])); 
                
                vertices[2] = new Vector3f(Float.parseFloat(line[6]),
                                           Float.parseFloat(line[7]),
                                           Float.parseFloat(line[8]));   
                int [] indexes = { 2,0,1 };
                
                 Mesh customMesh = new Mesh();
                 customMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
                 customMesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));                
                 customMesh.updateBound();
                 
                 tetrahedronSmall = new Geometry("My Mesh",customMesh);  
                 tetrahedronSmall.setLocalTranslation(0, 0, 0);
                 Material niceMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                 niceMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
                 niceMat.setColor("Color", ColorRGBA.randomColor());
                 tetrahedronSmall.setMaterial(niceMat);
                 small.attachChild(tetrahedronSmall);
                 small.setLocalTranslation(9, 5, -5);
                 
                 
                 
                 tetrahedronBig = new Geometry("My Mesh2",customMesh);       
                 tetrahedronBig.setLocalTranslation(0, 0, 0);
                 //tetrahedronBig.scale(2);
                 Material okayMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                 okayMat.setColor("Color", ColorRGBA.randomColor());
                 tetrahedronBig.setMaterial(okayMat);
                 big.attachChild(tetrahedronBig);
                 big.setLocalTranslation(-3, 5, -5);
                 
                   /** Must add a light to make the lit object visible! */
                DirectionalLight sun = new DirectionalLight();
                sun.setDirection(new Vector3f(1,0,-2).normalizeLocal());
                sun.setColor(ColorRGBA.White);
                rootNode.addLight(sun);
                rootNode.attachChild(big);
                rootNode.attachChild(small);
                
                
                triPhy1 = new RigidBodyControl(0f);
                tetrahedronSmall.addControl(triPhy1);
                
                
                
                triPhy2 = new RigidBodyControl(0f);
                tetrahedronBig.addControl(triPhy2);
                
                bulletAppState.getPhysicsSpace().add(triPhy1);
                bulletAppState.getPhysicsSpace().add(triPhy2);
               
              
           }
           
           
           
       
   }
  }

    public void collision(PhysicsCollisionEvent event) {
        
    }
}