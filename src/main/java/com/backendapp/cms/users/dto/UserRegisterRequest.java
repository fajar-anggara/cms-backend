package com.backendapp.cms.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Reequest body yang diharuskan saat register user baru
 */

@Schema(name = "UserRegisterRequest", description = "Reequest body yang diharuskan saat register user baru")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-19T20:20:03.897328600+07:00[Asia/Jakarta]", comments = "Generator version: 7.8.0")
public class UserRegisterRequest {

  private String username;

  private String email;

  private String password;

  private String confirmPassword;

  public UserRegisterRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserRegisterRequest(String username, String email, String password, String confirmPassword) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.confirmPassword = confirmPassword;
  }

  public UserRegisterRequest username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Username untuk setiap user/blogger (unique) bisa dijadikan identifier saat login.
   * @return username
   */
  @NotNull @Pattern(regexp = "^[a-zA-Z0-9_-]+$") @Size(min = 1, max = 255) 
  @Schema(name = "username", example = "andriana", description = "Username untuk setiap user/blogger (unique) bisa dijadikan identifier saat login.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserRegisterRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Email untuk setiap user/blogger (unique) bisa dijadikan identifier saar login.
   * @return email
   */
  @NotNull @Size(min = 1, max = 255) @Email
  @Schema(name = "email", example = "andriana@gmail.com", description = "Email untuk setiap user/blogger (unique) bisa dijadikan identifier saar login.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserRegisterRequest password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Password user/blogger yang sudah di HASH.
   * @return password
   */
  @NotNull @Size(min = 8) 
  @Schema(name = "password", example = "xxxxxxxx", description = "Password user/blogger yang sudah di HASH.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserRegisterRequest confirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
    return this;
  }

  /**
   * Field ini harus sama dengan field password
   * @return confirmPassword
   */
  @NotNull @Size(min = 8) 
  @Schema(name = "confirmPassword", example = "xxxxxxxxx", description = "Field ini harus sama dengan field password", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("confirmPassword")
  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRegisterRequest userRegisterRequest = (UserRegisterRequest) o;
    return Objects.equals(this.username, userRegisterRequest.username) &&
        Objects.equals(this.email, userRegisterRequest.email) &&
        Objects.equals(this.password, userRegisterRequest.password) &&
        Objects.equals(this.confirmPassword, userRegisterRequest.confirmPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, email, password, confirmPassword);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRegisterRequest {\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append("*").append("\n");
    sb.append("    confirmPassword: ").append("*").append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

