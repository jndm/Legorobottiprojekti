import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;


public class Piirtopaneeli extends JPanel{
	
	final String FONTNAME = "Arial";
	final int FONTSTYLE = Font.PLAIN;
	private Ohjain ohjain;
	private Image bluetooth,taustakuva,banaani;
	private Rectangle top, ohjeet, aloita, btnappi, tausta;
	private boolean painettava;
	
	public Piirtopaneeli(Ohjain ohjain){
		this.ohjain = ohjain;
		bluetooth = Toolkit.getDefaultToolkit().getImage("images/Bluetooth.png");
		taustakuva = Toolkit.getDefaultToolkit().getImage("images/taustakuva.jpg");
		banaani = Toolkit.getDefaultToolkit().getImage("images/Mlogo.png");
		painettava = false;
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		//AA p‰‰lle
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		g.setColor(Color.BLACK);
		
		//Luodaan neliˆt top10, ohjeille ja nappulalle (int)(this.getWidth()*0.62)
		tausta = new Rectangle((int)(this.getWidth()*0.04), (int)(this.getHeight()*0.12), (int)(this.getWidth()*0.92), (int)(this.getHeight()*0.8));
		top = new Rectangle((int)(this.getWidth()*0.04), (int)(this.getHeight()*0.12), (int)(this.getWidth()*0.62),(int)(this.getHeight()*0.6));
		ohjeet = new Rectangle(top.x+top.width, (int)(this.getHeight()*0.12), (int)(this.getWidth()*0.30),(int)(this.getHeight()*0.6));
		aloita = new Rectangle(top.x+(int)(top.width*0.25), top.y+top.height+(int)(this.getHeight()*0.00), (int)(top.width*0.5), (int)((tausta.height-top.height)*0.70));
		btnappi = new Rectangle(this.getWidth()-42, 10, bluetooth.getWidth(this), bluetooth.getHeight(this));

		g.drawImage(taustakuva, 0, 0, this);
		
		//Bluetooth kuvan h‰ivytys
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
		g.drawImage(bluetooth, this.getWidth()-42, 10, this);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g.fillRect(tausta.x, tausta.y, tausta.width, tausta.height);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
		g.setColor(new Color(200,200,240));
		//Piirret‰‰n neliˆt
		/*g.drawRect(top.x, top.y, top.width, top.height);
		g.drawRect(ohjeet.x, ohjeet.y, ohjeet.width, ohjeet.height);*/
		
		//Luodaan fontti ja haetaan sen rajat ja lasketaan fontin rajojen suhde fontin kokoon
		int size = 12;
		Font font = new Font(FONTNAME, FONTSTYLE, size);
		FontMetrics fontm = g.getFontMetrics(font);
		Rectangle2D sBounds = fontm.getStringBounds("Asd!",g);
		double ratio = sBounds.getHeight()/size;

		int fontSize;
		Font fontti;
		FontMetrics fm;
		String teksti;
		
		//S‰‰det‰‰n otsikon koko
		fontSize = (int)((this.getHeight()*0.1)/ratio);
		fontti = new Font(FONTNAME, FONTSTYLE, fontSize);
		fm = g.getFontMetrics(fontti);
		teksti = "Speden Speli";
		sBounds = fm.getStringBounds(teksti, g);
		g.setFont(fontti);
		
		//Piirret‰‰n otsikko
		g.drawString(teksti, (int)(this.getWidth()/2.0-sBounds.getWidth()/2.0), (int)(this.getHeight()*0.01)+fm.getAscent());
		
		fontSize = (int)((this.getHeight()*0.08)/ratio);
		fontti = new Font(FONTNAME, FONTSTYLE, fontSize);
		fm = g.getFontMetrics(fontti);
		teksti = "Top-10";
		sBounds = fm.getStringBounds(teksti, g);
		g.setFont(fontti);
		
		//Piirret‰‰n v‰liotsikot (Ohjeet & Top10)
		g.drawString(teksti, top.x + (int)(top.width/2.0-sBounds.getWidth()/2.0), top.y+fm.getAscent()+(int)(this.getHeight()*.01));
		g.drawString("Ohjeet", ohjeet.x + (int)(ohjeet.width/2.0-sBounds.getWidth()/2.0), ohjeet.y+fm.getAscent()+(int)(this.getHeight()*.01));

		fontSize = (int)((top.getHeight()*0.10)/ratio);
		fontti = new Font(FONTNAME, FONTSTYLE, fontSize);
		fm = g.getFontMetrics(fontti);
		teksti = "Top-10";
		sBounds = fm.getStringBounds(teksti, g);
		g.setFont(fontti);

		//Piirret‰‰n top10-taulukko
		int x=top.x+(int)(top.width*0.05);
		double xp = top.width*0.5;
		int y=top.y+(int)(top.height*0.2+fm.getAscent());
		int i = 0;
		for(Pisteet p:ohjain.getPisteet()){
			g.drawString(++i + ". " + p.getNimi(), x, y);
			Rectangle2D pBounds = fm.getStringBounds(""+p.getPisteet(), g);
			g.drawString(""+p.getPisteet(), (int)(xp-pBounds.getWidth()), y);
			y += (int)(top.height*0.15);
			
			if(i==5){
				x=top.x+(int)(top.width*0.55);
				xp = top.width;
				y=top.y+(int)(top.height*0.2+fm.getAscent());
			}
		}
		
		//Piirret‰‰n ohjeet
		String[] ohjerivit = ohjain.getOhjeet().split("\n");
		
		fontSize = (int)((ohjeet.getHeight()*0.08)/ratio);
		fontti = new Font(FONTNAME, FONTSTYLE, fontSize);
		fm = g.getFontMetrics(fontti);
		teksti = ohjerivit[0];
		sBounds = fm.getStringBounds(teksti, g);
		g.setFont(fontti);
		
		x=ohjeet.x+(int)(ohjeet.width*0.05);
		y=ohjeet.y+(int)(ohjeet.height*0.2+fm.getAscent());
		i = 0;
		
		for(String ohje : ohjerivit){
			g.drawString(ohje.split("\t")[0], (int)(x), (int)(y+fm.getAscent()*i));
			g.drawString(ohje.split("\t")[1], (int)(x+ohjeet.width*0.1), (int)(y+fm.getAscent()*i++));
		}
		
		g.setColor(Color.GRAY);
		g.drawLine(top.x+(int)(top.width/2.0),top.y+(int)(top.height*0.2), top.x+(int)(top.width/2.0), top.y+(int)(top.height*0.9));
		g.drawLine(top.x+top.width, tausta.y+(int)((tausta.height)*0.05), top.x+top.width, tausta.y+(int)((tausta.height)*0.95));
		g.setColor(new Color(200,200,240));
		
		fontSize = (int)((aloita.height)/ratio);
		fontti = new Font(FONTNAME, FONTSTYLE, fontSize);
		fm = g.getFontMetrics(fontti);
		teksti = "ALOITA";
		sBounds = fm.getStringBounds(teksti, g);
		g.setFont(fontti);
		
		if(painettava)
			g.setColor(new Color(39, 174, 96));
		else
			g.setColor(new Color(192, 57, 43));
		g.drawRoundRect(aloita.x, aloita.y, aloita.width, aloita.height, 50, 50);
		//g.drawRect(aloita.x, aloita.y, (int)sBounds.getWidth(), (int)sBounds.getHeight());
		g.setColor(new Color(200,200,240));
		
		//Piirret‰‰n aloitanappulan teksti
		g.drawString(teksti, aloita.x+(int)((aloita.width)/2.0-(sBounds.getWidth()/2.0)), aloita.y+fm.getAscent());

		g.drawImage(banaani, (int)(ohjeet.x+ohjeet.width/2.0-70), (int)(ohjeet.y+ohjeet.height-140), 292, 312, this);
		
		getToolkit().sync();
	}

	public Rectangle getAloita() {
		return aloita;
	}

	public Rectangle getBtnappi() {
		return btnappi;
	}

	public void setPainettava(boolean b) {
		painettava = b;
	}

	public boolean getPainettava() {
		return painettava;
	}
	
	
}
