package paket.arayuz;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import paket.veritabani.VeritabaniBaglantisi;

public class GirisEkrani extends JFrame {
    private JPanel pnlKartlar;
    private CardLayout cl;
    private JTextField txtTC = new JTextField(15);
    private JPasswordField txtSifre = new JPasswordField(15);
    private String secilenTip = "";
    private Color sarıTema = new Color(255, 241, 184);

    public GirisEkrani() {
        setTitle("Yurt Otomasyonu - Giriş");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cl = new CardLayout();
        pnlKartlar = new JPanel(cl);
        
        pnlKartlar.add(ekranSecim(), "SECIM");
        pnlKartlar.add(ekranLogin(), "LOGIN");
        
        add(pnlKartlar);
        cl.show(pnlKartlar, "SECIM");
    }

    private JPanel ekranSecim() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(sarıTema);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        RoundedButton btnIdare = new RoundedButton("İDARE GİRİŞİ", "");
        RoundedButton btnOgrenci = new RoundedButton("ÖĞRENCİ GİRİŞİ", "");
        
        btnIdare.addActionListener(e -> { secilenTip = "IDARE"; cl.show(pnlKartlar, "LOGIN"); });
        btnOgrenci.addActionListener(e -> { secilenTip = "OGRENCI"; cl.show(pnlKartlar, "LOGIN"); });
        
        gbc.gridy = 0; pnl.add(btnIdare, gbc);
        gbc.gridy = 1; pnl.add(btnOgrenci, gbc);
        return pnl;
    }

    private JPanel ekranLogin() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(sarıTema);
        
        JPanel pnlForm = new JPanel(new GridLayout(5, 1, 10, 10));
        pnlForm.setOpaque(false);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        
        JButton btnGeri = new JButton("⬅ Geri Dön");
        btnGeri.addActionListener(e -> cl.show(pnlKartlar, "SECIM"));
        
        RoundedButton btnGiris = new RoundedButton("Giriş Yap", "");
        btnGiris.setPreferredSize(new Dimension(200, 60));
        btnGiris.addActionListener(e -> girisYap());

        pnlForm.add(new JLabel("TC Kimlik Numarası:"));
        pnlForm.add(txtTC);
        pnlForm.add(new JLabel("Şifre:"));
        pnlForm.add(txtSifre);
        pnlForm.add(btnGiris);
        
        pnl.add(btnGeri, BorderLayout.NORTH);
        pnl.add(pnlForm, BorderLayout.CENTER);
        return pnl;
    }

    private void girisYap() {
        try (Connection conn = VeritabaniBaglantisi.baglan()) {
            String sql = "SELECT * FROM kullanicilar WHERE tc=? AND sifre=? AND tip=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTC.getText());
            ps.setString(2, new String(txtSifre.getPassword()));
            ps.setString(3, secilenTip);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (secilenTip.equals("IDARE")) new IdareAnaEkrani().setVisible(true);
                else new OgrenciAnaEkrani(rs.getInt("id"), rs.getString("tc")).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Bilgiler Hatalı!");
            }
        } catch (Exception ex) {}
    }

    class RoundedButton extends JButton {
        private String text;
        public RoundedButton(String text, String empty) {
            this.text = text;
            setPreferredSize(new Dimension(300, 80));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(255, 140, 0), 0, getHeight(), new Color(255, 215, 0));
            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            g2.setColor(new Color(45, 52, 54));
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(text, x, y);
            g2.dispose();
        }
    }
}