package model;
import java.util.List;

public class Card {
 // ===== BASIC =====
    public String id;                  // Scryfall ID (UUID)
    public Integer private_id;
    public String name;
    public String lang;
    public String oracle_id;
    public int    count =1;

    // ===== SET INFO =====
    public String set;
    public String set_name;
    public String set_id;
    public String collector_number;
    public String released_at;

    // ===== GAME DATA =====
    public String type_line;
    public String oracle_text;
    public String mana_cost;
    public Double cmc;

    public List<String> colors;
    public List<String> color_identity;
    public List<String> keywords;

    public String rarity;
    public String layout;

    // ===== FLAGS =====
    public Boolean reserved;
    public Boolean reprint;
    public Boolean full_art;
    public Boolean booster;

    // ===== META =====
    public String border_color;
    public Integer edhrec_rank;

    // ===== ART =====
    public String artist;
    public String card_back_id;

    // ===== OPTIONAL (vereinfacht, später evtl. als Objekte) =====
    public Euro price;
    public ImageUris image_uris;
    public PurchaseUris purchase_uris;

    public String getPng() {
        return image_uris != null ? image_uris.png : null;
    }
    public String getSmall() {
        return image_uris != null ? image_uris.small : null;
    } 
    public String getNormal() {
        return image_uris != null ? image_uris.normal : null;
    } 
    public String getCardmarket() {
        return purchase_uris != null ? purchase_uris.cardmarket : null;
    }
    @Override
    public String toString(){
        return (name + ": " +count+ " " + set_name);
    }
}
