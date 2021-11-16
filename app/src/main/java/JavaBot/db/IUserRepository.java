package JavaBot.db;

public interface IUserRepository {
    User get(String userId);
    void save(User user, Boolean update);
}
