package model;
import java.util.ArrayList;
import java.util.List;

public class CardFilter {
String color;
String rarity;
Double cmc;
String cardtype;
String oracle_text;


    public CardFilter(String color, String rarity,Double cmc, String cardtype,String oracle_text){
        if(color == "0"){
            this.color = null;
        }   else {
            this.color = color;
        }
        if(rarity == "0"){
            this.rarity = null;
        }   else {
            this.rarity = rarity;
        }
        if(cmc == 0){
            this.cmc = 10.0;
        }   else {
            this.cmc = cmc;
        }
        if(cardtype == "0"){
            this.cardtype = null;
        }   else {
            this.cardtype = cardtype;
        }
        if(oracle_text == "0"){
            this.oracle_text = null;
        }   else {
            this.oracle_text = oracle_text;
        }
    }
    public List<String> Color(){
        List<String> ret = new ArrayList<>();
        ret.add(color);
        return ret;
    }
    public String Rarity(){
        return this.rarity;
    }
    public Double Cmc(){
        return this.cmc;
    }
    public String Cardtype(){
        return this.cardtype;
    }
    public String Oracle_Text(){
        return this.oracle_text;
    }
}
