import org.w3c.dom.Element;

public class Scene {

	private String title;
	private int budget;
	private String sceneNum;
	private String desc;
	private Role[] roles;
	
	public Scene(Element card) {
		this.title = card.getAttribute("name");
		this.budget = Integer.parseInt(card.getAttribute("budget"));
		this.sceneNum = ((Element) card.getElementsByTagName("scene").item(0)).getAttribute("number");
		this.desc = ((Element) card.getElementsByTagName("scene").item(0)).getTextContent().trim();
		this.roles = new Role[card.getElementsByTagName("part").getLength()];
		for(int i = 0; i < roles.length; i++) {
			this.roles[i] = new Role(((Element) card.getElementsByTagName("part").item(i)));
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
	public Role[] getRoles() {
		return this.roles;
	}
	
	public boolean actorsOnCard() {
		boolean result = false;
		for(Role r : this.roles) {
			if(r.isWorked()) {
				result = true;
			}
		}
		return result;
	}
	
	public String listAvailableRoles(int rank) {
		String result = "";
		if(this.getRoles()[0].getRank() <= rank) {
			result = this.getRoles()[0].getName();
		}
		for(int i = 1; i < this.getRoles().length; i++) {
			if(this.getRoles()[0].getRank() <= rank) {
				result += ", " + this.getRoles()[0].getName();
			}
		}
		return result;
	}
	
	public String listRoles() {
		if(this.getRoles().length > 0) {
			String result = "[" + this.getRoles()[0].toString();
			for(int i = 1; i < this.getRoles().length; i++) {
				result += ", " + this.getRoles()[i].toString();
			}
			result += "]";
			return result;
		} else {
			return "[none]";
		}
	}
	
	public  Role lookUpRole(String desiredRole) {
		for(Role role : roles) {
			if(getTitle().equalsIgnoreCase(desiredRole)) {
				return role;
			}
		}
		return null;
	}
	
	public String toString() {
		return "title = " + this.title + ", budget = " + this.budget + ", description = \"Scene " + this.sceneNum + ": " + this.desc + "\", roles = " + this.listRoles();
	}
}
