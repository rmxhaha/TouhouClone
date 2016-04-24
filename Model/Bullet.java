package Model;


public class Bullet extends Moveable {
	public final int damage;
	
	public Bullet(int x, int y, int radius, float angle, float speed, int dmg){
		super(x,y,radius,angle,speed);
		damage = dmg;
	}
		
	public void update(float dt){
		
	}
	
	public void interact(Moveable m){
		
	}
}