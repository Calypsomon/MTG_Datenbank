package services;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.Card;
import model.ScrId;
import service.ManaboxImports;
import service.ScryfallClient;

public class Test_service {

    @Test
    void Test_ManaboxImports() {
        String[] ids = {"6559c423-449c-4e8e-8384-3ce78183e317", "1c244fc2-70f0-4149-b0d2-d49fc6bac2b0", "df2cdaa5-9ea0-4aa5-89d3-9edf40fa2a39", "1f9bd4da-4626-40ba-95f4-14e3de36f989", "2e914c3d-2eed-48bf-af9a-a8998fd5111d", "6c517308-831b-4e41-a82c-02ddf6383a0c", "fa684608-ecd3-43e2-99a1-71318f133e29", "9fbaabb5-e981-4cbf-888c-46449412711f", "243e392b-48b0-4e90-ae22-9a696f45e878", "ebbb5756-80fa-406f-8daa-be1f5a6c3c80", "bc0f5ca8-47bd-4451-8fd1-a312ff7d31ec", "e65ff1f4-a061-4c31-a78e-92af6e7bc56f", "ab47a37b-b66d-4f70-9bf0-4d5ed6b518f3", "9528cc07-df4b-417f-ad65-c2fae6fc2d49", "faab52c0-ce79-40af-a156-b193a62d439e"};
        ScrId ScrIds = new ScrId(ids);
        String expected = ScrIds.toString();
        assertEquals(expected,ManaboxImports.getScryfallID("test").toString());
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
    }

    @Test
    void Test_Scryfallpackager50(){
        String[] test = {"2e914c3d-2eed-48bf-af9a-a8998fd5111d", "6c517308-831b-4e41-a82c-02ddf6383a0c", "fa684608-ecd3-43e2-99a1-71318f133e29", "9fbaabb5-e981-4cbf-888c-46449412711f", "243e392b-48b0-4e90-ae22-9a696f45e878", "ebbb5756-80fa-406f-8daa-be1f5a6c3c80", "bc0f5ca8-47bd-4451-8fd1-a312ff7d31ec","e65ff1f4-a061-4c31-a78e-92af6e7bc56f", "ab47a37b-b66d-4f70-9bf0-4d5ed6b518f3","9528cc07-df4b-417f-ad65-c2fae6fc2d49","faab52c0-ce79-40af-a156-b193a62d439e"};
        ScrId ScrIds = new ScrId(test);
        String expected = "{\"identifiers\":[{\"id\": \"2e914c3d-2eed-48bf-af9a-a8998fd5111d\" },{\"id\": \"6c517308-831b-4e41-a82c-02ddf6383a0c\" },{\"id\": \"fa684608-ecd3-43e2-99a1-71318f133e29\" },{\"id\": \"9fbaabb5-e981-4cbf-888c-46449412711f\" },{\"id\": \"243e392b-48b0-4e90-ae22-9a696f45e878\" },{\"id\": \"ebbb5756-80fa-406f-8daa-be1f5a6c3c80\" },{\"id\": \"bc0f5ca8-47bd-4451-8fd1-a312ff7d31ec\" },{\"id\": \"e65ff1f4-a061-4c31-a78e-92af6e7bc56f\" },{\"id\": \"ab47a37b-b66d-4f70-9bf0-4d5ed6b518f3\" },{\"id\": \"9528cc07-df4b-417f-ad65-c2fae6fc2d49\" },{\"id\": \"faab52c0-ce79-40af-a156-b193a62d439e\" }]}";
        assertEquals(expected,ScryfallClient.Scryfallpackager50(ScrIds).getJson(), "ScryfallPackager test " );
    }

    @Test
    void Test_ScrJasonToCard(){         // die Funktion ScrJasonToCard gibt leider nur den Response Boddy zurück weshalb nicht mit dem header verglichen werdne kann. Der inhalt wird daher überprüft. 
        String[] test = {"2e914c3d-2eed-48bf-af9a-a8998fd5111d"};
        ScrId ScrIds = new ScrId(test);
        String actual = ScryfallClient.ScrJasonToCard(ScryfallClient.Scryfallpackager50(ScrIds));

        assertTrue(actual.contains("\"object\":\"list\""));
        assertTrue(actual.contains("Michelangelo"));
    }
    @Test
    void Test_respondToCard(){          //Vergleich über die ToString methode der zurückgegebenen Card.
    String[] test = {"2e914c3d-2eed-48bf-af9a-a8998fd5111d"};
    ScrId ScrIds = new ScrId(test);
    List<Card> cardlist = ScryfallClient.respondToCard(ScryfallClient.ScrJasonToCard(ScryfallClient.Scryfallpackager50(ScrIds))); 
    String response = cardlist.getFirst().toString();
    String result ="Michelangelo, Game Master: 1 Teenage Mutant Ninja Turtles";
    assertEquals(response, result,"Response to Card Test");
    }
}