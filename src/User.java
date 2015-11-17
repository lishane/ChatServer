
public class User {

    private String username;
    private String password;
    SessionCookie cookie;

    public User(String username, String password) {
        this(username, password, null);
    }

    public User(String username, String password, SessionCookie cookie) {
        this.username = username;
        this.password = password;
        this.cookie = cookie;
    }

    public String getName() {
        return username;
    }

    public SessionCookie getCookie() {
        return cookie;
    }

    public void setCookie(SessionCookie cookie) {
        this.cookie = cookie;
    }

    public boolean checkPassword(String password) {
        if (password.equals(this.password)) {
            return true;
        } else
            return false;
    }


}
