package yilanoyunu.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void yemYeninceSkorArtmaliVeYilanBuyumeli() {
        Konum hedef = motor.yem();
        motor.baslat();

        if (hedef.x() < 5) {
            adim(Yon.YUKARI);
            while (motor.yilan().peekFirst().x() > hedef.x()) adim(Yon.SOL);
        } else {
            while (motor.yilan().peekFirst().x() < hedef.x()) adim(Yon.SAG);
        }
        while (motor.yilan().peekFirst().y() > hedef.y()) adim(Yon.YUKARI);
        while (motor.yilan().peekFirst().y() < hedef.y()) adim(Yon.ASAGI);

        assertEquals(10, motor.skor());
        assertEquals(2, motor.yilan().size());
    }

    @Test
    void tahtaDoldugundaGecerliKazanmaDurumuOlusturmali() {
        OyunMotoru kucukMotor = new OyunMotoru(new OyunAyarlari(1, 1, 24, 100), new Random(42));

        assertEquals(OyunDurumu.KAZANDI, kucukMotor.durum());
        assertNull(kucukMotor.yem());
    }

    @Test
    void uretilenYemYilaninUzerindeOlmamali() {
        for (int i = 0; i < 50; i++) {
            assertTrue(!motor.yilan().contains(motor.yem()));
            motor.yenidenBaslat();
        }
    }

    private void adim(Yon yon) {
        motor.yonDegistir(yon);
        motor.ilerle();
    }
}
