import java.sql.*;

public class ResultService {

    private static final String DB_NAME = getEnvOrDefault("DB_NAME", "calc_data");
    private static final String DB_USER = getEnvOrDefault("DB_USER", "root");
    private static final String DB_PASSWORD = getEnvOrDefault("DB_PASSWORD", "Test12");

    // Load MariaDB driver
    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return value;
    }

    private static String getDatabaseHost() {
        return getEnvOrDefault("DB_HOST", "db");
    }

    private static String getDatabasePort() {
        return getEnvOrDefault("DB_PORT", "3306");
    }

    private static String getDatabaseUrl() {
        return "jdbc:mariadb://" + getDatabaseHost() + ":" + getDatabasePort() + "/" + DB_NAME
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    }

    public static void saveResult(double n1, double n2, double sum, double difference, double product, Double division) {
        String dbUrl = getDatabaseUrl();

        try (Connection conn = DriverManager.getConnection(dbUrl, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Create table if it doesn't exist
            String createTable = """
                CREATE TABLE IF NOT EXISTS calc_results (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    number1 DOUBLE NOT NULL,
                    number2 DOUBLE NOT NULL,
                    sum_result DOUBLE NOT NULL,
                    subtract_result DOUBLE NOT NULL,
                    product_result DOUBLE NOT NULL,
                    division_result DOUBLE NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            stmt.executeUpdate(createTable);

            // Insert the result
            String insert = "INSERT INTO calc_results (number1, number2, sum_result, subtract_result, product_result, division_result) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setDouble(1, n1);
                ps.setDouble(2, n2);
                ps.setDouble(3, sum);
                ps.setDouble(4, difference);
                ps.setDouble(5, product);
                if (division == null) {
                    ps.setNull(6, Types.DOUBLE);
                } else {
                    ps.setDouble(6, division);
                }
                ps.executeUpdate();
            }

            System.out.println("Result saved: " + n1 + ", " + n2
                    + " -> Sum=" + sum
                    + ", Subtract=" + difference
                    + ", Product=" + product
                    + ", Division=" + (division == null ? "NULL" : division));

        } catch (SQLException e) {
            System.err.println("Failed to save result to DB: " + dbUrl);
            e.printStackTrace();
        }
    }
}