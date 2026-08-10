package yilanoyunu.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import yilanoyunu.engine.OyunMotoru;
import yilanoyunu.Kaynaklar;
import yilanoyunu.model.OyunAyarlari;

public final class OyunPenceresi extends JFrame {
    private static final String MENU = "menu";
    private static final String OYUN = "oyun";

    private final OyunMotoru motor = new OyunMotoru(OyunAyarlari.varsayilan());
    private final OyunPaneli oyunPaneli = new OyunPaneli(motor);
    private final JLabel skorEtiketi = new JLabel("Skor: 0");
    private final JLabel enYuksekSkorEtiketi = new JLabel();
    private final JLabel seviyeEtiketi = new JLabel("Seviye: 1");
    private final SkorKaydi skorKaydi = new SkorKaydi();
    private final SesOynatici ses = new SesOynatici();
    private final CardLayout kartLayout = new CardLayout();
    private final JPanel kartlar = new JPanel(kartLayout);
    private Timer menuAnimasyon;

    public OyunPenceresi() {
        setTitle("Yılan Oyunu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        kartlar.add(baslangicEkrani(), MENU);
        kartlar.add(oyunEkrani(), OYUN);
        setContentPane(kartlar);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent event) { ses.close(); }
        });
        setVisible(true);
    }

    private JPanel baslangicEkrani() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g = (Graphics2D) graphics.create();
                g.setPaint(new GradientPaint(0, 0, new Color(20, 74, 50), 0, getHeight(), new Color(8, 25, 28)));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(600, 626));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 20, 10, 20);
        JLabel baslik = new JLabel("YILAN OYUNU");
        baslik.setForeground(new Color(255, 224, 72));
        baslik.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 42));
        panel.add(baslik, gbc);
        JLabel altBaslik = new JLabel("Klasik oyunun modern yorumu");
        altBaslik.setForeground(new Color(205, 230, 215));
        altBaslik.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        panel.add(altBaslik, gbc);
        JButton baslat = new JButton("OYUNU BAŞLAT");
        baslat.setPreferredSize(new Dimension(210, 48));
        baslat.setFocusPainted(false);
        baslat.setForeground(Color.WHITE);
        baslat.setBackground(new Color(32, 142, 84));
        baslat.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        baslat.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        baslat.addActionListener(event -> oyunuBaslat());
        panel.add(baslat, gbc);
        menuAnimasyon = new Timer(550, event -> {
            Color mevcut = baslat.getBackground();
            baslat.setBackground(mevcut.equals(new Color(32, 142, 84))
                    ? new Color(45, 170, 102) : new Color(32, 142, 84));
        });
        menuAnimasyon.start();
        JLabel kontroller = new JLabel("Ok tuşları / WASD: hareket   •   P / ESC: duraklat");
        kontroller.setForeground(new Color(175, 200, 185));
        kontroller.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        panel.add(kontroller, gbc);
        return panel;
    }

    private JPanel oyunEkrani() {
        JPanel ekran = new JPanel(new BorderLayout());
        ekran.setBackground(new Color(186, 231, 143));
        JPanel ustPanel = new JPanel(new BorderLayout(12, 0));
        ustPanel.setBackground(new Color(247, 251, 241));
        ustPanel.setBorder(new EmptyBorder(8, 14, 8, 14));
        skorEtiketi.setForeground(new Color(35, 55, 42));
        skorEtiketi.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        enYuksekSkorEtiketi.setText("En yüksek: " + skorKaydi.oku());
        enYuksekSkorEtiketi.setForeground(new Color(125, 92, 20));
        enYuksekSkorEtiketi.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        seviyeEtiketi.setForeground(new Color(38, 115, 70));
        seviyeEtiketi.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        JPanel skorlar = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        skorlar.setOpaque(false);
        skorlar.add(new JLabel(Kaynaklar.ikon("elma.png")));
        skorlar.add(skorEtiketi);
        skorlar.add(enYuksekSkorEtiketi);
        skorlar.add(seviyeEtiketi);
        ustPanel.add(skorlar, BorderLayout.WEST);
        JLabel bilgi = new JLabel("WASD / Ok tuşları   •   P: Duraklat");
        bilgi.setForeground(new Color(87, 105, 91));
        bilgi.setHorizontalAlignment(SwingConstants.RIGHT);
        ustPanel.add(bilgi, BorderLayout.EAST);
        ekran.add(ustPanel, BorderLayout.NORTH);
        ekran.add(oyunPaneli, BorderLayout.CENTER);
        oyunPaneli.setSkorDegisti(() -> {
            skorEtiketi.setText("Skor: " + motor.skor());
            int enYuksek = skorKaydi.guncelle(motor.skor());
            enYuksekSkorEtiketi.setText("En yüksek: " + enYuksek);
            oyunPaneli.setEnYuksekSkor(enYuksek);
            seviyeEtiketi.setText("Seviye: " + motor.seviye());
        });
        return ekran;
    }

    private void oyunuBaslat() {
        if (menuAnimasyon != null) menuAnimasyon.stop();
        kartLayout.show(kartlar, OYUN);
        ses.baslat("background-music.wav");
        oyunPaneli.baslat();
        pack();
        setLocationRelativeTo(null);
    }
}
