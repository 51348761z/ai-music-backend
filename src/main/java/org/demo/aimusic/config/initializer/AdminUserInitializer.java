package org.demo.aimusic.config.initializer;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.aimusic.config.properties.InitialAdminProperties;
import org.demo.aimusic.module.user.entity.User;
import org.demo.aimusic.module.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final InitialAdminProperties initialAdminProperties;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    log.info("Checking for existing admin user...");
    boolean adminExists = userService.lambdaQuery().eq(User::getRole, "admin").exists();
    if (adminExists) {
      log.info("Admin user already exists. Skipping initialization.");
      return;
    }
    log.info("No admin user found. Creating initial admin user...");
    if (initialAdminProperties.getEmail() == null || initialAdminProperties.getPassword() == null) {
      log.error("Initial admin email or password is not set. Cannot create admin user.");
      //      throw new IllegalStateException("Initial admin email or password is not set.");
      return;
    }

    try {
      User initialAdmin = new User();
      initialAdmin.setUuid(UUID.randomUUID().toString());
      initialAdmin.setUserIdStr(generateUniqueUserIdStr());
      initialAdmin.setEmail(initialAdminProperties.getEmail());
      initialAdmin.setPasswordHash(passwordEncoder.encode(initialAdminProperties.getPassword()));
      initialAdmin.setNickname(initialAdminProperties.getNickname());
      initialAdmin.setRole("admin");
      initialAdmin.setStatus("enabled");

      boolean saved = userService.save(initialAdmin);
      if (saved) {
        log.info("Initial admin user created successfully with email: {}", initialAdmin.getEmail());
      } else {
        log.error("Failed to save the initial admin user to the database.");
      }
    } catch (Exception e) {
      log.error("Failed to create initial admin user: {}", e.getMessage(), e);
    }
  }

  private String generateUniqueUserIdStr() {
    long timestamp = System.currentTimeMillis();
    int randomNum = ThreadLocalRandom.current().nextInt(1000, 10000);
    return "user_" + timestamp + "_" + randomNum;
  }
}
