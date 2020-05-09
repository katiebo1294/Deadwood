import java.util.ArrayList;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Scene {

	private String title;
	private int budget;
	private String sceneNum;
	private String desc;
	private ArrayList<Role> roles;
	
	public Scene(Node card) {
		this.title = ((Element) card).getAttribute("name");
		this.budget = Integer.parseInt(((Element) card).getAttribute("budget"));
		Node scene = card.getFirstChild();
		this.sceneNum = ((Element) scene).getAttribute("number");
		this.desc = ((Element) scene).getAttribute("desc");
		NodeList roles = scene.getChildNodes();
		for(int i = 0; i < roles.getLength(); i++) {
			if(roles.item(i).getNodeName().equals("part")) {
				this.roles.add(new Role(roles.item(i)));
			}
		}
	}
	
	/* Getters */
	public int getBudget() {
		return this.budget;
	}
	
	
	public String getTitle() {
		return this.title;
	}

	public String getSceneDesc() {
		return this.desc;
	}
	
	public String getSceneNum(String sceneNum){
        return this.sceneNum;
	}
	
	/* Setters */
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
