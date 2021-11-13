package JavaBot.db;

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
            database.createCollection("user_list");
        } catch (Exception e) {
            System.out.println("There is a user_list collection");
        }
        return database;
    }
}
