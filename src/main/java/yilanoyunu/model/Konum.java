package yilanoyunu.model;

public record Konum(int x, int y) {
    public Konum ilerle(Yon yon) {
        return new Konum(x + yon.x(), y + yon.y());
    }
}
