package repository;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import config.Config;
import model.Card;
import model.CardData;
import model.CardFilter;
public class CardRepository {
    


    public static void addBatch(List<Card> Cards){
        try{
        Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD
        );
        String sql = """
INSERT INTO card (
    id, name, lang, oracle_id, count, set, set_name, set_id, collector_number, released_at, type_line, oracle_text, mana_cost, cmc, colors, color_identity, keywords, rarity, layout, reserved, reprint, full_art, booster, border_color, edhrec_rank, artist, card_back_id, image_png, image_small, image_normal, purchase_cardmarket
) VALUES (?, ?, ?, ?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
ON CONFLICT (id)
DO UPDATE SET count = card.count + 1;
""";

PreparedStatement stmt = connection.prepareStatement(sql);

for (Card card : Cards) {
    String image_png = card.getPng();
    String image_small = card.getSmall();
    String image_normal = card.getNormal();
    String purchase_cardmarket = card.getCardmarket();
    if (card.id == null) {
        throw new IllegalArgumentException("Card ID darf nicht null sein");
    }
    stmt.setString(1, card.id);
    stmt.setObject(2, card.name);
    stmt.setObject(3, card.lang);
    stmt.setObject(4, card.oracle_id);
    stmt.setObject(5, card.set);
    stmt.setObject(6, card.set_name);
    stmt.setObject(7, card.set_id);
    stmt.setObject(8, card.collector_number);
    if (card.released_at != null) {
    stmt.setDate(9, java.sql.Date.valueOf(card.released_at));
    } else {
        stmt.setNull(9,java.sql.Types.DATE);
    }
    stmt.setObject(10, card.type_line);
    stmt.setObject(11, card.oracle_text);
    stmt.setObject(12, card.mana_cost);
    if (card.cmc != null) {
    stmt.setDouble(13, card.cmc);
    } else {
        stmt.setNull(13, java.sql.Types.DOUBLE);
    }
    if (card.colors != null) {
    stmt.setArray(14,connection.createArrayOf("text", card.colors.toArray()));
    } else {
        stmt.setArray(14, connection.createArrayOf("text", new String[0]));
    }
    stmt.setArray(15,connection.createArrayOf("text", card.color_identity.toArray()));
    stmt.setArray(16,connection.createArrayOf("text", card.keywords.toArray()));
    stmt.setString(17, card.rarity);
    stmt.setString(18, card.layout);
    stmt.setBoolean(19, card.reserved);
    stmt.setBoolean(20, card.reprint);
    stmt.setBoolean(21, card.full_art);
    stmt.setBoolean(22, card.booster);
    stmt.setString(23, card.border_color);
    if (card.edhrec_rank != null) {
    stmt.setInt(24, card.edhrec_rank);
    } else {
        stmt.setNull(24,java.sql.Types.INTEGER);
    }
    stmt.setString(25, card.artist);
    stmt.setString(26, card.card_back_id);
    stmt.setString(27, image_png);
    stmt.setString(28, image_small);
    stmt.setString(29, image_normal);
    stmt.setString(30, purchase_cardmarket);

    stmt.addBatch(); // 👈 wichtig!
    }
    stmt.executeBatch(); // 👈 alles auf einmal
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}

    public static void deleteCard(String id){
        try{
        Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD
            );
            String sql = """
            DELETE FROM card
            WHERE id = ?;
            """;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            stmt.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    
    }

    public static void deleteAllCards(){
        try{
        Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD
            );
            String sql = """
            DELETE FROM card
            """;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    
    }

