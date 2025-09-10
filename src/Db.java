import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Db {

    private Map<String, String> loadConfig(String iniFileName) throws Exception {
        Map<String, String> cfg = new LinkedHashMap<>();
        try (InputStream is = Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream(iniFileName),
                "Resource not found: " + iniFileName
        );
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String cleaned = line.replaceAll("[#;].*$", "").trim();
                if (cleaned.isEmpty()) continue;
                int eq = cleaned.indexOf('=');
                if (eq < 0) continue;
                String key = cleaned.substring(0, eq).trim();
                String val = cleaned.substring(eq + 1).trim();
                if (val.length() >= 2 &&
                        ((val.startsWith("\"") && val.endsWith("\"")) ||
                                (val.startsWith("'") && val.endsWith("'")))) {
                    val = val.substring(1, val.length() - 1);
                }
                if (!key.isEmpty()) cfg.put(key, val);
            }
        } catch (NullPointerException e) {
            throw new Exception("Resource not found: " + iniFileName);
        } catch (IOException e) {
            throw new Exception("IO error: " + e.getMessage(), e);
        }
        return cfg;
    }

    private Map<String, String> loadAllConfigs() throws Exception {
        Map<String, String> all = new LinkedHashMap<>();
        ClassLoader cl = getClass().getClassLoader();
        URL root = cl.getResource("");

        if (root != null && "file".equalsIgnoreCase(root.getProtocol())) {
            File dir = new File(root.toURI());
            File[] iniFiles = dir.listFiles((d, name) -> name.toLowerCase(Locale.ROOT).endsWith(".ini"));
            if (iniFiles != null && iniFiles.length > 0) {
                Arrays.sort(iniFiles, Comparator.comparing(File::getName));
                for (File f : iniFiles) {
                    all.putAll(loadConfig(f.getName()));
                }
                return all;
            }
        }

        for (String name : List.of( "dbauth.ini", "db.ini")) {
            try {
                all.putAll(loadConfig(name));
            } catch (Exception ignore) {
            }
        }
        if (all.isEmpty()) throw new Exception("No .ini found in resources");
        return all;
    }

    private String buildJdbcUrl(Map<String, String> c) {
        String protocol = Objects.requireNonNull(c.get("protocol"), "ini miss protocol");
        String dbms = Objects.requireNonNull(c.get("dbms"), "ini miss dbms");
        String host = Objects.requireNonNull(c.get("host"), "ini miss host");
        String port = Objects.requireNonNull(c.get("port"), "ini miss port");
        String scheme = Objects.requireNonNull(c.get("scheme"), "ini miss scheme");
        String user = Objects.requireNonNull(c.get("user"), "ini miss user");
        String password = Objects.requireNonNull(c.get("password"), "ini miss password");
        String params = Objects.requireNonNull(c.get("params"), "ini miss params");

        return String.format("%s:%s://%s:%s/%s?user=%s&password=%s&%s",
                protocol, dbms, host, port, scheme, user, password, params);
    }

    public void demo() {
        Driver mysqlDriver = null;
        Connection connection = null;
        String connectionString;

        try {
            Map<String, String> config = loadAllConfigs();
            connectionString = buildJdbcUrl(config);
            System.out.println(connectionString);
        } catch (Exception ex) {
            System.err.println("Config error: " + ex.getMessage());
            return;
        }

        try {
            mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            connection = DriverManager.getConnection(connectionString);
            System.out.println("Connection Ok");
        } catch (SQLException ex) {
            System.err.println("Start error: " + ex.getMessage());
            return;
        } finally {
            if (connection != null) try {
                connection.close();
            } catch (SQLException ignored) {
            }
            if (mysqlDriver != null) try {
                DriverManager.deregisterDriver(mysqlDriver);
            } catch (SQLException ignored) {
            }
        }
    }
}
