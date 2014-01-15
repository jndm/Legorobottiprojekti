import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class UI2 extends JFrame{
	
	private Ohjain ohjain;
	private Piirtopaneeli pp;
	private HiirenKuuntelija hk;
	private GraphicsDevice vc;
		
	public UI2(){    
	}
	
	private void alustaKomponentit(){
		pp = new Piirtopaneeli(ohjain);
		hk = new HiirenKuuntelija(pp,ohjain);
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = env.getDefaultScreenDevice();
		setUndecorated(true);
		setResizable(false);
		vc.setFullScreenWindow(this);
		
		pp.addMouseListener(hk);
		pp.addMouseMotionListener(hk);
		setVisible(true);
		pp.setPreferredSize(new Dimension(1920,1080));
		setContentPane(pp);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ohjain.lopetaBty();
                System.out.println("ikkuna suljettu");
                System.exit(0);
            }
        };
        addWindowListener(exitListener);
		pack();
	}

	public void rekisteroiOhjain(Ohjain ohjain) {
		this.ohjain = ohjain;
		alustaKomponentit();
	}
	

	public String kysyNimi(){
		String nimi = "123456789";
		while(nimi.length() > 8){
			nimi = JOptionPane.showInternalInputDialog(pp, "Anna nimesi. (max 8 merkkiä)", "Onneksi olkoon!", JOptionPane.DEFAULT_OPTION);
			if(nimi == null)
				nimi = "";
		}
		return nimi;
	}
	
	public void paivita(){
		pp.repaint();
	}

	public void setPainettava(boolean b) {
		pp.setPainettava(b);
	}
}
