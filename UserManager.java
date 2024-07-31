import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users;

    public UserManager() {
        users = new HashMap<>();
    }

    public void registerUser(User user) {
        users.put(user.getEmail(), user);
    }

    public User loginUser(String email, String password) {
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void updateUser(User user) {
        users.put(user.getEmail(), user);
    }

    public User getUser(String email) {
        return users.get(email);
    }
}