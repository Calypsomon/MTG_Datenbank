package model;

import java.util.List;

public class CardData {
    public String name;
    public String lang;
    public String oracle_id;
    public int    count;

    // ===== SET INFO =====
    public String set;
    public String set_name;
    public String collector_number;
    public String released_at;

    // ===== GAME DATA =====
    public String type_line;
    public String oracle_text;
    public String mana_cost;
    public Double cmc;

    public List<String> color_identity;
    public List<String> keywords;

    public String rarity;
    // ===== FLAGS =====
    public Boolean reserved;
    public Boolean reprint;
    public Boolean booster;

    // ===== META =====
    public Integer edhrec_rank;

    // ===== ART =====
    public String artist;
    public String card_back_id;

    public CardData(String name, String lang, String oracle_id, int count, String set, String set_name, 
        String collector_number, String released_at, String type_line,String oracle_text,String mana_cost,
        Double cmc,List<String> color_identity,List<String> keywords,String rarity,Boolean reserved,Boolean reprint,
        Boolean booster,Integer edhrec_rank,String artist,String card_back_id){

        this.name = name;
        this.lang = lang;
        this.oracle_id = oracle_id;
        this.count = count;
        this.set = set;
        this.set_name = set_name;
        this.collector_number = collector_number;
        this.released_at = released_at;
        this.type_line = type_line;
        this.oracle_text = oracle_text;
        this.mana_cost = mana_cost;
        this.cmc = cmc;
        this.color_identity = color_identity;
        this.keywords = keywords;
        this.rarity = rarity;
        this.reserved = reserved;
        this.reprint = reprint;
        this.booster = booster;
        this.edhrec_rank = edhrec_rank;
        this.artist = artist;
        this.card_back_id = card_back_id;
    }
    public String getName(){
        return name;
    }
    public String getLang(){
        return lang;
    }
    public String oracle_id(){
        return oracle_id;
    }
    public int getCount(){
        return count;
    }
    public String getSet(){
        return set;
    } 
    public String getSet_name(){
        return set_name;
    }
    public String getCollector_number(){
        return collector_number;
    }
    public String getReleased_at(){
        return released_at;
    }
    public String getType_line(){
        return type_line;
    }
    public String getOracle_text(){
        return oracle_text;
    }
    public String getMana_cost(){
        return mana_cost;
    } 
    public Double getCmc(){
        return cmc;
    }
    public List<String> getColor_identity(){
        return color_identity;
    }
    public List<String> getKeywords(){
        return keywords;
    }
    public String getRarity(){
        return rarity;
    }
    public Boolean getReserved(){
        return reserved;
    }
    public Boolean getReprint(){
        return reprint;
    }
    public Boolean getBooster(){
        return booster;
    }
    public Integer getEdhrec_rank(){
        return edhrec_rank;
    }
    public String getArtist(){
        return artist;
    }
    public String getCard_back_id(){
        return card_back_id;
    }
}
