package loclock.client;

import com.smartgwt.client.widgets.tile.TileRecord;  

public class StudentRecord extends TileRecord {  
  
    public StudentRecord() {  
    }  
  
    public StudentRecord(String name,  String picture) {  
        this(name, picture, null);  
    }  
  
    public StudentRecord(String name,  String picture, String Profile) {  
        setName(name);  
        
        setPicture(picture);  
        setProfile(Profile);  
    }  
  
    /** 
     * Set the name. 
     * 
     * @param name the name 
     */  
    public void setName(String name) {  
        setAttribute("name", name);  
    }  
  
    /** 
     * Return the name. 
     * 
     * @return the name 
     */  
    public String getName() {  
        return getAttribute("name");  
    }  
 
 
  
    /** 
     * Set the picture. 
     * 
     * @param picture the picture 
     */  
    public void setPicture(String picture) {  
        setAttribute("picture", picture);  
    }  
  
    /** 
     * Return the picture. 
     * 
     * @return the picture 
     */  
    public String getPicture() {  
        return getAttribute("picture");  
    }  
  
    /** 
     * Set the Profile
     * 
     * @param Profile  
     */  
    public void setProfile(String Profile) {  
        setAttribute("Profile", Profile);  
    }  
  
    /** 
     * Return the Profile
     * 
     * @return the Profile
     */  
    public String getProfile() {  
        return getAttribute("Profile");  
    }  
  
  
}  
