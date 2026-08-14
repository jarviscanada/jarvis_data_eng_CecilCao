package main.java.ca.jrvs.apps.practice;

import org.junit.Assert;
import org.junit.Test;

/**
 * This class should be created in a separate test folder, but we will be keeping it here for
 * unit test practice.
 */
public class RegexExcImpTest {

  @Test
  public void testMatchJpeg_whenOnlyKey() {
    RegexExcImp test = new RegexExcImp();
    // Test 1: Can check if .jpg is found.
    Assert.assertEquals(true, test.matchJpeg(".jpg"));

    // Test 2: Can check if .jpeg is found.
    Assert.assertEquals(true, test.matchJpeg(".jpeg"));
  }

  @Test
  public void testMatchJpeg_whenGeneralCase() {
    RegexExcImp test = new RegexExcImp();
    // Test 1: Can check within a proper file name
    Assert.assertEquals(true, test.matchJpeg("filename.jpg"));
    Assert.assertEquals(true, test.matchJpeg("filename.jpeg"));

    // Test 2: When given wrong file extension
    Assert.assertEquals(false, test.matchJpeg("filename.txt"));
  }

  @Test
  public void testMatchJpeg_whenUppercase() {
    RegexExcImp test = new RegexExcImp();

    // Test 1: The function should be case-insensitive.
    Assert.assertEquals(true, test.matchJpeg("filename.JPG"));
    Assert.assertEquals(true, test.matchJpeg("filename.JPEG"));
    Assert.assertEquals(true, test.matchJpeg("filename.JpG"));
    Assert.assertEquals(true, test.matchJpeg("filename.JPeg"));
  }

  @Test
  public void testMatchJpeg_whenWhitespace() {
    RegexExcImp test = new RegexExcImp();

    // Test 1: Trailing Whitespace
    Assert.assertEquals(true, test.matchJpeg("    trailing.jpeg"));
    Assert.assertEquals(true, test.matchJpeg("    trailing.jpg"));
    Assert.assertEquals(false, test.matchJpeg("trailing.jpg     "));
    Assert.assertEquals(false, test.matchJpeg("trailing.jpeg     "));

    // Test 4: Extension is not in the middle
    Assert.assertEquals(false, test.matchJpeg("asdf.jpeg.fake"));
    Assert.assertEquals(false, test.matchJpeg("abc.jpg.txt"));
  }

  @Test
  public void testMatchIp() {
    RegexExcImp test = new RegexExcImp();

    // Test 1: Valid IP address
    Assert.assertEquals(true, test.matchIp("0.0.0.0"));
    Assert.assertEquals(true, test.matchIp("999.999.999.999"));
    Assert.assertEquals(true, test.matchIp("123.456.789.10"));

    // Test 2: Invalid lengths
    Assert.assertEquals(false, test.matchIp("0.0.0.0.0"));
    Assert.assertEquals(false, test.matchIp("0"));

    // Test 3: No trailing characters or whitespace
    Assert.assertEquals(false, test.matchIp("123.456.789.012."));
    Assert.assertEquals(false, test.matchIp("0.0.0.0    "));
    Assert.assertEquals(false, test.matchIp("    1.2.3.4"));
    Assert.assertEquals(false, test.matchIp(".0.0.0.0"));

    // Test 5: Incorrect IP addresses
    Assert.assertEquals(false, test.matchIp(""));
    Assert.assertEquals(false, test.matchIp("."));
    Assert.assertEquals(false, test.matchIp("...."));
    Assert.assertEquals(false, test.matchIp("1234.1234.1234.1234"));
    Assert.assertEquals(false, test.matchIp("1. .3.4"));
  }

  @Test
  public void testIsEmptyLine() {
    RegexExcImp test = new RegexExcImp();

    // Test 1: General case
    Assert.assertEquals(false, test.isEmptyLine("a"));
    Assert.assertEquals(false, test.isEmptyLine("1"));
    Assert.assertEquals(true, test.isEmptyLine(" "));
    Assert.assertEquals(true, test.isEmptyLine(""));
    Assert.assertEquals(true, test.isEmptyLine("\n"));
    Assert.assertEquals(true, test.isEmptyLine("\t"));

    // Test 2: Trailing
    Assert.assertEquals(false, test.isEmptyLine("\nabc"));
    Assert.assertEquals(false, test.isEmptyLine("    abc"));
    Assert.assertEquals(false, test.isEmptyLine("avc      "));
    Assert.assertEquals(true, test.isEmptyLine("\n      \n"));
  }
}
