package JavaBot.deserialization;

public class Config {
    public static String botName = System.getenv("botName");
    public static String token = System.getenv("token");
    public static String uriMongoDB = System.getenv("uriMongoDB");
    public static String dbName = System.getenv("dbName");
    public static String extKey = System.getenv("extKey");
}
