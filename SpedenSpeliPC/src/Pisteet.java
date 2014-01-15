import java.io.Serializable;

public class Pisteet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4979664776678675455L;
	private String nimi;
	private int pisteet;

	public Pisteet(String nimi, int pisteet){
		this.nimi=nimi;
		this.pisteet=pisteet;
	}
	public String toString(){
		return nimi+": "+pisteet;
	}
	
	public int getPisteet() {
		return pisteet;
	}
	
	public String getNimi() {
		return nimi;
	}
	
}
