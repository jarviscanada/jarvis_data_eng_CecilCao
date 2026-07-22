package main.java.ca.jrvs.apps.practice;

import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

  @Override
  public boolean matchJpeg(String filename) {
    return Pattern.matches(".*(?i)\\.jpe?g$", filename);
  }

  @Override
  public boolean matchIp(String ipAddr) {
    return Pattern.matches("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$", ipAddr);
  }

  @Override
  public boolean isEmptyLine(String line) {
    return Pattern.matches("^\\s*$", line);
  }
}
