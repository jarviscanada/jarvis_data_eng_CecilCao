package main.java.ca.jrvs.apps.grep_app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep {
  final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

  private String outFile;
  private String regex;
  private String rootPath;

  @Override
  public void process() throws IOException {
    try {
      List<File> files = listFiles(getRootPath());
      List<String> grepLines = new ArrayList<>();
      for (File file : files) {
        readLines(file).stream().filter(this::containsPattern).forEach(grepLines::add);
      }
      writeToFile(grepLines);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> fileList = new ArrayList<>();
    File rootFile = new File(rootDir);

    // Recursively add the files to the list
    Arrays.stream(Objects.requireNonNull(rootFile.listFiles())).forEach(file -> {
      if (file.isDirectory()) {
        fileList.addAll(listFiles(file.getAbsolutePath()));
      } else {
        fileList.add(file);
        logger.debug("Added file: {}", file.getName());
      }
    });

    return fileList;
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> lines = new ArrayList<>();
    try (Scanner sc = new Scanner(inputFile)) {
      while (sc.hasNext()) {
        lines.add(sc.nextLine());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return lines;
  }

  @Override
  public boolean containsPattern(String line) {
    return Pattern.matches(getRegex(), line);
  }

  @Override
  public void writeToFile(List<String> lines) {
    try {
      File newFile = new File(getOutFile());
      if (!newFile.createNewFile()) {
        logger.debug("[JavaGrepImp] Did not create new file {}", getOutFile());
      }

      FileWriter fw = new FileWriter(getOutFile());
      for (String line : lines) {
        fw.write(line);
      }
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

    JavaGrepImp javaGrepImp = new JavaGrepImp();
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
