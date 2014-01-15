
import lejos.nxt.Motor;

public class Demotila extends Thread{
	
	private volatile boolean kaynnissa;
	
	public Demotila(){
		kaynnissa = true;
		Motor.A.setSpeed(720);
		Motor.C.setSpeed(720);
	}
	
	public void run(){
		int rand = 0;
		
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(kaynnissa){
			rand = (int)(Math.random()*4);
			
			switch(rand){
				case 0:
					Motor.A.rotate(30);
					break;
				case 1:
					Motor.A.rotate(-30);
					break;
				case 2:
					Motor.C.rotate(30);
					break;
				case 3:
					Motor.C.rotate(-30);
					break;
			}
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch(rand){
				case 0:
					Motor.A.rotate(-30);
					break;
				case 1:
					Motor.A.rotate(30);
					break;
				case 2:
					Motor.C.rotate(-30);
					break;
				case 3:
					Motor.C.rotate(30);
					break;
			}
		}
	}
	
	public void lopeta(){
		kaynnissa=false;
	}
}
