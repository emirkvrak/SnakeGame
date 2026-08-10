package yilanoyunu.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import yilanoyunu.Kaynaklar;
import yilanoyunu.engine.OyunMotoru;
import yilanoyunu.model.Konum;
import yilanoyunu.model.OyunDurumu;
import yilanoyunu.model.Yon;
import yilanoyunu.model.GucTuru;

public final class OyunPaneli extends JPanel {
    private final OyunMotoru motor;
    private final Timer hareketTimer;
    private final Timer cizimTimer;
    private final Image yemGorseli;
    private final Image mayinGorseli;
    private final Image altinCoinGorseli;
    private final List<PuanAnimasyonu> puanAnimasyonlari = new ArrayList<>();
    private int sonSkor;
    private int enYuksekSkor;
    private long ipucuBaslangici;
    private long olumBaslangici;
    private long animasyonBaslangici;
    private Deque<Konum> oncekiYilan = new ArrayDeque<>();
    private final JButton yenidenBaslat;
    private Runnable skorDegisti;
    private Runnable oyunBitti;

    public OyunPaneli(OyunMotoru motor) {
        this.motor = motor;
        setFocusable(true);
        setLayout(null);
        setPreferredSize(new java.awt.Dimension(
                motor.ayarlar().sutun() * motor.ayarlar().hucreBoyutu(),
                motor.ayarlar().satir() * motor.ayarlar().hucreBoyutu()));
        yemGorseli = gorsel("elma.png");
        mayinGorseli = gorsel("bomba.png");
        altinCoinGorseli = gorsel("altin-coin.png");
        yenidenBaslat = new JButton("YENİDEN BAŞLAT");
        yenidenBaslat.setFocusPainted(false);
        yenidenBaslat.setForeground(Color.WHITE);
        yenidenBaslat.setBackground(new Color(32, 142, 84));
        yenidenBaslat.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        yenidenBaslat.setBorderPainted(false);
        yenidenBaslat.setMargin(new java.awt.Insets(8, 16, 8, 16));
        yenidenBaslat.setVisible(false);
        yenidenBaslat.addActionListener(event -> yenidenBaslat());
        add(yenidenBaslat);
        hareketTimer = new Timer(motor.ayarlar().timerMillis(), this::oyunDöngüsü);
        cizimTimer = new Timer(16, event -> {
            if ((motor.durum() == OyunDurumu.BITTI || motor.durum() == OyunDurumu.KAZANDI) && olumBaslangici > 0
                    && System.currentTimeMillis() - olumBaslangici > 700) ((Timer) event.getSource()).stop();
            repaint();
        });
        new KlavyeKontrolcusu(this, motor::yonDegistir, this::duraklatDevamEt);
    }

