package main.java.ca.jrvs.apps.grep_app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {

  /**
   * The top-level search workflow for Grep
   * @throws IOException if the process is interrupted or fails during the IO process
   */
  void process() throws IOException;

  /**
   * Lists the filenames in a given directory and all its
   * subdirectories.
   * @param rootDir the root directory
   * @return a list of Files from the root directory and its
   * subdirectories
   */
  List<File> listFiles(String rootDir);

  /**
   * Reads the lines of a File and converts it into a String List
   * @param inputFile the File to be read
   * @return lines of the given inputFile
   * @throws IllegalArgumentException if given inputFile is not a File
   */
  List<String> readLines(File inputFile) throws FileNotFoundException;

  /**
   * Checks whether a given line has a regex pattern dictated within the class.
   * @param line the String to check the regex pattern on
   * @return True if the pattern exists. False otherwise.
   */
  boolean containsPattern(String line);

  /**
   * Write a String List to a file with a name from the class' outFile.
   * @param lines the List of strings to write
   * @throws IOException if the IO process is interrupted or fails
   */
  void writeToFile(List<String> lines) throws IOException;

  /* Getters and Setter Methods */
  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outFile);
}
