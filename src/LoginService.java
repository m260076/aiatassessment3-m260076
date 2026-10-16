import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * Handles member login for the membership store.
 *
 * Members normally authenticate against the users table in the database.
 * An optional fallback admin account can be configured using environment
 * variables rather than storing credentials directly in source code.
 */
public class LoginService {

    private static final String ADMIN_USERNAME =
            System.getenv("ADMIN_USERNAME");

    private static final String ADMIN_PASSWORD =
            System.getenv("ADMIN_PASSWORD");

    /**
     * Full login entry point.
     */
    public static boolean login(
            Connection conn,
            String username,
            String password) throws SQLException {

        if (isHardcodedAdmin(username, password)) {
            return true;
        }

        return authenticate(conn, username, password);
    }

    /**
     * Checks submitted credentials against the optional administrator
     * credentials supplied through environment variables.
     */
    public static boolean isHardcodedAdmin(
            String username,
            String password) {

        return ADMIN_USERNAME != null
                && ADMIN_PASSWORD != null
                && ADMIN_USERNAME.equals(username)
                && ADMIN_PASSWORD.equals(password);
    }

    /**
     * Hashes a plaintext password before comparing it with the value
     * stored in the database.
     *
     * MD5 is intentionally unchanged at this stage so that the separate
     * hashing weakness can still be analysed and verified.
     */
    public static String hashPassword(String plainPassword) {

        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");

            byte[] digest =
                    md.digest(plainPassword.getBytes());

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
     * Returns the parameterised SQL statement used for authentication.
     *
     * User supplied values are not concatenated into the SQL command.
     */
    public static String buildLoginQuery(
            String username,
            String password) {

        return "SELECT * FROM users "
                + "WHERE username = ? AND password = ?";
    }

    /**
     * Authenticates a member using a parameterised database query.
     */
    public static boolean authenticate(
            Connection conn,
            String username,
            String password) throws SQLException {

        String hashed = hashPassword(password);

        String sql =
                "SELECT * FROM users "
                + "WHERE username = ? AND password = ?";

        try (PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashed);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Manual entry point for testing the security behaviour without
     * requiring a live database.
     */
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println(
                    "Usage: java LoginService <username> <password>");
            return;
        }

        String username = args[0];
        String password = args[1];

        System.out.println(
                "Constructed SQL: "
                        + buildLoginQuery(username, password));

        System.out.println(
                "Hardcoded admin match: "
                        + isHardcodedAdmin(username, password));
    }
}