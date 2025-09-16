import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Same style that worked in your test (URL carries user+password)
        String url =
                "jdbc:postgresql://aws-1-eu-north-1.pooler.supabase.com:6543/postgres" +
                        "?sslmode=require" +
                        "&user=postgres.fkrfbpiiettzyotnssff" +
                        "&password=kosomCar123!";

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected to database!");
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1) List customers");
                System.out.println("2) Add customer");
                System.out.println("0) Exit");
                System.out.print("> ");
                String choice = in.nextLine().trim();

                switch (choice) {
                    case "1" -> listCustomers(conn);
                    case "2" -> addCustomer(conn, in);
                    case "0" -> { System.out.println("Bye!"); return; }
                    default -> System.out.println("Invalid choice");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listCustomers(Connection conn) throws SQLException {
        String sql = "SELECT renter_id, name, city FROM renter ORDER BY renter_id";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("%d - - %s (%s)%n",
                        rs.getInt("renter_id"),
                        rs.getString("name : "),
                        rs.getString("city"));
            }
        }
    }

    private static void addCustomer(Connection conn, Scanner in) throws SQLException {
        System.out.print("Name: "); String name = in.nextLine();
        System.out.print("City: "); String city = in.nextLine();

        String sql = """
            INSERT INTO renter(name, address, zip, city, driver_license_number, driver_since_date)
            VALUES (?, '', '', ?, ?, '2010-01-01')
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, city);
            ps.setString(3, "DL-" + System.currentTimeMillis()); // simple dummy DL
            ps.executeUpdate();
            System.out.println("Customer added!");
        }
    }
}
