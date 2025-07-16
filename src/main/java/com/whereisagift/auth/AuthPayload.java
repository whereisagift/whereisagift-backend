package com.whereisagift.auth;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class AuthPayload {
  private Integer id;
  private String first_name;
  @Nullable private String last_name;
  @Nullable private String photo_url;
  private Integer auth_date;
  private String hash;
  private String username;

  public Integer getTelegramId() {
    return id;
  }

  public Integer getAuthDate() {
    return auth_date;
  }

  public String getFirstName() {
    return first_name;
  }

  public String getLastName() {
    return last_name;
  }

  public String getPhotoUrl() {
    return photo_url;
  }
}
