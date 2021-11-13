package JavaBot.db;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public class MongoDBRepository implements UserRepository {
    private final MongoDatabase database;
    
    public MongoDBRepository(MongoDatabase database) {
        this.database = database;
    }
    
    @Override
    public User get(String userId) {
        var userCollection = database.getCollection("user_list");
        var filterDoc = new Document("_id", new Document("$regex", userId));
        var neededDoc = userCollection.find(filterDoc).first();
        if (neededDoc == null)
            return null;
        var words = ((List<String>) neededDoc.get("words")).stream().map(EnglishConverter::ToCouple).toList();
        return new User(userId, words);
    }

    @Override
    public void save(User user, Boolean update) {
        var userCollection = database.getCollection("user_list");
        var doc = new Document(Map.of("_id", user.id, "words", List.of()));
        try {
            userCollection.insertOne(doc);
        } catch (Exception e) {
            if (!update)
                return;
            var filterDoc = new Document("_id", new Document("$regex", user.id));
            var wordsToDB = user.learnedWords.stream().map(EnglishConverter::ToString).toList();
            var updatedDoc = new Document(Map.of("$set", new Document("words", wordsToDB)));

            userCollection.updateOne(filterDoc, updatedDoc);
        }
    }
}
