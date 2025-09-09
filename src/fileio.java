import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class fileio {
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public void demo() {
        File current = new File("./");

        if (current.exists()) {
            System.out.println("Object exists");
            System.out.println(current.getAbsolutePath());
        }

        if (current.isFile()) {
            System.out.println("Object is file");
        }

        if (current.isDirectory()) {
            System.out.println("Object is directory:");
            File[] content = current.listFiles();
            if (content != null) {
                for (File file : content) {
                    System.out.print(dateFormatter.format(
                            new Date(file.lastModified())
                    ) + " ");
                    if (file.isDirectory()) {
                        System.out.print("<DIR>");
                    } else {
                        System.out.print(file.length());
                    }
                    System.out.println(" " + file.getName());
                }
            }
        }
        File sub = new File("./subdir");
        if (sub.exists()) {
            sub.delete();
            System.out.println("Sub deleted");
        } else {
            sub.mkdir();
        }
        //-------------------------------------------------

        try (FileWriter fw = new FileWriter("test.txt"))
        {
            fw.write("Line 1\nLine 2\nThe line 3");
            fw.flush();

        } catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        try (
                FileReader fr = new FileReader("test.txt");
                Scanner scanner = new Scanner(fr)
        ) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("-------------config---------------");
        Map<String, String> config = new LinkedHashMap<>();
        try (InputStream inputStream = Objects.requireNonNull(
                this.getClass().getClassLoader().getResourceAsStream("db.ini"));
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(inputStream, java.nio.charset.StandardCharsets.UTF_8)))
        {
            String line;
            java.util.regex.Pattern commentTail = java.util.regex.Pattern.compile("[#;].*$");

            while ((line = br.readLine()) != null)
            {
                String cleaned = commentTail.matcher(line).replaceFirst("").trim();
                if (cleaned.isEmpty()) continue;

                int eq = cleaned.indexOf('=');
                if (eq < 0) continue;

                String key = cleaned.substring(0, eq).trim();
                String value = cleaned.substring(eq + 1).trim();
                if (!key.isEmpty())
                {
                    config.put(key, value);
                }
            }

        } catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        } catch (NullPointerException ex) {
            System.err.println("Resource not found");
        }

        for (Map.Entry<String, String> entry : config.entrySet()) {
            System.out.printf("\"%s\"     \"%s\"%n", entry.getKey(), entry.getValue());
        }
    }
}
