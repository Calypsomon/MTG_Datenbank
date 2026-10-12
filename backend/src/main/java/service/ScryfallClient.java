package service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Card;
import model.ScrId;
import model.ScryfallResponse;

public class ScryfallClient{

    public static ScrId Scryfallpackager50(ScrId ScrIDS){       //takes Scryfall Ids and the current position of the array and gives pack a json for api requests 50 if possible
        StringBuilder sb = new StringBuilder();
        sb.append("{\"identifiers\":[");
        int currentposition = ScrIDS.getPosition();
        for(int i =currentposition; i<50+currentposition;i++){
            if(0>=(ScrIDS.getIds().length-ScrIDS.getPosition())){
                break;
            }
            sb.append("{\"id\": \"");
            sb.append((ScrIDS.getIds())[i]);
            sb.append("\" },");
            ScrIDS.addPosition(1);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        ScrIDS.setJson(sb.toString());
        System.out.println("Position " + ScrIDS.getPosition() + " von " + ScrIDS.getIds().length);
        return ScrIDS;
    }

    public static String ScrJasonToCard(ScrId ScrIDS){    //makes a api request whit the json, sorts them into list of cards ScrId is a important typ
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.scryfall.com/cards/collection"))
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .header("User-Agent", "PackTheRipper/1.0 (contact: Calypsomon@gmail.com)")
        .POST(HttpRequest.BodyPublishers.ofString(ScrIDS.getJson()))
        .build();

    try {
        HttpResponse<String> response =client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println("Response Acceptet");
        return response.body();
    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
        return null;
    }
}

public static List<Card> respondToCard(String respons) {        //takes the Scryfall API result and convert it into a List of cards 
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
        ScryfallResponse response = mapper.readValue(respons, ScryfallResponse.class);
    //    System.out.println(response.data.size() + " Cards Created");
        return response.data;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
/* 

    public static void main(String[] args){     //only test
    String[] test = {"2e914c3d-2eed-48bf-af9a-a8998fd5111d", "6c517308-831b-4e41-a82c-02ddf6383a0c", "fa684608-ecd3-43e2-99a1-71318f133e29", "9fbaabb5-e981-4cbf-888c-46449412711f", "243e392b-48b0-4e90-ae22-9a696f45e878", "ebbb5756-80fa-406f-8daa-be1f5a6c3c80", "bc0f5ca8-47bd-4451-8fd1-a312ff7d31ec","e65ff1f4-a061-4c31-a78e-92af6e7bc56f", "ab47a37b-b66d-4f70-9bf0-4d5ed6b518f3","9528cc07-df4b-417f-ad65-c2fae6fc2d49","faab52c0-ce79-40af-a156-b193a62d439e"};
    ScrId testids = new ScrId(test);
    ScrId json = Scryfallpackager50(testids);
    String x = ScrJasonToCard(json);
    //System.out.println(x.data.get(0).name);
    List<Card> cards = respondToCard(x);
   for (Card card : cards) {
    System.out.println(card);
    card.getPng();
   }
   CardRepository.downloadPng();
   System.out.println(CardRepository.getAllCards());

}
   */}