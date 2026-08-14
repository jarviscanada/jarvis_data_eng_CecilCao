package main.java.ca.jrvs.apps.grep_app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepStreamImp implements JavaGrepStream {
  final Logger logger = LoggerFactory.getLogger(JavaGrepStreamImp.class);

  private String outFile;
  private String regex;
  private String rootPath;

  @Override
  public void process() throws IOException {
    try {
      Stream<File> files = listFiles(this.getRootPath());

      // Filter and find the grep lines for each file
      Stream<String> grepLines = Stream.empty();
      for (File file : files.toList()) {
        Stream<String> results = readLines(file).filter(this::containsPattern);
        grepLines = Stream.concat(grepLines, results);
      }

      writeToFile(grepLines);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Stream<File> listFiles(String rootDir) {
    try (Stream<Path> fileList = Files.walk(Path.of(rootDir))) {
      return fileList.filter(Files::isRegularFile).map(Path::toFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return Stream.empty();
  }

  @Override
  public Stream<String> readLines(File inputFile) {
    Stream<String> lines = Stream.empty();
    try (FileReader fr = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(fr)) {
      lines = br.lines();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return lines;
  }

  @Override
  public boolean containsPattern(String line) {
    return Pattern.matches(this.getRegex(), line);
  }

  @Override
  public void writeToFile(Stream<String> lines) {
    try {
      File newFile = new File(this.getOutFile());
      if (!newFile.createNewFile()) {
        logger.debug("[JavaGrepImp] Did not create new file {}", this.getOutFile());
      }

      // Write the file
      FileWriter fw = new FileWriter(this.getOutFile());
      lines.forEach(line -> {
        try {
          fw.write(line);
        } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      });

      fw.close();
      logger.debug("[JavaGrepImp] Finished writing to file.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /* Setter and Getter Methods */
  public String getRootPath() {
    return rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  public String getRegex() {
    return regex;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }

  public String getOutFile() {
    return outFile;
  }

  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: Invalid number of arguments. Expected 3 but got " + args.length);
    }

    JavaGrepStreamImp javaGrepImp = new JavaGrepStreamImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
