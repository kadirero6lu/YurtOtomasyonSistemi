package paket.arayuz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.io.File;
import java.nio.file.*;
import paket.veritabani.VeritabaniBaglantisi;

public class IdareAnaEkrani extends JFrame {
    private DefaultTableModel mUrun = new DefaultTableModel(new String[]{"ID", "İsim", "Fiyat"}, 0);
    private DefaultTableModel mTalep = new DefaultTableModel(new String[]{"ID", "Öğrenci", "Kategori", "Açıklama"}, 0);
    private JTable tUrun = new JTable(mUrun);
    private Color sarıTema = new Color(255, 241, 184);

    public IdareAnaEkrani() {
        setTitle("KYK İdare Yönetim Paneli");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(45, 52, 54));
        pnlHeader.setPreferredSize(new Dimension(1100, 80));
        
        JLabel lblBaslik = new JLabel("  KYK İDARE PANELİ");
        lblBaslik.setForeground(Color.WHITE);
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel pnlSag = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlSag.setOpaque(false);
        try {
            ImageIcon icon = new ImageIcon("images/kyk-logo.png");
            pnlSag.add(new JLabel(new ImageIcon(icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH))));
        } catch(Exception e) {}
        
        JButton btnCikis = new JButton("Güvenli Çıkış");
        btnCikis.addActionListener(e -> { new GirisEkrani().setVisible(true); this.dispose(); });
        pnlSag.add(btnCikis);
        
        pnlHeader.add(lblBaslik, BorderLayout.WEST); 
        pnlHeader.add(pnlSag, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlMerkez = new JPanel(new GridLayout(2, 1, 15, 15));
        pnlMerkez.setBackground(sarıTema);
        pnlMerkez.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel pnlTalepler = new JPanel(new BorderLayout());
        pnlTalepler.setOpaque(false);
        JLabel lblTalepBaslik = new JLabel("Gelen Teknik Talepler", SwingConstants.LEFT);
        lblTalepBaslik.setFont(new Font("Arial", Font.BOLD, 16));
        pnlTalepler.add(lblTalepBaslik, BorderLayout.NORTH);
        pnlTalepler.add(new JScrollPane(new JTable(mTalep)), BorderLayout.CENTER);
        
        JPanel pnlUrunYonetim = new JPanel(new BorderLayout());
        pnlUrunYonetim.setOpaque(false);
        JLabel lblUrunBaslik = new JLabel("Kantin Ürün Yönetimi", SwingConstants.LEFT);
        lblUrunBaslik.setFont(new Font("Arial", Font.BOLD, 16));
        pnlUrunYonetim.add(lblUrunBaslik, BorderLayout.NORTH);
        pnlUrunYonetim.add(new JScrollPane(tUrun), BorderLayout.CENTER);
        
        RoundedGradientButton btnSil = new RoundedGradientButton("Seçili Ürünü Sil", "🗑️", 200, 50);
        btnSil.addActionListener(e -> {
            int r = tUrun.getSelectedRow();
            if (r != -1) {
                try (Connection conn = VeritabaniBaglantisi.baglan()) {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM urunler WHERE id=?");
                    ps.setInt(1, (int)mUrun.getValueAt(r, 0));
                    ps.executeUpdate();
                    verileriYukle();
                } catch (Exception ex) {}
            }
        });
        pnlUrunYonetim.add(btnSil, BorderLayout.SOUTH);
        
        pnlMerkez.add(pnlTalepler);
        pnlMerkez.add(pnlUrunYonetim);
        add(pnlMerkez, BorderLayout.CENTER);

        JPanel pnlAlt = new JPanel(new GridLayout(2, 1));
        pnlAlt.setPreferredSize(new Dimension(1100, 180));
        
        JPanel pnlUrunEkle = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlUrunEkle.setBackground(sarıTema);
        pnlUrunEkle.setBorder(BorderFactory.createTitledBorder("Yeni Ürün Ekle"));
        JTextField txtAd = new JTextField(12);
        JTextField txtFiyat = new JTextField(6);
        RoundedGradientButton btnGorsel = new RoundedGradientButton("Görsel Seç", "🖼️", 160, 45);
        RoundedGradientButton btnEkle = new RoundedGradientButton("Ürünü Kaydet", "➕", 160, 45);
        final String[] path = {""};
        
        btnGorsel.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File s = fc.getSelectedFile();
                    Path d = Paths.get("images/" + s.getName());
                    Files.copy(s.toPath(), d, StandardCopyOption.REPLACE_EXISTING);
                    path[0] = "images/" + s.getName();
                    btnGorsel.setText(s.getName());
                } catch(Exception ex) {}
            }
        });
        
        btnEkle.addActionListener(e -> {
            try (Connection conn = VeritabaniBaglantisi.baglan()) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO urunler (isim, fiyat, resim_yolu) VALUES (?,?,?)");
                ps.setString(1, txtAd.getText());
                ps.setDouble(2, Double.parseDouble(txtFiyat.getText()));
                ps.setString(3, path[0]);
                ps.executeUpdate();
                verileriYukle();
                txtAd.setText(""); txtFiyat.setText("");
            } catch (Exception ex) {}
        });
        
        pnlUrunEkle.add(new JLabel("Ürün Adı:")); pnlUrunEkle.add(txtAd);
        pnlUrunEkle.add(new JLabel("Fiyat:")); pnlUrunEkle.add(txtFiyat);
        pnlUrunEkle.add(btnGorsel); pnlUrunEkle.add(btnEkle);

        JPanel pnlMenuGuncelle = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlMenuGuncelle.setBackground(sarıTema);
        pnlMenuGuncelle.setBorder(BorderFactory.createTitledBorder("Günün Menüsünü Yayınla"));
        JTextField txtMenu = new JTextField(40);
        RoundedGradientButton btnMenuGuncelle = new RoundedGradientButton("Menüyü Güncelle", "🥗", 200, 45);
        
        btnMenuGuncelle.addActionListener(e -> {
            try (Connection conn = VeritabaniBaglantisi.baglan()) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO yemek_menusu (tarih, liste) VALUES (CURDATE(), ?) ON DUPLICATE KEY UPDATE liste=?");
                ps.setString(1, txtMenu.getText());
                ps.setString(2, txtMenu.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Günün menüsü başarıyla güncellendi!");
            } catch (Exception ex) {}
        });
        
        pnlMenuGuncelle.add(new JLabel("Menü İçeriği:")); pnlMenuGuncelle.add(txtMenu);
        pnlMenuGuncelle.add(btnMenuGuncelle);
        
        pnlAlt.add(pnlUrunEkle);
        pnlAlt.add(pnlMenuGuncelle);
        add(pnlAlt, BorderLayout.SOUTH);
        verileriYukle();
    }

    private void verileriYukle() {
        mUrun.setRowCount(0); mTalep.setRowCount(0);
        try (Connection conn = VeritabaniBaglantisi.baglan()) {
            ResultSet rs1 = conn.createStatement().executeQuery("SELECT * FROM urunler");
            while(rs1.next()) mUrun.addRow(new Object[]{rs1.getInt("id"), rs1.getString("isim"), rs1.getDouble("fiyat")});
            ResultSet rs2 = conn.createStatement().executeQuery("SELECT * FROM teknik_talepler");
            while(rs2.next()) mTalep.addRow(new Object[]{rs2.getInt("id"), rs2.getInt("ogrenci_id"), rs2.getString("kategori"), rs2.getString("aciklama")});
        } catch (Exception e) {}
    }

    class RoundedGradientButton extends JButton {
        private String text;
        private String icon;

        public RoundedGradientButton(String text, String icon, int w, int h) {
            this.text = text;
            this.icon = icon;
            setPreferredSize(new Dimension(w, h));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        public void setText(String text) {
            this.text = text;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(255, 140, 0), 0, getHeight(), new Color(255, 215, 0));
            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            g2.setColor(new Color(45, 52, 54));
            g2.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            String fullText = icon + " " + text;
            int x = (getWidth() - fm.stringWidth(fullText)) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(fullText, x, y);
            g2.dispose();
        }
    }
}