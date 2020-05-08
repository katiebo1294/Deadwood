import java.util.ArrayList;

public class Scene {

	private int budget; //1-6
	private String title;
	private String desc;
	private int sceneNum;
	private String cardImg;
	private ArrayList<Role> roles;
	
	public Scene(){}
	public Scene(String title, String cardImg, String desc, int sceneNum, int budget, ArrayList<Role> roles) {
		this.title = title;
		this.cardImg = cardImg;
		this.desc = desc;
		this.sceneNum = sceneNum;
		this.budget = budget;
		this.roles = roles;
	}
	
	/* Getters */
	public int getBudget() {
		return this.budget;
	}
	
	public String getCardImg(){
        return this.cardImg;
	}
	
	public String getTitle() {
		return this.title;
	}

	public String getSceneDesc() {
		return this.desc;
	}
	
	public int getSceneNum(int sceneNum){
        return this.sceneNum;
	}
	
	//Setters
	public void setBudget(int budget){
        this.budget = budget;
	}
	
    public void setCardImg(String cardImg){
        this.cardImg = cardImg;
	}
	
	public void setTitle(String title){
        this.title = title;
	}
	
	public void setDescription(String desc){
        this.desc = desc;
	}
	
	public void setSceneNum(int sceneNum){
        this.sceneNum = sceneNum;
	}
	
	public ArrayList<Role> getRoles() {
		return this.roles;
	}
	
	public void setRoles(ArrayList<Role> roles){
        this.roles = roles;
	}
	
	public boolean actorsOnCard() {
		boolean result = false;
		for(Role r : this.roles) {
			if(r.getIsWorked()) {
				result = true;
			}
		}
		return result;
	}
	
	/* Lists the name of each role in this.roles in a comma-separated list */
	public String listRoles(int rank) {
		String result = "";
		if(this.getRoles().get(0).getRank() <= rank) {
			result = this.getRoles().get(0).getName();
		}
		for(int i = 1; i < this.getRoles().size(); i++) {
			if(this.getRoles().get(i).getRank() <= rank) {
				result += ", " + this.getRoles().get(i).getName();
			}
		}
		return result;
	}
}
