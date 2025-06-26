package com.backendapp.cms.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

/**
 * Request body saat user akan login
 */

@Data
@Builder
@Schema(name = "UserLoginRequest", description = "Request body saat user akan login")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-06-24T13:54:40.417245300+07:00[Asia/Jakarta]", comments = "Generator version: 7.8.0")
public class UserLoginRequest {

  private String identifier;

  private String password;

  private Boolean rememberMe = false;

  public UserLoginRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserLoginRequest(String identifier, String password, Boolean rememberMe) {
    this.identifier = identifier;
    this.password = password;
    this.rememberMe = rememberMe;
  }

  public UserLoginRequest identifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

  /**
   * Identifier bisa menggunakan username atau email untuk login.
   * @return identifier
   */
  @NotNull @Size(min = 1, max = 255) 
  @Schema(name = "identifier", example = "andriana@gmail.com", description = "Identifier bisa menggunakan username atau email untuk login.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("identifier")
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public UserLoginRequest password(String password) {
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

  public UserLoginRequest rememberMe(Boolean rememberMe) {
    this.rememberMe = rememberMe;
    return this;
  }

  /**
   * Apakah user ingin diingat atau login setiap sesi
   * @return rememberMe
   */
  @NotNull 
  @Schema(name = "rememberMe", example = "true", description = "Apakah user ingin diingat atau login setiap sesi", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rememberMe")
  public Boolean getRememberMe() {
    return rememberMe;
  }

  public void setRememberMe(Boolean rememberMe) {
    this.rememberMe = rememberMe;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserLoginRequest userLoginRequest = (UserLoginRequest) o;
    return Objects.equals(this.identifier, userLoginRequest.identifier) &&
        Objects.equals(this.password, userLoginRequest.password) &&
        Objects.equals(this.rememberMe, userLoginRequest.rememberMe);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier, password, rememberMe);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserLoginRequest {\n");
    sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
    sb.append("    password: ").append("*").append("\n");
    sb.append("    rememberMe: ").append(toIndentedString(rememberMe)).append("\n");
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

