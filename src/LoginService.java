import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles member login for the membership store.
 *
 * Members normally authenticate against the users table in the database.
 * There is also a fallback admin account, used if the database is
 * unreachable, so store staff can still get in during an outage.
 *
 * NOTE FOR STUDENTS: this file is part of the application you are analysing.
 * You are not expected to have written it — your job is to find, verify, and
 * fix the vulnerability(ies) it contains.
 */
public class LoginService {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Sup3rSecret!";

    /**
     * Full login entry point: tries the admin fallback first, then falls
     * back to the real database-backed check.
     */
    public static boolean login(Connection conn, String username, String password) throws SQLException {
        if (isHardcodedAdmin(username, password)) {
            return true;
        }
        return authenticate(conn, username, password);
    }

    /**
     * Checks the submitted credentials against the hardcoded admin fallback
     * account. Returns true only for an exact username+password match.
     */
    public static boolean isHardcodedAdmin(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    /**
     * Hashes a plaintext password before storing/comparing it against the
     * value held in the database.
     */
    public static String hashPassword(String plainPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(plainPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds the SQL statement used to look up a member by username and
     * (hashed) password. Pulled out as its own method so it can be exercised
     * directly, without needing a live database connection, to see exactly
     * what SQL text would be sent.
     */
    public static String buildLoginQuery(String username, String password) {
        String hashed = hashPassword(password);
        return "SELECT * FROM users WHERE username = '" + username
                + "' AND password = '" + hashed + "'";
    }

    /**
     * Full login path against the real database. Requires a live Connection
     * (not needed for the static/Semgrep analysis, or for exercising
     * buildLoginQuery directly — only relevant if you wire up an actual DB).
     */
    public static boolean authenticate(Connection conn, String username, String password) throws SQLException {
        String hashed = hashPassword(password);
        String sql = "SELECT * FROM users WHERE username = '" + username
                + "' AND password = '" + hashed + "'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next();
        }
    }

    // Simple manual entry point so the query-building logic can be run/tested
    // standalone without needing a real database.
    public static void main(String[] args) {
        String username = args[0];
        String password = args[1];
        System.out.println("Constructed SQL: " + buildLoginQuery(username, password));
        System.out.println("Hardcoded admin match: " + isHardcodedAdmin(username, password));
    }
}
