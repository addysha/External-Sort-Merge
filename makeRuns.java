import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class makeRuns {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java makeRuns <run_size> <input_file>");
            return;
        }

        int runSize = Integer.parseInt(args[0]);
        String inputFile = args[1];

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                lines.add(line);

                if (lines.size() == runSize) {
                    Collections.sort(lines);
                    for (String sortedLine : lines) {
                        System.out.println(sortedLine);
                    }
                    lines.clear();
                }
            }

            if (!lines.isEmpty()) {
                Collections.sort(lines);
                for (String sortedLine : lines) {
                    System.out.println(sortedLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}