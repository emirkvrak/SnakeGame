package yilanoyunu.model;

public record OyunAyarlari(int sutun, int satir, int hucreBoyutu, int timerMillis) {
    public static OyunAyarlari varsayilan() {
        return new OyunAyarlari(25, 24, 24, 110);
    }
}
