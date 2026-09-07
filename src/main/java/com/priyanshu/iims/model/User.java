package com.priyanshu.iims.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.priyanshu.iims.model.enums.Role;

@Document(collection="users")
public class User {
	@Id
	private String id;
	private String email;
	private String password;
	private Role role;
	
	public User() {
		
	}
	public User(String email,String password, Role role) {
		this.email=email;
		this.password=password;
		this.role=role;
	}
	public String getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password=password;
	}
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role=role;
	}
}