    private Image gorsel(String ad) {
        try {
            return Kaynaklar.ikon(ad).getImage();
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public void baslat() {
        motor.baslat();
        ipucuBaslangici = System.currentTimeMillis();
        olumBaslangici = 0;
        oncekiYilan = motor.yilan();
        animasyonBaslangici = System.currentTimeMillis();
        hareketTimer.start();
        cizimTimer.start();
        requestFocusInWindow();
        repaint();
    }

    public void yenidenBaslat() {
        motor.yenidenBaslat();
        sonSkor = 0;
        puanAnimasyonlari.clear();
        yenidenBaslat.setVisible(false);
        baslat();
    }

    private void duraklatDevamEt() {
        if (motor.durum() == OyunDurumu.BITTI || motor.durum() == OyunDurumu.KAZANDI
                || motor.durum() == OyunDurumu.HAZIR) return;
        motor.duraklatDevamEt();
        if (motor.durum() == OyunDurumu.DURAKLATILDI) {
            hareketTimer.stop();
        } else {
            hareketTimer.start();
        }
        repaint();
    }

    @Override public void doLayout() {
        int buttonWidth = 190;
        int buttonHeight = 38;
        yenidenBaslat.setBounds((getWidth() - buttonWidth) / 2,
                getHeight() / 2 + 42, buttonWidth, buttonHeight);
    }

    private void oyunDöngüsü(ActionEvent event) {
        oncekiYilan = motor.yilan();
        motor.ilerle();
        animasyonBaslangici = System.currentTimeMillis();
        int kazanilanPuan = motor.skor() - sonSkor;
        if (kazanilanPuan > 0) {
            Konum kafaKonumu = motor.yilan().peekFirst();
            puanAnimasyonlari.add(new PuanAnimasyonu(
                    new Konum(kafaKonumu.x(), Math.max(0, kafaKonumu.y() - 1)), kazanilanPuan));
            sonSkor = motor.skor();
        }
        int temelSure = motor.yavaslatmaAktif() ? 160 : 110;
        int yeniHareketSuresi = Math.max(55, temelSure - (motor.skor() / 50) * 8);
        hareketTimer.setDelay(yeniHareketSuresi);
        if (skorDegisti != null) skorDegisti.run();
        if (motor.durum() == OyunDurumu.BITTI || motor.durum() == OyunDurumu.KAZANDI) {
            hareketTimer.stop();
            if (olumBaslangici == 0) olumBaslangici = System.currentTimeMillis();
            yenidenBaslat.setVisible(true);
            if (oyunBitti != null) oyunBitti.run();
        }
        repaint();
    }

    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (olumBaslangici > 0 && System.currentTimeMillis() - olumBaslangici < 450) {
            int titremeX = (int) (Math.random() * 7) - 3;
            int titremeY = (int) (Math.random() * 7) - 3;
            g.translate(titremeX, titremeY);
        }
        tahtaCiz(g);
        int hucre = motor.ayarlar().hucreBoyutu();
        if (yemGorseli != null && motor.yem() != null) {
            g.drawImage(yemGorseli, motor.yem().x() * hucre, motor.yem().y() * hucre, hucre, hucre, this);
        }
        for (Konum engel : motor.engeller()) {
            if (mayinGorseli != null) g.drawImage(mayinGorseli, engel.x() * hucre, engel.y() * hucre, hucre, hucre, this);
        }
        if (motor.guc() != null) {
            Konum konum = motor.guc().konum();
            int x = konum.x() * hucre;
            int y = konum.y() * hucre;
            if (motor.guc().tur() == GucTuru.ALTIN_ELMA) {
                if (altinCoinGorseli != null) g.drawImage(altinCoinGorseli, x, y, hucre, hucre, this);
            } else {
                g.setColor(new Color(100, 220, 255));
                g.fillRoundRect(x + 3, y + 3, hucre - 6, hucre - 6, 8, 8);
                g.setColor(Color.WHITE);
                g.drawString("❄", x + 5, y + hucre - 6);
            }
        }
        Deque<Konum> mevcutYilan = motor.yilan();
        int toplamParca = mevcutYilan.size();
        double ilerleme = Math.min(1.0, (System.currentTimeMillis() - animasyonBaslangici)
                / (double) Math.max(1, hareketTimer.getDelay()));
        ilerleme = ilerleme * ilerleme * (3 - 2 * ilerleme);

        java.util.List<Point2D.Double> noktalar = new ArrayList<>();
        java.util.List<Double> xList = new ArrayList<>();
        java.util.List<Double> yList = new ArrayList<>();

        int parcaIndex = 0;
        for (Konum parca : mevcutYilan) {
            Konum onceki = oncekiKonum(parcaIndex);
            double x = onceki == null ? parca.x() : onceki.x() + (parca.x() - onceki.x()) * ilerleme;
            double y = onceki == null ? parca.y() : onceki.y() + (parca.y() - onceki.y()) * ilerleme;
            xList.add(x);
            yList.add(y);
            noktalar.add(new Point2D.Double(x * hucre + hucre / 2.0, y * hucre + hucre / 2.0));
            parcaIndex++;
        }

        g.setColor(new Color(43, 101, 177));
        g.setStroke(new BasicStroke(hucre - 5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < noktalar.size() - 1; i++) {
            Point2D.Double p1 = noktalar.get(i);
            Point2D.Double p2 = noktalar.get(i + 1);
            g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        }

        for (int i = toplamParca - 1; i >= 1; i--) {
            float oran = (float) i / Math.max(1, (toplamParca - 1));
            g.setColor(new Color(54 - (int) (oran * 20), 124 - (int) (oran * 45), 211 - (int) (oran * 65)));
            int parcaX = (int) (xList.get(i) * hucre) + 2;
            int parcaY = (int) (yList.get(i) * hucre) + 2;
            g.fillRoundRect(parcaX, parcaY, hucre - 4, hucre - 4, 10, 10);
            g.setColor(new Color(25, 72, 135));
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(parcaX, parcaY, hucre - 4, hucre - 4, 10, 10);
        }

        if (!xList.isEmpty()) {
            kafaCiz(g, xList.get(0) * hucre, yList.get(0) * hucre, hucre);
        }
        puanAnimasyonlariniCiz(g, hucre);
        ipucuCiz(g);
        if (motor.durum() == OyunDurumu.BITTI || motor.durum() == OyunDurumu.KAZANDI) oyunBittiEkrani(g);
        else if (motor.durum() == OyunDurumu.DURAKLATILDI) duraklatmaEkrani(g);
        g.dispose();
    }

    private Konum oncekiKonum(int index) {
        if (index >= oncekiYilan.size()) return null;
        int i = 0;
        for (Konum konum : oncekiYilan) {
            if (i++ == index) return konum;
        }
        return null;
    }

    private void kafaCiz(Graphics2D g, double x, double y, int hucre) {
        double centerX = x + hucre / 2.0;
        double centerY = y + hucre / 2.0;
        double ileriX = motor.yon().x();
        double ileriY = motor.yon().y();
        double yanX = -ileriY;
        double yanY = ileriX;
        g.setColor(new Color(63, 139, 225));
        g.fillRoundRect((int) x, (int) y, hucre, hucre, 12, 12);
        g.setColor(new Color(25, 72, 135));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect((int) x, (int) y, hucre - 1, hucre - 1, 12, 12);
        for (int taraf : new int[] {-1, 1}) {
            double eyeX = centerX + ileriX * 4 + yanX * taraf * 5;
            double eyeY = centerY + ileriY * 4 + yanY * taraf * 5;
            g.setColor(Color.WHITE);
            g.fillOval((int) eyeX - 4, (int) eyeY - 4, 8, 8);
            g.setColor(new Color(20, 35, 60));
            g.fillOval((int) (eyeX + ileriX * 2) - 2, (int) (eyeY + ileriY * 2) - 2, 4, 4);
        }
        g.setColor(new Color(235, 70, 85));
        int dilBaslangicX = (int) (centerX + ileriX * (hucre / 2.0 - 1));
        int dilBaslangicY = (int) (centerY + ileriY * (hucre / 2.0 - 1));
        int dilUcuX = (int) (centerX + ileriX * (hucre / 2.0 + 6));
        int dilUcuY = (int) (centerY + ileriY * (hucre / 2.0 + 6));
        g.drawLine(dilBaslangicX, dilBaslangicY, dilUcuX, dilUcuY);
        g.drawLine(dilUcuX, dilUcuY, dilUcuX - (int) (ileriX * 3) + (int) (yanX * 3), dilUcuY - (int) (ileriY * 3) + (int) (yanY * 3));
        g.drawLine(dilUcuX, dilUcuY, dilUcuX - (int) (ileriX * 3) - (int) (yanX * 3), dilUcuY - (int) (ileriY * 3) - (int) (yanY * 3));
    }

    private void tahtaCiz(Graphics2D g) {
        int hucre = motor.ayarlar().hucreBoyutu();
        g.setColor(new Color(186, 231, 143));
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int satir = 0; satir < motor.ayarlar().satir(); satir++) {
            for (int sutun = 0; sutun < motor.ayarlar().sutun(); sutun++) {
                g.setColor((satir + sutun) % 2 == 0
                        ? new Color(186, 231, 143) : new Color(174, 222, 130));
                g.fillRect(sutun * hucre, satir * hucre, hucre, hucre);
            }
        }
    }

    private void puanAnimasyonlariniCiz(Graphics2D g, int hucre) {
        Iterator<PuanAnimasyonu> iterator = puanAnimasyonlari.iterator();
        while (iterator.hasNext()) {
            PuanAnimasyonu puan = iterator.next();
            if (puan.expired()) { iterator.remove(); continue; }
            g.setColor(new Color(255, 245, 120));
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            g.drawString("+" + puan.deger, puan.x() * hucre, puan.y() * hucre - puan.yukselis());
        }
    }

    private static final class PuanAnimasyonu {
        private final Konum konum;
        private final int deger;
        private final long baslangic = System.currentTimeMillis();
        PuanAnimasyonu(Konum konum, int deger) { this.konum = konum; this.deger = deger; }
        boolean expired() { return System.currentTimeMillis() - baslangic > 700; }
        int x() { return konum.x(); }
        int y() { return konum.y(); }
        int yukselis() { return (int) ((System.currentTimeMillis() - baslangic) / 18); }
    }

    private void oyunBittiEkrani(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, getWidth(), getHeight());
        int kartGenislik = 360;
        int kartYukseklik = 210;
        int kartX = (getWidth() - kartGenislik) / 2;
        int kartY = (getHeight() - kartYukseklik) / 2 - 20;
        g.setColor(new Color(25, 31, 38, 245));
        g.fill(new RoundRectangle2D.Double(kartX, kartY, kartGenislik, kartYukseklik, 24, 24));
        g.setColor(new Color(255, 224, 72));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        String baslik = motor.durum() == OyunDurumu.KAZANDI ? "KAZANDINIZ" : "OYUN BİTTİ";
        g.drawString(baslik, (getWidth() - g.getFontMetrics().stringWidth(baslik)) / 2, kartY + 48);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        String skor = "Skorunuz: " + motor.skor();
        g.drawString(skor, (getWidth() - g.getFontMetrics().stringWidth(skor)) / 2, kartY + 88);
        g.setColor(new Color(255, 215, 90));
        String enIyi = "En iyi skor: " + enYuksekSkor;
        g.drawString(enIyi, (getWidth() - g.getFontMetrics().stringWidth(enIyi)) / 2, kartY + 120);
        g.setColor(new Color(190, 200, 210));
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        String aciklama = "Yeniden başlatmak için butona bas";
        g.drawString(aciklama, (getWidth() - g.getFontMetrics().stringWidth(aciklama)) / 2, kartY + 154);
        if (olumBaslangici > 0 && System.currentTimeMillis() - olumBaslangici < 350) {
            g.setColor(new Color(220, 35, 35, 65));
            g.fillRect(-10, -10, getWidth() + 20, getHeight() + 20);
        }
    }

