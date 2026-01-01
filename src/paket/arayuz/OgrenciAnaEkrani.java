package paket.arayuz;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.io.File;
import paket.veritabani.VeritabaniBaglantisi;

public class OgrenciAnaEkrani extends JFrame {
    private int aktifId;
    private String tc;
    private Color sarıTema = new Color(255, 241, 184);
    private JPanel pnlMerkez;
    private CardLayout kartlar;

    public OgrenciAnaEkrani(int id, String tc) {
        this.aktifId = id;
        this.tc = tc;
        setTitle("KYK Öğrenci Paneli");
        setSize(1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(45, 52, 54));
        pnlHeader.setPreferredSize(new Dimension(1000, 80));
        
        JLabel lblBaslik = new JLabel("  KYK PANELİ");
        lblBaslik.setForeground(Color.WHITE);
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel pnlSag = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlSag.setOpaque(false);
        try {
            ImageIcon icon = new ImageIcon("images/kyk-logo.png");
            pnlSag.add(new JLabel(new ImageIcon(icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH))));
        } catch(Exception e) {}
        
        JButton btnCikis = new JButton("Çıkış Yap");
        btnCikis.addActionListener(e -> { new GirisEkrani().setVisible(true); this.dispose(); });
        pnlSag.add(btnCikis);
        
        pnlHeader.add(lblBaslik, BorderLayout.WEST); 
        pnlHeader.add(pnlSag, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        kartlar = new CardLayout();
        pnlMerkez = new JPanel(kartlar);
        
        pnlMerkez.add(panelAnaMenu(), "MENU");
        pnlMerkez.add(panelKantin(), "KANTIN");
        pnlMerkez.add(panelCamasir(), "CAMASIR");
        pnlMerkez.add(panelTeknik(), "TEKNIK");

        add(pnlMerkez, BorderLayout.CENTER);
        kartlar.show(pnlMerkez, "MENU");
    }

    private JPanel panelAnaMenu() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(sarıTema);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        RoundedGradientButton b1 = new RoundedGradientButton("KANTİN", "🛒", 280, 250);
        RoundedGradientButton b2 = new RoundedGradientButton("ÇAMAŞIRHANE", "🧺", 280, 250);
        RoundedGradientButton b3 = new RoundedGradientButton("TEKNİK DESTEK", "🛠️", 280, 250);

        b1.addActionListener(e -> kartlar.show(pnlMerkez, "KANTIN"));
        b2.addActionListener(e -> kartlar.show(pnlMerkez, "CAMASIR"));
        b3.addActionListener(e -> kartlar.show(pnlMerkez, "TEKNIK"));

        gbc.gridy = 0; gbc.gridx = 0; pnl.add(b1, gbc);
        gbc.gridx = 1; pnl.add(b2, gbc);
        gbc.gridx = 2; pnl.add(b3, gbc);

        RoundedGradientButton bMenu = new RoundedGradientButton("GÜNÜN MENÜSÜ", "🍴", 940, 100);
        menuMetniniYukle(bMenu);
        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 3;
        pnl.add(bMenu, gbc);

