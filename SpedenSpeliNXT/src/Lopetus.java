import lejos.nxt.Button;


public class Lopetus extends Thread{
	
	private boolean kaynnissa;
	private Paaluokka pl;
	
	public Lopetus(Paaluokka pl){
		this.pl = pl;
		kaynnissa = true;
	}
	
	public void run(){
		while(kaynnissa){
			if(Button.readButtons()==Button.ID_ESCAPE)
				kaynnissa=false;
		}
		pl.lopeta();
	}

}
