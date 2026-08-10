package yilanoyunu;

import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;

/** Uygulama kaynaklarını IDE'den, JAR'dan veya paketlenmiş uygulamadan okur. */
public final class Kaynaklar {

    private Kaynaklar() {
    }

    public static URL url(String dosyaAdi) {
        URL kaynak = Kaynaklar.class.getResource("/" + dosyaAdi);
        if (kaynak == null) {
            throw new IllegalArgumentException("Kaynak bulunamadı: " + dosyaAdi);
        }
        return kaynak;
    }

    public static InputStream akis(String dosyaAdi) {
        try {
            return url(dosyaAdi).openStream();
        } catch (java.io.IOException exception) {
            throw new IllegalStateException("Kaynak okunamadı: " + dosyaAdi, exception);
        }
    }

    public static ImageIcon ikon(String dosyaAdi) {
        return new ImageIcon(url(dosyaAdi));
    }
}
