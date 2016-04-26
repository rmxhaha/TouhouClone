package Model;

import javax.swing.JPanel;
import java.awt.Color;
import java.util.Vector;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.stream.Collectors;
import java.lang.Error;
import Model.Event.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Battlefield extends JPanel {
	public final int width;
	public final int height;
	private Vector<Movable> mList;
	private AssetLoader assetLoader;
	private JFrame frame;
	private ScheduledExecutorService scheduledPool;


	
	private static int getDistance(Movable a, Movable b){
		int dx = a.getX() - b.getX();
		int dy = a.getY() - b.getY();
		return dx*dx+dy*dy;
	}
	
	public Battlefield(){
		width = 600;
		height = 800;
		mList = new Vector<>();
		assetLoader = new AssetLoader();
		scheduledPool = Executors.newScheduledThreadPool(1);
		
		frame = new JFrame("Touhou Clone");
		frame.setSize(600,800);
		frame.setResizable(false);
		frame.setVisible(true);

		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		
		
		Boss boss = new Boss(100,100,2000);
		add( boss );
		
		scheduledPool.scheduleWithFixedDelay(new UpdateEvent(this,0.02f), 0, 20, TimeUnit.MILLISECONDS);
		
		int t = 0;
		for( int i = 0; i < 10; ++ i ){
			t += 200;
			scheduledPool.schedule(new FlowerEvent(this,10,i*10.f,1500), t, TimeUnit.MILLISECONDS);
		}
		

		for( int i = 0; i < 10; ++ i ){
			t += 200;
			scheduledPool.schedule(new FlowerEvent(this,10,-i*10.f,1500), t, TimeUnit.MILLISECONDS);
		}
		
		for( int i = 0; i < 10; ++ i ){
			t += 200;
			scheduledPool.schedule(new FlowerEvent(this,10,i*10.f,1500), t, TimeUnit.MILLISECONDS);
		}
	}
	
	public void add(Movable m){
		mList.add(m);
	}
	
	public Boss getBoss() throws Error {
		for( Movable m : mList ){
			if( m instanceof Boss )
				return (Boss)m;
		}
		
		
		throw new Error("no boss");
	}
	
	public void update(float dt){
		// updates Movable
		for( Movable it : mList ){
			it.update(dt);
		}
		
		// interact when collide
		for( int i = 0; i < mList.size(); ++ i ){
			for( int k = i + 1; k < mList.size(); ++ k ){
				Movable first = mList.get(i);
				Movable second = mList.get(k);

				if( getDistance(first,second) < first.getRadius() + second.getRadius()){
					first.interact(second);
					if( first.isAlive() && second.isAlive() ){
						second.interact(first);
					}
				}
			}
		}
		
		// clear dead stuff
		/*
		mList = mList.stream()
				.filter(Movable::isAlive)
				.collect(Collectors.toCollection(Vector::new));
		*/
	}
	

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.white);
		
		for( Movable it : mList ){
            int x = it.getX();
            int y = it.getY();

            if( it.isDead() ) continue;

            if( it instanceof Boss )
                g.drawImage(assetLoader.getBossImage(), x, y,85,127,null);
            else if( it instanceof Player )
                g.drawImage(assetLoader.getPlayerImage(), x, y,65,125,null);
            else if( it instanceof Bullet )
                g.drawImage(assetLoader.getBulletImage(0), x, y, null );
        }
        
    }
	
	
}