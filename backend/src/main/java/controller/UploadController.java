package controller;
import java.io.InputStream;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import model.CardData;
import model.CardFilter;
import repository.CardRepository;
import service.Pipeline;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api")
public class UploadController {

/*    @RestController
public class CardController {

    private final CardRepository repo;

    public CardController(CardRepository repo) {
        this.repo = repo;
    }
}*/

    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile file) throws Exception {

        InputStream input = file.getInputStream();

        // Beispiel: direkt weitergeben
        Pipeline.CardPipeline2(input);
    }

@GetMapping("/card/image/{id}")
public ResponseEntity<byte[]> getImage(@PathVariable String id) {

    try {
        byte[] image = CardRepository.getPng(id);

        if (image == null || image.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);

    } catch (Exception e) {
        e.printStackTrace();  // muss im Terminal sichtbar sein
        return ResponseEntity.status(500)
        .contentType(MediaType.TEXT_PLAIN)
        .body(e.getMessage().getBytes());
    }
}
@CrossOrigin(origins = "*")
@RestController
public class CardController{}

@GetMapping("/cards")
public List<String> getCards(
        @RequestParam(required = false) String color,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) double cmc,
        @RequestParam(required = false) String rarity,
        @RequestParam(required = false) String oracle_text
    ) {
       System.out.println("cmc");
        CardFilter filter = new CardFilter(color,rarity,cmc,type,oracle_text);
        
    return CardRepository.getAllCards(filter);
}



@GetMapping("/card/data/{id}")
public CardData getData(@PathVariable String id) {
    return CardRepository.getCardData(id);
    }

@GetMapping("/cards/count")
public int getCardCount(){
    return CardRepository.getCardCount();
}

}