    private void ipucuCiz(Graphics2D g) {
        if (ipucuBaslangici == 0) return;
        long gecen = System.currentTimeMillis() - ipucuBaslangici;
        if (gecen > 3500) return;
        int alpha = gecen < 2500 ? 210 : (int) (210 * (3500 - gecen) / 1000);
        g.setColor(new Color(0, 0, 0, Math.max(0, alpha)));
        g.fillRoundRect(130, getHeight() - 48, getWidth() - 260, 30, 15, 15);
        g.setColor(new Color(255, 255, 255, Math.max(0, alpha)));
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        String metin = "Hareket etmek için ok tuşlarını veya WASD'yi kullan";
        g.drawString(metin, (getWidth() - g.getFontMetrics().stringWidth(metin)) / 2, getHeight() - 28);
    }

    private void duraklatmaEkrani(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 145));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        String metin = "DURAKLATILDI";
        g.drawString(metin, (getWidth() - g.getFontMetrics().stringWidth(metin)) / 2, getHeight() / 2);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
        String aciklama = "Devam etmek için P veya ESC tuşuna bas";
        g.drawString(aciklama, (getWidth() - g.getFontMetrics().stringWidth(aciklama)) / 2, getHeight() / 2 + 32);
    }

    public void setSkorDegisti(Runnable listener) { skorDegisti = listener; }
    public void setOyunBitti(Runnable listener) { oyunBitti = listener; }
    public void setEnYuksekSkor(int skor) { enYuksekSkor = skor; }
}
