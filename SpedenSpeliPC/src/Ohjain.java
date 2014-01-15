import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Ohjain extends Thread{

	private BTYhteys bty;
	private ArrayList<Pisteet> pisteet;
	private UI2 nakyma;
	private boolean kaynnissa=true, demoKaynnissa=false;
	private String ohjeet;
	private long aika;
	
	public Ohjain(UI2 ui){
		pisteet = new ArrayList<Pisteet>();
		ohjeet = "";
		avaaTiedosto();
		avaaOhjeet();
		this.nakyma = ui;
		ui.rekisteroiOhjain(this);
		aika=System.currentTimeMillis();
		bty = new BTYhteys(this);
	}
	public void run(){
		while(kaynnissa){
			nakyma.paivita();
			odota();
			vertaaAika();
		}
	}
	public void kaynnista(){
		if(!bty.isAlive()){
			bty = new BTYhteys(this);
			System.out.println("Kaynnistetaan Bluetoothyhteys!");
			bty.start();
		}else{
			bty.lopeta();
		}
	}

	public void kaynnistaPeli() {
		if(bty.isAlive()){
			bty.kaynnistaPeli();
		}
	}
	
	public ArrayList<Pisteet> getPisteet(){
		return pisteet;
	}
	
	private void avaaTiedosto(){
		try{
			FileInputStream tiedosto = new FileInputStream("data/pisteet.dat");
	        ObjectInputStream syote = new ObjectInputStream(tiedosto);
	        try{
	        	pisteet = (ArrayList)syote.readObject();
	        }catch(Exception e){
	        	e.printStackTrace();
	        	System.out.println("huehue");
	        }
       		syote.close();
       		for(Pisteet p : pisteet){
       			System.out.println(p);
       		}
		}catch(IOException e){
			System.out.print("asdasasd");
			e.printStackTrace();
		}
	}
	private void tallennaTiedosto(){
		try{
            FileOutputStream tiedosto = new FileOutputStream("data/pisteet.dat");
            ObjectOutputStream tuloste = new ObjectOutputStream(tiedosto);
            tuloste.writeObject(pisteet);
            tuloste.close();
        }catch(IOException e){
            e.printStackTrace();
        }
	}
	
	public void lisaaTulos(int p){
		boolean listalla=false;
		boolean lopeta=false;
		if(pisteet.size()!=0 && p>0){
			for(int i=pisteet.size()-1;i>=0 && !lopeta;--i){
				if(p > pisteet.get(i).getPisteet()){
					if(i==0)
						lopeta=true;
					listalla=true;
				}
				else 
					lopeta=true;
				
				if((listalla || pisteet.size()<10) && lopeta){
					String nimi = nakyma.kysyNimi();
					if(p>pisteet.get(0).getPisteet())
						i=-1;
					if(nimi!=null && nimi.length()>0)	
						pisteet.add(i+1, new Pisteet(nimi,p));
				}
			}
		}
		else if(p>0){
			String nimi = nakyma.kysyNimi();
			if(nimi!=null && nimi.length()>0)
				pisteet.add(new Pisteet(nimi,p));
		}
		if(pisteet.size()>10)
			pisteet.remove(10);
		tallennaTiedosto();
		nakyma.setPainettava(true);
		setAika(System.currentTimeMillis());
	}
	
	public void odota(){
		try {
			Thread.sleep(1000/60);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setPainettava(boolean b) {
		nakyma.setPainettava(b);
	}
	
	public void avaaOhjeet(){
		try{
			FileInputStream tiedosto = new FileInputStream("data/ohjeet.txt");
	        InputStreamReader syote = new InputStreamReader(tiedosto);
	        try{
	        	Scanner sc = new Scanner(tiedosto);
	        	while(sc.hasNext()){
	        		ohjeet = ohjeet.concat(sc.nextLine()+"\n");
	        	}
	        	System.out.print(ohjeet);
	        }catch(Exception e){
	        	e.printStackTrace();
	        	System.out.println("huehue");
	        }
       		syote.close();
       		
		}catch(IOException e){
			System.out.print("asdasasd");
			e.printStackTrace();
		}
	}
	
	public String getOhjeet() {
		return ohjeet;
	}
	public void setAika(long aika) {
		this.aika = aika;
	}
	public void vertaaAika(){
		long tamaaika = System.currentTimeMillis();
		if(tamaaika >= (aika + (1000*30*60)) && !demoKaynnissa && !bty.getKaynnista()){
			bty.kaynnistaDemo();
			demoKaynnissa = true;
		}
		else if(tamaaika <= (aika + (1000*10*60)) && demoKaynnissa){
			bty.lopetaDemo();
			demoKaynnissa = false;
		}
	}
	public void lopetaBty() {
		bty.lopeta();
	}
}
