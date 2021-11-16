package JavaBot.db;

import JavaBot.Strings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBOperator {
    public static MongoDatabase InitializeDatabase(String uri, String name) {
        var connectionString = new ConnectionString(uri);
        var settings = MongoClientSettings
                .builder()
                .applyConnectionString(connectionString)
                .build();
        var mongoClient = MongoClients.create(settings);
        var database = mongoClient.getDatabase(name);
        try {
            database.createCollection(Strings.collectionName.string);
        } catch (Exception e) {
            System.out.printf("There is a user_list collection%n");
        }
        return database;
    }
}
