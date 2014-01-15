
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;


public class Paaluokka {

	private volatile boolean kaynnissa;
	private Pyoritys pyoritys;
	private final boolean DEBUG = false;
	private long aika;
	private Demotila demo;
	
	public Paaluokka(){
		TouchSensor nappi1 = new TouchSensor(SensorPort.S1);
		TouchSensor nappi2 = new TouchSensor(SensorPort.S2);
		TouchSensor nappi3 = new TouchSensor(SensorPort.S3);
		TouchSensor nappi4 = new TouchSensor(SensorPort.S4);
		
		boolean peliKaynnissa=false;
		kaynnissa = true;
		int painettuNappi=0;
		int pisteet=0;
		Sound.setVolume(20);
		
		BTConnection btc = Bluetooth.waitForConnection();
		DataInputStream dis = btc.openDataInputStream();
		DataOutputStream dos = btc.openDataOutputStream();
		
		pyoritys = new Pyoritys();
		Lopetus lopetus = new Lopetus(this);
		lopetus.start();
		demo = new Demotila();
		
		while(kaynnissa){
			try {
				int tila = dis.read();
				if(tila==1){
					pyoritys = new Pyoritys();
					pyoritys.start();
					peliKaynnissa = true;
					pisteet = 0;
				}
				else if(tila==2){
					System.out.println("k‰ynnistet‰‰n demo");
					demo = new Demotila();
					demo.start();
					while(dis.read()!=3);
					System.out.println("lopetetaan demo");
					demo.lopeta();
					tila=0;
					pisteet=0;
				}
				else if(tila==9){
					dis.close();
					dos.close();
					kaynnissa = false;
					pyoritys.lopeta();
					demo.lopeta();
				}
				else;		
			}catch (IOException e1) {}
			if(peliKaynnissa){
				while(peliKaynnissa){
					int painettava = -2;
					painettuNappi=-1;
					if(nappi1.isPressed()){
						painettuNappi=0;
						aika = System.currentTimeMillis();
					}
					if(nappi2.isPressed()){
						painettuNappi=1;
						aika = System.currentTimeMillis();
					}
					if(nappi3.isPressed()){
						painettuNappi=2;
						aika = System.currentTimeMillis();
					}
					if(nappi4.isPressed()){
						painettuNappi=3;
						aika = System.currentTimeMillis();
					}
					
					switch(painettuNappi){
						case 0:
							painettava = pyoritys.pollaaKasa();
							break;
						case 1:
							painettava = pyoritys.pollaaKasa();
							break;
						case 2:
							painettava = pyoritys.pollaaKasa();
							break;
						case 3:
							painettava = pyoritys.pollaaKasa();
							break;
					}
					
					if(painettuNappi == painettava){
						pisteet++;
						if(DEBUG){
							System.out.println(painettuNappi + " : " + painettava);
						}else{
							lejos.nxt.LCD.clear();
							System.out.println("\n\n\n\t"+pisteet);
						}
					}else if(painettuNappi != -1){
						peliKaynnissa=false;
						pyoritys.lopeta();
						Sound.buzz();
						System.out.println("Peli loppui");
						if(DEBUG){
							System.out.println(painettuNappi + " : " + painettava);
						}else{
							lejos.nxt.LCD.clear();
							System.out.println("\n\n\n\t"+pisteet);
						}
					}
					while(nappi1.isPressed() && painettuNappi==0);
					while(nappi2.isPressed() && painettuNappi==1);
					while(nappi3.isPressed() && painettuNappi==2);
					while(nappi4.isPressed() && painettuNappi==3);
					while(System.currentTimeMillis()<(aika+200));
					
				}
				try {
					dos.write(pisteet);
					dos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				pyoritys.lopeta();
			}
		}
		System.exit(0);
	}
	
	public static void main(String[] args) {
		new Paaluokka();
	}

	public void lopeta() {
		pyoritys.lopeta();
		demo.lopeta();
		kaynnissa = false;
	}
}
