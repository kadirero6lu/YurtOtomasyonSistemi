import paket.arayuz.GirisEkrani;

public class AnaGiris {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            GirisEkrani g = new GirisEkrani();
            g.setVisible(true);
        });
    }
}