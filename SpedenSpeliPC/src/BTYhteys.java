import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;


public class BTYhteys extends Thread {

	private NXTComm nxtComm;
	private NXTInfo[] nxtInfo;
	private int pisteet;
	private volatile boolean kaynnissa=false, kaynnista, demoKaynnista=false, demoKaynnissa,sammuta;
	private Ohjain ohjain;
	
	public BTYhteys(Ohjain ohjain){
		this.ohjain = ohjain;
		kaynnista = false;
		kaynnissa = true;
		sammuta = false;
	}
	
	public void run(){
		long edellinen = System.currentTimeMillis();
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			nxtInfo = nxtComm.search("Ryhma6");
		} catch (NXTCommException e1) {
		}
		if (nxtInfo.length>0){
			NXTInfo info = nxtInfo[0];
			try {
				nxtComm.open(info);
				ohjain.setPainettava(true);
				InputStream is = nxtComm.getInputStream();
				OutputStream os = nxtComm.getOutputStream();
				DataInputStream dis = new DataInputStream(is);
				DataOutputStream dos = new DataOutputStream(os);

				while(kaynnissa){
					if(demoKaynnista){
						demoKaynnissa=true;
						dos.write(2);
						dos.flush();
						while(demoKaynnissa);
						demoKaynnista=false;
						dos.write(3);
						dos.flush();
					}
					if(kaynnista){
						dos.write(1);
						dos.flush();
					
						pisteet=dis.read();
						System.out.println(pisteet);
						ohjain.lisaaTulos(pisteet);
						kaynnista = false;
					}
					if(sammuta){
						dos.write(9);
						dos.flush();						
					}
					
					if(System.currentTimeMillis() > edellinen+2000){
						dos.write(12);
						dos.flush();
						edellinen = System.currentTimeMillis();
					}
				}
				
				dis.close();
				is.close();
				dos.close();
				os.close();
			} catch (Exception e) {
				System.out.println("Epäonnistui...");
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Robottia ei löytynyt.");
		}
		
		ohjain.setPainettava(false);
	}

	public void lopeta(){
		sammuta = true;
		kaynnissa = false;
	}
	
	public boolean getKaynnista(){
		return kaynnista;
	}
	
	public void kaynnistaPeli() {
		kaynnista=true;
	}

	public void kaynnistaDemo() {
		demoKaynnista = true;		
	}

	public void lopetaDemo() {
		demoKaynnissa = false;
	}
}
