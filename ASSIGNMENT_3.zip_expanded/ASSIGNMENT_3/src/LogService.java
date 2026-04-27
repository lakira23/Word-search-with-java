import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LogService {

    public File writeLog(List<ShapeResult> history) throws IOException {
        File file = new File("draw_shape_log.txt");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("=== Draw Shape Log ===\n\n");

            writer.write("Shapes drawn in order:\n");
            for (ShapeResult result : history) {
                if ("Triangle".equals(result.getType())) {
                    writer.write(result.getType() + ": " + result.getSizeText()
                            + " (angles: " + result.getAngleText()
                            + "; time: " + result.getTimeMs() + " ms)\n");
                } else {
                    writer.write(result.getType() + ": " + result.getSizeText()
                            + " (time: " + result.getTimeMs() + " ms)\n");
                }
            }

            if (!history.isEmpty()) {
                ShapeResult largest = findLargest(history);
                writer.write("\nLargest shape by area:\n");
                writer.write(largest.getType() + ": " + largest.getSizeText() + "\n");

                String mostFrequent = findMostFrequent(history);
                int count = countType(history, mostFrequent);
                writer.write("\nMost frequent shape:\n");
                writer.write(mostFrequent + ": " + count + " times\n");

                long avg = averageTime(history);
                writer.write("\nAverage time:\n");
                writer.write(avg + " ms\n");
            }
        }

        return file;
    }

    private ShapeResult findLargest(List<ShapeResult> history) {
        ShapeResult largest = history.get(0);
        for (ShapeResult result : history) {
            if (result.getArea() > largest.getArea()) {
                largest = result;
            }
        }
        return largest;
    }

    private String findMostFrequent(List<ShapeResult> history) {
        int squares = countType(history, "Square");
        int triangles = countType(history, "Triangle");
        return squares >= triangles ? "Square" : "Triangle";
    }

    private int countType(List<ShapeResult> history, String type) {
        int count = 0;
        for (ShapeResult result : history) {
            if (type.equals(result.getType())) {
                count++;
            }
        }
        return count;
    }

    private long averageTime(List<ShapeResult> history) {
        long total = 0;
        for (ShapeResult result : history) {
            total += result.getTimeMs();
        }
        return total / history.size();
    }
}