import java.util.ArrayList;

public class Scene {

	private int budget; //1-6
	private String title;
	private String desc;
	private ArrayList<Role> roles;
	
	public Scene(String title, String desc, int budget, ArrayList<Role> roles) {
		this.title = title;
		this.desc = desc;
		this.budget = budget;
		this.roles = roles;
	}
	
	/* Getters */
	public int getBudget() {
		return this.budget;
	}
	
	public String getSceneTitle() {
		return this.title;
	}

	public String getSceneDesc() {
		return this.desc;
	}
	
	public ArrayList<Role> getRoles() {
		return this.roles;
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
