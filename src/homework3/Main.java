package homework3;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java Main <PPM File>");
            return;
        }

        DctCompression dct = new DctCompression(args[0]);
        dct.resize();
    }
}
