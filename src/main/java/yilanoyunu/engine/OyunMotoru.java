package yilanoyunu.engine;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.Set;
import yilanoyunu.model.Konum;
import yilanoyunu.model.OyunAyarlari;
import yilanoyunu.model.OyunDurumu;
import yilanoyunu.model.Yon;
import yilanoyunu.model.Guc;
import yilanoyunu.model.GucTuru;

/** Oyunun kurallarını Swing arayüzünden bağımsız olarak yönetir. */
public final class OyunMotoru {
    private final OyunAyarlari ayarlar;
    private final Random random;
    private final Deque<Konum> yilan = new ArrayDeque<>();
    private final Set<Konum> engeller = new HashSet<>();
    private Konum yem;
    private Yon yon = Yon.SAG;
    private final Deque<Yon> yonKuyrugu = new ArrayDeque<>();
    private OyunDurumu durum = OyunDurumu.HAZIR;
    private int skor;
    private Guc guc;
    private int gucKalanHareket;
    private int yavaslatmaKalanHareket;

    public OyunMotoru(OyunAyarlari ayarlar) {
        this(ayarlar, new Random());
    }

    OyunMotoru(OyunAyarlari ayarlar, Random random) {
        this.ayarlar = ayarlar;
        this.random = random;
        yenidenBaslat();
    }

    public void baslat() {
        if (durum == OyunDurumu.HAZIR) durum = OyunDurumu.OYNUYOR;
    }

    public void duraklatDevamEt() {
        if (durum == OyunDurumu.OYNUYOR) durum = OyunDurumu.DURAKLATILDI;
        else if (durum == OyunDurumu.DURAKLATILDI) durum = OyunDurumu.OYNUYOR;
    }

    public void yenidenBaslat() {
        yilan.clear();
        engeller.clear();
        yilan.addFirst(new Konum(ayarlar.sutun() / 2, ayarlar.satir() / 2));
        yem = rastgeleBosKonum();
        yon = Yon.SAG;
        yonKuyrugu.clear();
        skor = 0;
        guc = null;
        gucKalanHareket = 0;
        yavaslatmaKalanHareket = 0;
        durum = OyunDurumu.HAZIR;
    }

    public void yonDegistir(Yon yeniYon) {
        Yon sonYon = yonKuyrugu.isEmpty() ? yon : yonKuyrugu.peekLast();
        if (!yeniYon.ters(sonYon) && yeniYon != sonYon && yonKuyrugu.size() < 2) {
            yonKuyrugu.addLast(yeniYon);
        }
    }

    public void ilerle() {
        if (durum != OyunDurumu.OYNUYOR) return;
        if (!yonKuyrugu.isEmpty()) {
            yon = yonKuyrugu.removeFirst();
        }
        if (guc != null && --gucKalanHareket <= 0) guc = null;
        if (yavaslatmaKalanHareket > 0) yavaslatmaKalanHareket--;
        Konum kafa = yilan.peekFirst().ilerle(yon);
        if (duvaraCarpti(kafa) || yilan.contains(kafa) || engeller.contains(kafa)) {
            durum = OyunDurumu.BITTI;
            return;
        }
        yilan.addFirst(kafa);
        if (kafa.equals(yem)) {
            skor += 10;
            yem = rastgeleBosKonum();
            if (skor % 50 == 0) engeller.add(rastgeleBosKonum());
            if (skor % 30 == 0 && guc == null) {
                guc = new Guc(rastgeleBosKonum(), random.nextBoolean() ? GucTuru.ALTIN_ELMA : GucTuru.BUZ);
                gucKalanHareket = 55;
            }
        } else {
            yilan.removeLast();
        }
        if (guc != null && kafa.equals(guc.konum())) {
            if (guc.tur() == GucTuru.ALTIN_ELMA) skor += 50;
            else yavaslatmaKalanHareket = 45;
            guc = null;
        }
    }

    private boolean duvaraCarpti(Konum konum) {
        return konum.x() < 0 || konum.x() >= ayarlar.sutun()
                || konum.y() < 0 || konum.y() >= ayarlar.satir();
    }

    private Konum rastgeleBosKonum() {
        for (int deneme = 0; deneme < 1000; deneme++) {
            Konum aday = new Konum(random.nextInt(ayarlar.sutun()), random.nextInt(ayarlar.satir()));
            if (!yilan.contains(aday) && !engeller.contains(aday) && !aday.equals(yem)) return aday;
        }
        return new Konum(0, 0);
    }

    public Deque<Konum> yilan() { return new ArrayDeque<>(yilan); }
    public Set<Konum> engeller() { return Set.copyOf(engeller); }
    public Konum yem() { return yem; }
    public int skor() { return skor; }
    public OyunDurumu durum() { return durum; }
    public OyunAyarlari ayarlar() { return ayarlar; }
    public Yon yon() { return yon; }
    public int seviye() { return Math.min(4, skor / 100 + 1); }
    public Guc guc() { return guc; }
    public boolean yavaslatmaAktif() { return yavaslatmaKalanHareket > 0; }
}
