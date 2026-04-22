package com.incapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

//Equivalent to @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode. 
@Data 

@Entity
public class MyUser {
	
  @Id
  private String username;
  private String name;
  private String password;
  private boolean enabled=true;
  private String role="ROLE_USER";
  private String phone;
  @Column(nullable = true ,columnDefinition = "longblob")
  private byte[] photo;
  public String getUsername() {
	return username;
  }
  public void setUsername(String username) {
	this.username = username;
  }
  public String getName() {
	return name;
  }
  public void setName(String name) {
	this.name = name;
  }
  public String getPassword() {
	return password;
  }
  public void setPassword(String password) {
	this.password = password;
  }
  public boolean isEnabled() {
	return enabled;
  }
  public void setEnabled(boolean enabled) {
	this.enabled = enabled;
  }
  public String getRole() {
	return role;
  }
  public void setRole(String role) {
	this.role = role;
  }
  public String getPhone() {
	return phone;
  }
  public void setPhone(String phone) {
	this.phone = phone;
  }
  public byte[] getPhoto() {
	return photo;
  }
  public void setPhoto(byte[] photo) {
	this.photo = photo;
  }
  
}
