import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class HiirenKuuntelija extends MouseAdapter{
	
	private Ohjain ohjain;
	private Piirtopaneeli piirto;
	private Rectangle aloita;
	private Rectangle bt;
	
	public HiirenKuuntelija(Piirtopaneeli piirto, Ohjain ohjain){	
		this.piirto=piirto;
		this.ohjain = ohjain;
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		ohjain.setAika(System.currentTimeMillis());
		aloita = piirto.getAloita();
		bt = piirto.getBtnappi();
		if(aloita.contains(me.getX(), me.getY()) && piirto.getPainettava()){
			ohjain.kaynnistaPeli();
		}
		if(bt.contains(me.getX(), me.getY())){
			ohjain.kaynnista();
		}
	}
	@Override
	public void mouseEntered(MouseEvent me) {
	}
	@Override
	public void mouseExited(MouseEvent me) {}
	@Override
	public void mousePressed(MouseEvent me) {}
	@Override
	public void mouseReleased(MouseEvent me) {}
	
	public void mouseMoved(MouseEvent me){
		ohjain.setAika(System.currentTimeMillis());
	}

}
