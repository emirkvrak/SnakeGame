package yilanoyunu.ui;

import java.util.prefs.Preferences;

/** En yüksek skoru bilgisayarda kullanıcıya özel olarak saklar. */
public final class SkorKaydi {
    private static final String EN_YUKSEK_SKOR = "enYuksekSkor";
    private final Preferences tercihler = Preferences.userNodeForPackage(SkorKaydi.class);

    public int oku() {
        return tercihler.getInt(EN_YUKSEK_SKOR, 0);
    }

    public int guncelle(int skor) {
        if (skor > oku()) tercihler.putInt(EN_YUKSEK_SKOR, skor);
        return oku();
    }
}
