import org.w3c.dom.Element;

public class CastingOffice extends Room {

	private static int[][] priceList; // stores the cost to upgrade to each rank (see below)
	
	/* Rank | Dollars | Credits
	 *   2       4         5   
	 *   3      10        10   
	 *   4      18        15   
	 *   5      28        20   
	 *   6      40        25   
	 */
	
	public CastingOffice(Element room) {
		super(room);
		this.name = "Casting Office";
		priceList = new int[room.getElementsByTagName("upgrade").getLength() / 2][2];
		int currencyType = 0;
		for(int i = 0; i < priceList.length; i++) {
			priceList[i][currencyType] = Integer.parseInt(((Element) room.getElementsByTagName("upgrade").item((currencyType+1) * i)).getAttribute("amt"));
		}
	}
	
	/* Getters */
	
	// returns a numerical/data representation of the list of upgrade prices
	public int[][] getPriceList() {
		return priceList;
	}
	
	// returns an ASCII chart-like display representation of the list of upgrade prices
	public String displayPriceList() {
		String result = "Rank | Dollars | Credits";
		for(int i = 0; i < priceList.length; i++) {
			result += "\n  " + i + "       " + priceList[i][0] + "         " + priceList[i][1] + "   \n";
		}
		return result;
	}
}
