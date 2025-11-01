package org.demo.aimusic.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.demo.aimusic.config.properties.MinioProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {
  private final MinioProperties minioProperties;

  /**
   * Creates and configures a MinioClient bean using properties defined in MinioProperties.
   *
   * @return Configured MinioClient instance.
   */
  public MinioClient minioClient() {
    return MinioClient.builder()
        .endpoint(minioProperties.getEndpoint())
        .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
        .build();
  }

}
