public interface User {
    String getFirstName();
    String getLastName();
    String getEmail();
    String getPassword();
    UserRoles getUserRole();
    boolean login(String email, String password);
    void logout();
    User viewProfile();
}
