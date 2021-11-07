package JavaBot.db;

import JavaBot.resources.Word;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MongoDBOperator implements Operator {
    private final MongoDatabase database;

    public MongoDBOperator(String uri, String dbName) {
        var connectionString = new ConnectionString(uri);
        var settings = MongoClientSettings
                .builder()
                .applyConnectionString(connectionString)
                .build();
        var mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(dbName);

        try {
            database.createCollection("user_list");
        } catch (Exception ignored) { }
    }

    public void add(String userId) {
        var userCollection = database.getCollection("user_list");
        var doc = new Document(Map.of("_id", userId, "words", List.of()));
        try {
            userCollection.insertOne(doc);
        } catch (Exception e) {
            System.out.println("Already has this user");
        }
    }

    public List<Word> getWords(String userId) {
        var userCollection = database.getCollection("user_list");
        var filterDoc = new Document("_id", new Document("$regex", userId));
        var neededDoc = userCollection.find(filterDoc).first();
        if (neededDoc == null)
            return null;
        var res = (List<String>) neededDoc.get("words");
        return res.stream().map(EnglishConverter::ToCouple).toList();
    }

    public void updateWords(String userId, List<Word> newWords) {
        var userCollection = database.getCollection("user_list");
        var filterDoc = new Document("_id", new Document("$regex", userId));
        var wordStream = Stream.concat(getWords(userId).stream(), newWords.stream());
        var newDoc = new Document(
                Map.of("$set", new Document("words", wordStream.map(EnglishConverter::ToString).toList()))
        );
        userCollection.updateOne(filterDoc, newDoc);
    }
}
