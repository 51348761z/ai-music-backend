package org.demo.aimusic.common.util;

import java.util.concurrent.ThreadLocalRandom;

public final
class GeneratorUtil { // final class as it only contains static methods and should not be extended
  private GeneratorUtil() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static String generatedUniqueUserIdStr() {
    long timestamp = System.currentTimeMillis();
    int randomNum = ThreadLocalRandom.current().nextInt(1000, 10000);
    return "user_" + timestamp + randomNum;
  }
}
