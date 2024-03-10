package util;

import controller.FileController;
import controller.UserController;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import minispring.util.RequestMappingAnnotationToRegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlToRegexUtilTest {
  private static final Logger logger = LoggerFactory.getLogger(UrlToRegexUtilTest.class);

  @Test
  public void regexMatchTest() {
    try {
      // user/create\?(userId=?[\w-;,.\?%@&=]+)&(password=?[\w-;,.\?%@&=]+)&(name=?[\w-;,.\?%@&=]+)&(email=?[\w-;,.\?%@&=]+)
      // OR 로 처리해서, 순서 상관없이 뽑아낼 수 있도록.
      String regex = RequestMappingAnnotationToRegexUtil.convertReqeustMappingToRegexXXX(UserController.class.getMethod(
              "createUser", // method name
              String.class, // param1
              String.class, // param2
              String.class, // param3
              String.class  // param4
      ));
      logger.debug(regex);
      String testUrl = "/user/create?userId=&password=&name=&email=foo@gmail.com";

      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(testUrl);

      assertThat(matcher.matches()).isTrue();
      assertThat(matcher.groupCount()).isEqualTo(4);

      for (int i=1; i<=matcher.groupCount(); ++i) {
        logger.debug(matcher.group(i));
      }
    } catch (Exception e) {
      logger.debug(e.getMessage());
    }
  }

  @Test
  public void regexMatchTest2() {
    try {
      String regex = RequestMappingAnnotationToRegexUtil.convertReqeustMappingToRegexXXX (
              FileController.class.getMethod("getStaticFile", String.class)
      );
      logger.debug(regex);
      String testUrl = "/webapp/index.html";

      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(testUrl);

      assertThat(matcher.matches()).isTrue();

      for (int i=1; i<=matcher.groupCount(); ++i) {
        logger.debug(matcher.group(i));
      }
    } catch (Exception e) {
      logger.debug(e.getMessage());
    }
  }
}
