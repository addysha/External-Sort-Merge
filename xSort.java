import java.io.*;
import java.util.*;

public class xSort {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java xSort <run_size> <input_file> <k>");
            return;
        }

        int runSize = Integer.parseInt(args[0]);
        String inputFile = args[1];
        int k = Integer.parseInt(args[2]);

        try {
            mergeRuns(runSize, inputFile, k);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mergeRuns(int runSize, String inputFile, int k) throws IOException {
        List<String> tempFiles = distributeRuns(runSize, inputFile, k);
        while (tempFiles.size() > 1) {
            List<String> mergedFiles = new ArrayList<>();
            for (int i = 0; i < tempFiles.size(); i += k) {
                List<String> filesToMerge = tempFiles.subList(i, Math.min(i + k, tempFiles.size()));
                String mergedFile = mergeFiles(filesToMerge);
                mergedFiles.add(mergedFile);
            }
            tempFiles = mergedFiles;
        }

        // Print the final sorted data to standard output
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFiles.get(0)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    private static List<String> distributeRuns(int runSize, String inputFile, int k) throws IOException {
        List<String> tempFiles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int fileIndex = 0;
            PrintWriter writer = new PrintWriter(new File("temp_" + fileIndex + ".txt"));
            tempFiles.add("temp_" + fileIndex + ".txt");
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                writer.println(line);
                lineCount++;
                if (lineCount == runSize) {
                    writer.close();
                    fileIndex++;
                    writer = new PrintWriter(new File("temp_" + fileIndex + ".txt"));
                    tempFiles.add("temp_" + fileIndex + ".txt");
                    lineCount = 0;
                }
            }
        }
        return tempFiles;
    }

    private static String mergeFiles(List<String> filesToMerge) throws IOException {
        String mergedFileName = UUID.randomUUID().toString() + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(mergedFileName))) {
            List<BufferedReader> readers = new ArrayList<>();
            for (String file : filesToMerge) {
                readers.add(new BufferedReader(new FileReader(file)));
            }
            PriorityQueue<QueueNode> minHeap = new PriorityQueue<>(Comparator.comparing(QueueNode::getValue));
            for (BufferedReader reader : readers) {
                String line = reader.readLine();
                if (line != null) {
                    minHeap.offer(new QueueNode(line, reader));
                }
            }
            while (!minHeap.isEmpty()) {
                QueueNode node = minHeap.poll();
                writer.println(node.getValue());
                String nextLine = node.getReader().readLine();
                if (nextLine != null) {
                    minHeap.offer(new QueueNode(nextLine, node.getReader()));
                }
            }
        }
        for (String file : filesToMerge) {
            new File(file).delete();
        }
        return mergedFileName;
    }

    private static class QueueNode {
        private String value;
        private BufferedReader reader;

        public QueueNode(String value, BufferedReader reader) {
            this.value = value;
            this.reader = reader;
        }

        public String getValue() {
            return value;
        }

        public BufferedReader getReader() {
            return reader;
        }
    }
}
