package yilanoyunu.ui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import yilanoyunu.Kaynaklar;

/** Oyunun arka plan müziğini yönetir; ses bulunamazsa oyun yine çalışır. */
public final class SesOynatici implements AutoCloseable {
    private Clip clip;

    public void baslat(String dosyaAdi) {
        try {
            AudioInputStream akis = AudioSystem.getAudioInputStream(Kaynaklar.akis(dosyaAdi));
            clip = AudioSystem.getClip();
            clip.open(akis);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception exception) {
            System.err.println("Ses başlatılamadı: " + exception.getMessage());
        }
    }

    @Override
    public void close() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
