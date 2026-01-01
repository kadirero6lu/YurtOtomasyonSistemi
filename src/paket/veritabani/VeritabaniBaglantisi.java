package paket.veritabani;
import java.sql.Connection;
import java.sql.DriverManager;

public class VeritabaniBaglantisi {
    public static Connection baglan() {
        try {
            String url = "jdbc:mysql://localhost:3306/yurt_otomasyon";
            String user = "root"; 
            String pass = "admin"; 
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            return null;
        }
    }
}