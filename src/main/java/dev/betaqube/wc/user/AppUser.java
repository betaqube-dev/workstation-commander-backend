package dev.betaqube.wc.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
@Table(name = "users")
public class AppUser implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String role;

	protected AppUser() {
	}

	public AppUser(String email, String name, String password, String role) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return email;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getRole() {
		return role;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AppUser appUser)) return false;
		return Objects.equals(id, appUser.id) && Objects.equals(email, appUser.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email);
	}

	@Override
	public String toString() {
		return "AppUser{" +
				"id=" + id +
				", email='" + email + '\'' +
				", name='" + name + '\'' +
				", role='" + role + '\'' +
				'}';
	}
}
