import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

public class cardParse{

    private Scene[] allCards;
    
    public Scene[] getAllCards(){
        return this.allCards;
    }

     public Document getDocFromFile(String filename) throws ParserConfigurationException {
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;
            try {
                doc = db.parse(filename);
            } catch (Exception ex) {
                System.out.println("XML parse failure");
                ex.printStackTrace();
            }
            return doc;
        } // exception handling
    }

    public void cardParse(Document d){

        Scene[] deck = new Scene[40];
        Element root = d.getDocumentElement();
        NodeList cards = root.getElementsByTagName("card");
        
        for(int i = 0; i < cards.getLength(); i++){
            Scene curCard = new Scene();
            ArrayList<Role> roles = new ArrayList<Role>();
            
            Node card = cards.item(i);
            
            //set card title
            String cardTitle = card.getAttributes().getNamedItem("name").getNodeValue();
            curCard.setTitle(cardTitle);
            
            //set card img
            String cardImg = card.getAttributes().getNamedItem("img").getNodeValue();
            curCard.setCardImg(cardImg);
            
            //set budget
            String budget = card.getAttributes().getNamedItem("budget").getNodeValue();
            int budgetOb = Integer.parseInt(budget);
            curCard.setBudget(budgetOb);
            
            //reads data
            NodeList children = card.getChildNodes();
            
            for(int j = 0; j < children.getLength(); j++){
            
                Node sub = children.item(j);
                
                if("scene".equals(sub.getNodeName())){
                    //get scene # & description
                    int sceneNumber = Integer.parseInt(sub.getAttributes().getNamedItem("number").getNodeValue());
                    String scene = sub.getTextContent();
                    
                    //load them in
                    curCard.setDescription(scene);
                    curCard.setSceneNum(sceneNumber);
                } else if("part".contentEquals(sub.getNodeName())){
                    Role curRole = new Role();
                    
                    //get roleName
                    String roleName = sub.getAttributes().getNamedItem("name").getNodeValue();
                    
                    //get rank of role
                    String rank = sub.getAttributes().getNamedItem("level").getNodeValue();
                    int level = Integer.parseInt(rank);
                    
                    //get line
                    String lineName = sub.getTextContent();
                    
                    NodeList location = sub.getChildNodes();
                    
                    for(int a = 0; a < location.getLength(); a++){
                        Node _location = location.item(a);
                        
                        if("area".contentEquals(_location.getNodeName())){
                            int locationX = Integer.parseInt(_location.getAttributes().getNamedItem("x").getNodeValue());
                            int locationY = Integer.parseInt(_location.getAttributes().getNamedItem("y").getNodeValue());
                            int locationH = Integer.parseInt(_location.getAttributes().getNamedItem("h").getNodeValue());
                            int locationW = Integer.parseInt(_location.getAttributes().getNamedItem("w").getNodeValue());
                            Location curLocation = new Location(locationX, locationY, locationH, locationW);
                            curRole.setLocation(curLocation);
                        }
                    }
                    
                    curRole.setRank(level);
                    curRole.setName(roleName);
                    curRole.setLine(lineName);
                    curRole.setIsWorked(true); //idk
                    roles.add(curRole);
                }
            }
            curCard.setRoles(roles);
//            curCard.setNumRoles(roles.size());
            deck[i] = curCard;
        }
        this.allCards = deck;
    }
}
