package yilanoyunu.ui;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import yilanoyunu.model.Yon;

/** Klavye kısayollarını oyun panelinden bağımsız yönetir. */
public final class KlavyeKontrolcusu {
    private final JComponent hedef;
    private final Consumer<Yon> yonDegistirici;
    private final Runnable duraklatma;

    public KlavyeKontrolcusu(JComponent hedef, Consumer<Yon> yonDegistirici, Runnable duraklatma) {
        this.hedef = hedef;
        this.yonDegistirici = yonDegistirici;
        this.duraklatma = duraklatma;
        kur();
    }

    private void kur() {
        yonAta("SOL", Yon.SOL, "pressed LEFT");
        yonAta("SAG", Yon.SAG, "pressed RIGHT");
        yonAta("YUKARI", Yon.YUKARI, "pressed UP");
        yonAta("ASAGI", Yon.ASAGI, "pressed DOWN");
        yonAta("SOL_A", Yon.SOL, "pressed A");
        yonAta("SAG_D", Yon.SAG, "pressed D");
        yonAta("YUKARI_W", Yon.YUKARI, "pressed W");
        yonAta("ASAGI_S", Yon.ASAGI, "pressed S");
        hedef.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("pressed P"), "DURAKLAT");
        hedef.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("pressed ESCAPE"), "DURAKLAT");
        hedef.getActionMap().put("DURAKLAT", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent event) { duraklatma.run(); }
        });
    }

    private void yonAta(String ad, Yon yon, String tus) {
        hedef.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(tus), ad);
        hedef.getActionMap().put(ad, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent event) { yonDegistirici.accept(yon); }
        });
    }
}
