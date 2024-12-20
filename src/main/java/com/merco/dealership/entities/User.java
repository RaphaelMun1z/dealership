package com.merco.dealership.entities;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.merco.dealership.entities.enums.UserRole;
import com.merco.dealership.entities.validation.constraints.PhoneNumber;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_users")
public abstract class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@NotNull(message = "Required field")
	@Pattern(regexp = "^[A-Z]+(.)*")
	private String name;

	@PhoneNumber(message = "Invalid field value")
	@Column(unique = true)
	private String phone;

	@NotNull(message = "Required field")
	@Email(message = "Invalid field value")
	@Column(unique = true)
	private String email;

	@NotNull(message = "Required field")
	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	private boolean isAccountNonExpired;

	private boolean isAccountNonLocked;

	private boolean isCredentialsNonExpired;

	private boolean isEnabled;

	protected User() {
		super();
	}

	public User(String id, @NotNull(message = "Required field") @Pattern(regexp = "^[A-Z]+(.)*") String name,
			String phone, @NotNull(message = "Required field") @Email(message = "Invalid field value") String email,
			@NotNull(message = "Required field") String password, UserRole role) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.password = password;
		this.role = role;
		this.isAccountNonExpired = true;
		this.isAccountNonLocked = true;
		this.isCredentialsNonExpired = true;
		this.isEnabled = true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role.getRole();
	}

	public void setRole(UserRole role) {
		if (role == null) {
			throw new IllegalStateException("Role cannot be null");
		}

		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (role == UserRole.ADM) {
			return List.of(new SimpleGrantedAuthority("ROLE_ADM"), new SimpleGrantedAuthority("ROLE_SELLER"));
		} else if (role == UserRole.SELLER) {
			return List.of(new SimpleGrantedAuthority("ROLE_SELLER"));
		} else {
			throw new IllegalArgumentException("Unexpected value: " + this.role);
		}
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}
}
