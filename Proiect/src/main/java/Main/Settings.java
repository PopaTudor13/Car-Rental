package Main;

import java.io.FileReader;

import java.io.IOException;
import java.util.Properties;

public class Settings {
    private static Settings instance;
    private final String repoType;
    private final String repoType2;
    private final String repoFile;
    private final String repoFile2;

    private final String runtype;

    private Settings(String repoType, String repoFile, String repoType2, String repoFile2, String runtype) {
        this.repoType = repoType;
        this.repoFile = repoFile;
        this.repoType2 = repoType2;
        this.repoFile2 = repoFile2;
        this.runtype = runtype;
    }

    public String getRepoFile() {
        return repoFile;
    }

    public String getRepoType() {
        return repoType;
    }

    public String getRepoFile2() {
        return repoFile2;
    }

    public String getRepoType2() {
        return repoType2;
    }

    public String getRunType() {
        return runtype;
    }

    private static Properties loadSettings() {
        try (FileReader fr = new FileReader("settings")) {
            Properties settings = new Properties();
            settings.load(fr);
            return settings;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized Settings getInstance() {
        Properties properties = loadSettings();
        instance = new Settings(properties.getProperty("repo_type"), properties.getProperty("repo_file"), properties.getProperty("repo_type2"), properties.getProperty("repo_file2"),
                properties.getProperty("run_type"));
        return instance;
    }
}