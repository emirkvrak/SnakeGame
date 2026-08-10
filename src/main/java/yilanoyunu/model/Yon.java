package yilanoyunu.model;

public enum Yon {
    SOL(-1, 0), SAG(1, 0), YUKARI(0, -1), ASAGI(0, 1);

    private final int x;
    private final int y;

    Yon(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() { return x; }
    public int y() { return y; }

    public boolean ters(Yon diger) {
        return x + diger.x == 0 && y + diger.y == 0;
    }
}
