package JavaBot.db;

public interface UserRepository {
    User get(String userId);
    void save(User user, Boolean update);
}
