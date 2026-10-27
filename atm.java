
import java.sql.*;
import java.util.Scanner;

public class atm {

    static final String URL = "jdbc:mysql://localhost:3306/atm_db";
    static final String USER = "root";
    static final String PASSWORD = "your_mysql_password";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                URL, USER, PASSWORD
            );

            System.out.println("===== WELCOME TO ATM =====");

            System.out.print("Enter Account Number: ");
            int account = sc.nextInt();

            System.out.print("Enter PIN: ");
            int pin = sc.nextInt();

            String login = "SELECT * FROM accounts WHERE account_number = ? AND pin = ?";

            PreparedStatement ps = con.prepareStatement(login);
            ps.setInt(1, account);
            ps.setInt(2, pin);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid Account Number or PIN.");
                con.close();
                sc.close();
                return;
            }

            System.out.println("\nLogin Successful!");
            System.out.println("Welcome, " + rs.getString("account_holder"));

            int choice;

            do {
                System.out.println("\n===== ATM MENU =====");
                System.out.println("1. Check Balance");
                System.out.println("2. Deposit Money");
                System.out.println("3. Withdraw Money");
                System.out.println("4. Account Details");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        String balanceQuery =
                            "SELECT balance FROM accounts WHERE account_number = ?";

                        PreparedStatement ps1 = con.prepareStatement(balanceQuery);
                        ps1.setInt(1, account);

                        ResultSet rs1 = ps1.executeQuery();

                        if (rs1.next()) {
                            System.out.println(
                                "Current Balance: Rs. " +
                                rs1.getDouble("balance")
                            );
                        }

                        rs1.close();
                        ps1.close();
                        break;

                    case 2:
                        System.out.print("Enter deposit amount: Rs. ");
                        double deposit = sc.nextDouble();

                        if (deposit <= 0) {
                            System.out.println("Enter a valid amount.");
                            break;
                        }

                        String depositQuery =
                            "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";

                        PreparedStatement ps2 = con.prepareStatement(depositQuery);
                        ps2.setDouble(1, deposit);
                        ps2.setInt(2, account);

                        ps2.executeUpdate();

                        System.out.println("Deposit Successful!");
                        ps2.close();
                        break;

                    case 3:
                        System.out.print("Enter withdrawal amount: Rs. ");
                        double withdraw = sc.nextDouble();

                        if (withdraw <= 0) {
                            System.out.println("Enter a valid amount.");
                            break;
                        }

                        String withdrawQuery =
                            "UPDATE accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?";

                        PreparedStatement ps3 = con.prepareStatement(withdrawQuery);
                        ps3.setDouble(1, withdraw);
                        ps3.setInt(2, account);
                        ps3.setDouble(3, withdraw);

                        int updated = ps3.executeUpdate();

                        if (updated > 0) {
                            System.out.println("Please collect your cash.");
                            System.out.println("Withdrawal Successful!");
                        } else {
                            System.out.println("Insufficient Balance.");
                        }

                        ps3.close();
                        break;

                    case 4:
                        String detailsQuery =
                            "SELECT account_number, account_holder, balance FROM accounts WHERE account_number = ?";

                        PreparedStatement ps4 = con.prepareStatement(detailsQuery);
                        ps4.setInt(1, account);

                        ResultSet rs4 = ps4.executeQuery();

                        if (rs4.next()) {
                            System.out.println("\n===== ACCOUNT DETAILS =====");
                            System.out.println("Account Number: " +
                                rs4.getInt("account_number"));
                            System.out.println("Account Holder: " +
                                rs4.getString("account_holder"));
                            System.out.println("Balance: Rs. " +
                                rs4.getDouble("balance"));
                        }

                        rs4.close();
                        ps4.close();
                        break;

                    case 5:
                        System.out.println("Thank you for using our ATM!");
                        break;

                    default:
                        System.out.println("Invalid Choice. Try Again.");
                }

            } while (choice != 5);

            rs.close();
            ps.close();
            con.close();
            sc.close();

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            System.out.println("Add MySQL Connector/J to your project.");

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}
