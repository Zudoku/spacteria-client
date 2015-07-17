package fingerprint.inout;

public class FileTester {
    public static void main(String[] args) {
        TileFileHandler s = new TileFileHandler();
        s.init("123123");
        s.writeTestMap();
    }

}
