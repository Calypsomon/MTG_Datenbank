package service;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import model.Card;
import model.ScrId;
import repository.CardRepository;

public class Pipeline {

    public static void CardPipeline(String name){                       //Complete Pipeline
        ScrId ScrIDS = ManaboxImports.getScryfallID(name);              //ManaboxImport -> ScrIDS
        while(ScrIDS.getPosition()<ScrIDS.getIds().length){             //While not all cards saved
            ScrIDS = ScryfallClient.Scryfallpackager50(ScrIDS);         //ScrIDS -> 50er Json Request
            String response = ScryfallClient.ScrJasonToCard(ScrIDS);    //50erJson -> 50 Cards
            List<Card> Cards = ScryfallClient.respondToCard(response);  //50 Cards -> 50Cards in Databank
            CardRepository.addBatch(Cards);                             //Cards in Databank
            try {       //0.6 Sekunden warten da nur 2 Anfragen pro sekunde
                TimeUnit.MILLISECONDS.sleep(600);
            } catch (InterruptedException e) {
            e.printStackTrace();                
}
        }
        System.out.println("updating Images");
        CardRepository.downloadPng();                                   //Download Png for each card whit no png jet
        System.out.println("Finished");
    }


public static void CardPipeline2(InputStream name){                       //Complete Pipeline
        ScrId ScrIDS = ManaboxImports.getScryfallID2(name);              //ManaboxImport -> ScrIDS
        while(ScrIDS.getPosition()<ScrIDS.getIds().length){             //While not all cards saved
            ScrIDS = ScryfallClient.Scryfallpackager50(ScrIDS);         //ScrIDS -> 50er Json Request
            String response = ScryfallClient.ScrJasonToCard(ScrIDS);    //50erJson -> 50 Cards
            List<Card> Cards = ScryfallClient.respondToCard(response);  //50 Cards -> 50Cards in Databank
            CardRepository.addBatch(Cards);                             //Cards in Databank
            try {       //0.6 Sekunden warten da nur 2 Anfragen pro sekunde
                TimeUnit.MILLISECONDS.sleep(600);
            } catch (InterruptedException e) {
            e.printStackTrace();                
}
        }
        System.out.println("updating Images");
        CardRepository.downloadPng();                                   //Download Png for each card whit no png jet
        System.out.println("Finished");
    }
    public static void main(String[] Arges){
    //    CardPipeline("uncommon");
      //  System.out.println(CardRepository.getAllCards());
     CardRepository.deleteAllCards();
    }

    
}