    public static CardData getCardData(String id){
        try{
        Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD
            );
            String sql = """
            SELECT name,lang,oracle_id,`count`,`set`,set_name,collector_number,released_at,type_line,oracle_text,mana_cost,cmc,color_identity,keywords,rarity,reserved,reprint,booster,edhrec_rank,artist,card_back_id From card
            WHERE id = ?
            """;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
            List<String> color_identity = List.of(rs.getString("color_identity").split(","));
            List<String> keywords = List.of(rs.getString("keywords").split(","));
            return new CardData(
                rs.getString ("name"),
                rs.getString ("lang"),
                rs.getString("oracle_id"),
                rs.getInt("count"),
                rs.getString ("set"), 
                rs.getString ("set_name"),
                rs.getString ("collector_number"),
                rs.getString ("released_at"), 
                rs.getString ("type_line"),
                rs.getString ("oracle_text"),
                rs.getString ("mana_cost"),
                rs.getDouble ("cmc"),
                color_identity,keywords,
                rs.getString ("rarity"),
                rs.getBoolean ("reserved"),
                rs.getBoolean ("reprint"),
                rs.getBoolean ("booster"),
                rs.getInt ("edhrec_rank"),
                rs.getString ("artist"),
                rs.getString ("card_back_id"));
        }
        return null;
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }    
    }

    public static String getAllCardsAndID(){
        try{
        Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD
            );
            String sql = """
            SELECT name,id FROM card
            """;
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            String ret = null;
            while(rs.next()){
            ret += rs.getString(1);
            ret += " / " + (rs.getString(2));
            ret += "\n";
            }
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    
    }

    public static List<String> getAllCards(CardFilter f){
        System.out.println("ALLCARDS AUSGEFÜHRT");
            StringBuilder sql = new StringBuilder("SELECT id FROM card WHERE 1=1");
        List<Object> params = new ArrayList<>();

      if (f.Color().getFirst() != null && !f.Color().isEmpty()) {
    sql.append(" AND color_identity && ARRAY[?]::text[]");
    params.add(f.Color().get(0));
    }

        if (f.Cardtype() != null) {
            sql.append(" AND type_line LIKE ?");
            params.add(f.Cardtype());
        }

        if (f.Cmc() != 10) {
            sql.append(" AND cmc = ?");
            params.add(f.Cmc());
        }

        if (f.Rarity() != null) {
            sql.append(" AND rarity = ?");
            params.add(f.Rarity());
        }

        if (f.Oracle_Text() != null) {
            sql.append(" AND oracle_text LIKE ?");
            params.add(f.Oracle_Text());
        }

        try{ Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD
            );
            List<String> ret = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
            ret.add(rs.getString(1));
            }
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    
    }

    public static byte[] getPng(String id){
    try {
        Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD
        );

        String sql = """
        SELECT image_png_data FROM card
        WHERE id = ?
        """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, id);

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return null;
        }

       byte[] data = rs.getBytes("image_png_data");

        if (data == null || data.length == 0) {
            System.out.println("EMPTY IMAGE FOR ID: " + id);
            return null;
        }

        return data;

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

public record Pair<F, S>(F first, S second) {}
    public static void downloadPng(){
        try{
        Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD
            );
            String sql = """
            SELECT id, image_png 
            FROM card
            WHERE image_png_data IS NULL;

            """;
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<Pair<String,String>> tasks = new ArrayList<>();
            while (rs.next()) {
            tasks.add(new Pair<>(rs.getString("id"), rs.getString("image_png")));
            }
            AtomicInteger counter = new AtomicInteger();
            int total = tasks.size();
            try (ExecutorService executor = Executors.newFixedThreadPool(10)) {

                List<CompletableFuture<Void>> futures = tasks.stream()
                    .map(pair ->
                        CompletableFuture.runAsync(() -> {
                            try { byte[] image = download(pair.second());
                        saveImage(pair.first(), image);
                        int done = counter.incrementAndGet();
                        System.out.println(done + "/" + total);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, executor)
            )
        .toList();

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
}} catch (SQLException e) {
            throw new RuntimeException(e);
        }    
    }

    public static void saveImage(String id,byte[] image){
        try {
            Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD   
            );
            String sql ="""
            UPDATE card
            SET image_png_data = ?
            WHERE id = ?
            """;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBytes(1,image);
            stmt.setString(2,id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] download(String url) throws Exception {
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

    HttpResponse<byte[]> response =
            client.send(request, HttpResponse.BodyHandlers.ofByteArray());

    return response.body();
}

    public static List<String> DeckCardToId(String[] oracle_ids){
        try{
        Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD
            );
            String questionmarks = "";
            for(int i = 0; i<oracle_ids.length;i++){
                questionmarks += "? ";
            }
            List<String> ret = new ArrayList<>();
            String sql = """
            SELECT id
            FROM cards
            WHERE oracle_id IN (?)
            """;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,questionmarks);
            for(int i = 0; i<oracle_ids.length;i++){
               stmt.setString(1,oracle_ids[i]); 
                ResultSet rs = stmt.executeQuery();
                ret.add(rs.getString(1));
            }
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    
    }

    public static int getCardCount(){
        try{
        Connection connection = DriverManager.getConnection(
            Config.URL,
            Config.USERNAME,
            Config.PASSWORD
            );
        String sql = 
        """
        SELECT COUNT(*) FROM card;    
        """;
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
           if (rs.next()) {
        int Areturn = rs.getInt(1);
        return Areturn;
    }
    return 0;
    }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}