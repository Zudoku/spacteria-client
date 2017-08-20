/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.gameplay.objects.player;

/**
 * Created Aug 20, 2017
 * @author arska
 */
public class DummyCharacterCharacterData {
    private String name;
    private int level;
    private int experience;
    private int userid;
    private int uniqueid;
    private int cclass;
    private String created;

    public int getCclass() {
        return cclass;
    }

    public String getCreated() {
        return created;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getUniqueid() {
        return uniqueid;
    }

    public int getUserid() {
        return userid;
    }

    public void setCclass(int cclass) {
        this.cclass = cclass;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUniqueid(int uniqueid) {
        this.uniqueid = uniqueid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
    
    
    
}
