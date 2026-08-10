package yilanoyunu.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yilanoyunu.model.Konum;
import yilanoyunu.model.OyunAyarlari;
import yilanoyunu.model.OyunDurumu;
import yilanoyunu.model.Yon;

class OyunMotoruTest {
    private OyunMotoru motor;

    @BeforeEach
    void setUp() {
        motor = new OyunMotoru(new OyunAyarlari(10, 10, 24, 100), new Random(42));
    }

    @Test
    void oyunBaslangictaHazirOlmali() {
        assertEquals(OyunDurumu.HAZIR, motor.durum());
        assertEquals(new Konum(5, 5), motor.yilan().peekFirst());
    }

    @Test
    void baslatildigindaYilanSagaHareketEtmeli() {
        motor.baslat();
        motor.ilerle();

        assertEquals(new Konum(6, 5), motor.yilan().peekFirst());
    }

    @Test
    void yilanDogrudanTersYoneDonememeli() {
        motor.baslat();
        motor.yonDegistir(Yon.SOL);
        motor.ilerle();

        assertEquals(new Konum(6, 5), motor.yilan().peekFirst());
    }

    @Test
    void hizliDonuslerGuvenliSekildeSiralanmali() {
        motor.baslat();
        motor.yonDegistir(Yon.YUKARI);
        motor.yonDegistir(Yon.SOL);
        motor.ilerle();

        assertEquals(new Konum(5, 4), motor.yilan().peekFirst());
        motor.ilerle();
        assertEquals(new Konum(4, 4), motor.yilan().peekFirst());
    }

    @Test
    void yenidenBaslatSkoruVeDurumuSifirlamali() {
        motor.baslat();
        motor.ilerle();
        motor.yenidenBaslat();

        assertEquals(0, motor.skor());
        assertEquals(OyunDurumu.HAZIR, motor.durum());
        assertNotEquals(new Konum(6, 5), motor.yilan().peekFirst());
    }
}
