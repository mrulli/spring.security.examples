package spring.jwt.example;

/**
 * Simple class that contains credentials data
 */

public class AccountCredentials {

	private String username;
	private String password;
	
	
	public AccountCredentials() {
	}

	public AccountCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
	}
	
}
