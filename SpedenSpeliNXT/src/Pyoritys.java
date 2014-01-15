import java.util.ArrayList;
import java.util.Iterator;

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Pyoritys extends Thread{
	
	private boolean kaynnissa;
	private ArrayList<Integer> kasa;
	private int arvo=99;
	private boolean pollaamassa=false;
	
	public Pyoritys(){
		kaynnissa = true;
		kasa = new ArrayList<Integer>();
		Motor.A.setSpeed(720);
		Motor.C.setSpeed(720);
	}
	
	public void run(){
		int aika = 1000;
		int rand = 0;
		
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(kaynnissa){
			rand = (int)(Math.random()*4);
			//rand = (rand+1)%4;
			lisaaKasaan(rand);
			
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
				sleep(aika);
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
			if(aika>0){
				aika=aika-20;
			}
			if(Button.readButtons()!=0){
				kaynnissa=false;
			}
		}
	}
	/*
	public void lisaaKasaan(int arvo){
		while(pollaamassa){}
		kasa.add(0,Integer.valueOf(arvo));
		System.out.println(kasa.get((kasa.size()-1))+1);
	}
	
	public int pollaaKasa() {
		pollaamassa=true;
		if(kasa.size()!=0){
			arvo = kasa.get(kasa.size()-1);
			kasa.remove(kasa.size()-1);
		}
		else {
			arvo=99;
		}
		pollaamassa=false;
		System.out.println("Pollataan");
		return arvo;
		
	}*/
	
	public synchronized void lisaaKasaan(int arvo){
		kasa.add(0,Integer.valueOf(arvo));
	}
	
	public synchronized int pollaaKasa() {
		Iterator ite = kasa.iterator();
		if(ite.hasNext()){
			arvo = (int)ite.next();
		    while(ite.hasNext()){
		        arvo = (int)ite.next();
		    }
	        ite.remove();
		}
		else {
			arvo=99;
		}
		return arvo;
		
	}
	
	public void lopeta(){
		kaynnissa=false;
	}
}
