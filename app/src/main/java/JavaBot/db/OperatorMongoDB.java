package JavaBot.db;

import JavaBot.resources.Config;
import JavaBot.resources.WordAndTranslate;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class OperatorMongoDB implements Operator {
    private final MongoDatabase database;

    public OperatorMongoDB(Config config) {
        var connectionString = new ConnectionString(config.uriMongoDB);
        var settings = MongoClientSettings
                .builder()
                .applyConnectionString(connectionString)
                .build();
        var mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(config.dbName);

        try {
            database.createCollection("user_list");
        } catch (Exception ignored) { }
    }

    public void add(String user_id) {
        var userCollection = database.getCollection("user_list");
        var doc = new Document(Map.of("_id", user_id, "words", List.of()));
        try {
            userCollection.insertOne(doc);
        } catch (Exception e) {
            System.out.println("Already has this user");
        }
    }

    public List<WordAndTranslate> getWords(String user_id) {
        var userCollection = database.getCollection("user_list");
        var filterDoc = new Document("_id", new Document("$regex", user_id));
        var neededDoc = userCollection.find(filterDoc).first();
        if (neededDoc == null)
            return null;
        var res = (List<String>) neededDoc.get("words");
        return res.stream().map(EnglishConverter::ToCouple).toList();
    }

    public void updateWords(String user_id, List<WordAndTranslate> newWords) {
        var userCollection = database.getCollection("user_list");
        var filterDoc = new Document("_id", new Document("$regex", user_id));
        var wordStream = Stream.concat(getWords(user_id).stream(), newWords.stream());
        var newDoc = new Document(
                Map.of("$set", new Document("words", wordStream.map(EnglishConverter::ToString).toList()))
        );
        userCollection.updateOne(filterDoc, newDoc);
    }
}
