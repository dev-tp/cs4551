package homework2;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

class LZW {

    private ArrayList<Integer> result;
    private HashMap<Integer, String> inverseLookupTable;
    private String data;

    LZW(String pathToFile) throws IOException {
        inverseLookupTable = new HashMap<>();
        result = new ArrayList<>();

        readFile(pathToFile);
    }

    void compressionRatio() {
        System.out.printf("Compression ratio: %.3f\n", (double) data.length() / result.size());
    }

    void decode() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Integer entry : result) {
            stringBuilder.append(inverseLookupTable.get(entry));
        }

        System.out.printf("Decoded message: %s\n", stringBuilder.toString());
    }

    void encode() {
        HashMap<String, Integer> lookupTable = new HashMap<>();

        int index = 0;

        for (int i = 0; i < data.length(); i++) {
            String character = "" + data.charAt(i);

            if (!lookupTable.containsKey(character)) {
                lookupTable.put(character, index);
                inverseLookupTable.put(index++, character);
            }
        }

        String p = "";

        for (int i = 0; i < data.length(); i++) {
            String c = "" + data.charAt(i);
            String pc = p + c;

            if (lookupTable.containsKey(pc)) {
                p = pc;
            } else {
                result.add(lookupTable.get(p));
                lookupTable.put(pc, index);
                inverseLookupTable.put(index++, pc);
                p = c;
            }
        }

        System.out.print("\nEncoded message: ");

        for (Integer entry : result) {
            System.out.printf("%d ", entry);
        }

        System.out.println();
    }

    private void readFile(String pathToFile) throws IOException {
        Path path = Paths.get(pathToFile);

        if (Files.exists(path)) {
            BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            data = stringBuilder.toString();
        } else {
            throw new IOException("Path does not exist");
        }
    }
}
