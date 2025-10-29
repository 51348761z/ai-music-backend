package org.demo.aimusic.module.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaResponse {

  /**
   * UUID of the captcha
   */
  private String uuid;

  /**
   * Base64 encoded image of the captcha
   */
  private String imgBase64;

}
