package main.java.ca.jrvs.apps.practice;

public interface RegexExc {

  /**
   * Returns true if a filename's extension ends with .jpg or .jpeg
   * (This function should be case-insensitive)
   * @param filename
   * @return
   */
  public boolean matchJpeg(String filename);

  /**
   * Checks and returns true if the IP Address is valid.
   * It should check for IPv4 ranges 0.0.0.0 to 999.999.999.999
   * @param ipAddr
   * @return
   */
  public boolean matchIp(String ipAddr);

  /**
   * Checks if a given string is empty (e.g. whitespace, tabs, etc.)
   * @param line
   * @return
   */
  public boolean isEmptyLine(String line);
}
