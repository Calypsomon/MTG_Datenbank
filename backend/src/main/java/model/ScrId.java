package model;
import java.util.Arrays;

public class ScrId{
    String[] ids;
    int position;   
    String json;
    public ScrId(String[] ids){
        this.ids = ids;
        this.position = 0; 
        this.json = null;
    }
    public void addPosition(int position){
        this.position += position;
    }
    public void setPosition(int position){
        this.position = position;
    }
    public void setIds(String[] ids){
        this.ids = ids;
    }
    public void setJson(String json){
        this.json = json;
    }
    public String getJson(){
        return json;
    }
    public String[] getIds(){
        return ids;
    }
    public int getPosition(){
        return position;
    }
    @Override
    public String toString(){
        return Arrays.toString(ids);
    }
}