        return pnl;
    }

    private void menuMetniniYukle(RoundedGradientButton btn) {
        try (Connection c = VeritabaniBaglantisi.baglan()) {
            ResultSet rs = c.createStatement().executeQuery("SELECT liste FROM yemek_menusu WHERE tarih = CURDATE()");
            if (rs.next()) btn.setCustomText("🍴 GÜNÜN MENÜSÜ: " + rs.getString("liste"));
            else btn.setCustomText("🍴 Günün menüsü henüz girilmedi.");
        } catch (Exception e) {}
    }

    class RoundedGradientButton extends JButton {
        private String text;
        private String icon;
        private int w, h;

        public RoundedGradientButton(String text, String icon, int w, int h) {
            this.text = text;
            this.icon = icon;
            this.w = w;
            this.h = h;
            setPreferredSize(new Dimension(w, h));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        public void setCustomText(String newText) {
            this.text = newText;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(255, 140, 0), 0, getHeight(), new Color(255, 215, 0));
            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            g2.setColor(new Color(45, 52, 54));
            
            if (h > 150) {
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70));
                FontMetrics fmI = g2.getFontMetrics();
                g2.drawString(icon, (getWidth() - fmI.stringWidth(icon)) / 2, 120);
                g2.setFont(new Font("Arial", Font.BOLD, 22));
                FontMetrics fmT = g2.getFontMetrics();
                g2.drawString(text, (getWidth() - fmT.stringWidth(text)) / 2, 190);
            } else {
                g2.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(text, (getWidth() - fm.stringWidth(text)) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
            g2.dispose();
        }
    }

    private JPanel panelKantin() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(sarıTema);
        
        JPanel pnlUst = new JPanel(new BorderLayout());
        pnlUst.setOpaque(false);
        JButton bGeri = new JButton("⬅ Geri");
        bGeri.addActionListener(e -> kartlar.show(pnlMerkez, "MENU"));
        JLabel lblBaslik = new JLabel("KANTİN ÜRÜNLERİ", SwingConstants.CENTER);
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 24));
        lblBaslik.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        pnlUst.add(bGeri, BorderLayout.WEST);
        pnlUst.add(lblBaslik, BorderLayout.CENTER);
        p.add(pnlUst, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 5, 15, 15));
        grid.setBackground(sarıTema);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        try (Connection conn = VeritabaniBaglantisi.baglan()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM urunler");
            while (rs.next()) {
                JPanel kart = new JPanel(new BorderLayout());
                kart.setBackground(Color.WHITE);
                kart.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                String yol = rs.getString("resim_yolu");
                JLabel imgLbl;
                File f = new File(yol != null ? yol : "");
                if (f.exists()) imgLbl = new JLabel(new ImageIcon(new ImageIcon(yol).getImage().getScaledInstance(150, 130, Image.SCALE_SMOOTH)));
                else imgLbl = new JLabel("Resim Yok", SwingConstants.CENTER);
                JLabel info = new JLabel("<html><center>" + rs.getString("isim") + "<br>" + rs.getDouble("fiyat") + " TL</center></html>", SwingConstants.CENTER);
                kart.add(imgLbl, BorderLayout.CENTER);
                kart.add(info, BorderLayout.SOUTH);
                grid.add(kart);
            }
        } catch (Exception e) {}
        p.add(new JScrollPane(grid), BorderLayout.CENTER);
        return p;
    }

    private JPanel panelCamasir() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(sarıTema);
        
        JPanel pnlUst = new JPanel(new BorderLayout());
        pnlUst.setOpaque(false);
        JButton bGeri = new JButton("⬅ Geri");
        bGeri.addActionListener(e -> kartlar.show(pnlMerkez, "MENU"));
        JLabel lblBaslik = new JLabel("ÇAMAŞIR MAKİNE DURUMLARI", SwingConstants.CENTER);
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 24));
        lblBaslik.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        pnlUst.add(bGeri, BorderLayout.WEST);
        pnlUst.add(lblBaslik, BorderLayout.CENTER);
        p.add(pnlUst, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(6, 5, 10, 10));
        grid.setBackground(sarıTema);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        makineleriGuncelle(grid);
        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private void makineleriGuncelle(JPanel p) {
        p.removeAll();
        try (Connection conn = VeritabaniBaglantisi.baglan()) {
            for (int i = 1; i <= 30; i++) {
                final int mNo = i;
                JButton btn = new JButton("M-" + i);
                btn.setOpaque(true);
                btn.setFont(new Font("Arial", Font.BOLD, 12));
                PreparedStatement ps = conn.prepareStatement("SELECT bitis_zamani FROM makineler WHERE makine_no=?");
                ps.setInt(1, mNo);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Timestamp bitis = rs.getTimestamp("bitis_zamani");
                    if (bitis != null) {
                        long kalan = (bitis.getTime() - System.currentTimeMillis()) / 60000;
                        if (kalan > 0) {
                            btn.setBackground(new Color(255, 230, 230));
                            btn.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 4));
                            btn.setText("<html><center>M-" + i + "<br>" + kalan + " dk</center></html>");
                        } else {
                            btn.setBackground(new Color(230, 255, 230));
                            btn.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 4));
                        }
                    } else {
                        btn.setBackground(new Color(230, 255, 230));
                        btn.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 4));
                    }
                } else {
                    btn.setBackground(new Color(230, 255, 230));
                    btn.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 4));
                }
                btn.addActionListener(e -> {
                    if (btn.getBackground().getRed() > 240) {
                        try (Connection c2 = VeritabaniBaglantisi.baglan()) {
                            PreparedStatement ps2 = c2.prepareStatement("SELECT * FROM makineler WHERE makine_no=?");
                            ps2.setInt(1, mNo);
                            ResultSet rs2 = ps2.executeQuery();
                            if (rs2.next()) JOptionPane.showMessageDialog(this, "Kullanıcı: " + rs2.getString("ogr_ad") + " " + rs2.getString("ogr_soyad") + "\nOda: " + rs2.getString("ogr_oda"));
                        } catch (Exception ex) {}
                    } else makineBaslat(mNo, p);
                });
                p.add(btn);
            }
        } catch (Exception e) {}
        p.revalidate(); p.repaint();
    }

    private void makineBaslat(int no, JPanel p) {
        JTextField ad = new JTextField(); JTextField oda = new JTextField();
        Object[] m = {"Ad:", ad, "Oda:", oda};
        if (JOptionPane.showConfirmDialog(null, m, "Başlat", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection c3 = VeritabaniBaglantisi.baglan()) {
                PreparedStatement ps3 = c3.prepareStatement("INSERT INTO makineler (makine_no, bitis_zamani, dolu_mu, ogr_ad, ogr_oda) VALUES (?, DATE_ADD(NOW(), INTERVAL 90 MINUTE), 1, ?, ?) ON DUPLICATE KEY UPDATE bitis_zamani=DATE_ADD(NOW(), INTERVAL 90 MINUTE), dolu_mu=1, ogr_ad=?, ogr_oda=?");
                ps3.setInt(1, no); ps3.setString(2, ad.getText()); ps3.setString(3, oda.getText()); ps3.setString(4, ad.getText()); ps3.setString(5, oda.getText());
                ps3.executeUpdate();
                makineleriGuncelle(p);
            } catch (Exception ex) {}
        }
    }

    private JPanel panelTeknik() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(sarıTema);
        
        JPanel pnlUst = new JPanel(new BorderLayout());
        pnlUst.setOpaque(false);
        JButton bGeri = new JButton("⬅ Geri");
        bGeri.addActionListener(e -> kartlar.show(pnlMerkez, "MENU"));
        JLabel lblBaslik = new JLabel("TEKNİK ARIZA TALEP FORMU", SwingConstants.CENTER);
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 24));
        lblBaslik.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        pnlUst.add(bGeri, BorderLayout.WEST);
        pnlUst.add(lblBaslik, BorderLayout.CENTER);
        p.add(pnlUst, BorderLayout.NORTH);
        
        JPanel f = new JPanel(new GridBagLayout());
        f.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JComboBox<String> cb = new JComboBox<>(new String[]{"Banyo Arızası", "Elektrik Sorunu", "Mobilya Kırığı", "İnternet Bağlantısı"});
        cb.setPreferredSize(new Dimension(400, 40));
        JTextField ta = new JTextField();
        ta.setPreferredSize(new Dimension(400, 100));
        
        RoundedGradientButton bg = new RoundedGradientButton("Talebi Gönder", "📩", 400, 70);
        bg.addActionListener(e -> {
            try (Connection c = VeritabaniBaglantisi.baglan()) {
                PreparedStatement ps = c.prepareStatement("INSERT INTO teknik_talepler (ogrenci_id, kategori, aciklama) VALUES (?,?,?)");
                ps.setInt(1, aktifId); ps.setString(2, cb.getSelectedItem().toString()); ps.setString(3, ta.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Talebiniz Kaydedildi!");
            } catch (Exception ex) {}
        });

        f.add(new JLabel("Arıza Kategorisi:"), gbc);
        f.add(cb, gbc);
        f.add(new JLabel("Açıklama:"), gbc);
        f.add(ta, gbc);
        f.add(bg, gbc);
        
        p.add(f, BorderLayout.CENTER);
        return p;
    }
}