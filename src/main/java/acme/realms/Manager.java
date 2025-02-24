/*
 *
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Transient;

import acme.client.components.basis.AbstractRole;
import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.datatypes.UserIdentity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Manager extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "{validation.realms.manager.identifierNumber}")
	@Mandatory

	private String				identifierNumber;

	@Mandatory
	@ValidNumber(min = 0, max = 100, integer = 3, fraction = 0)
	private Integer				yearsOfExperience;

	@Mandatory
	@Temporal(TemporalType.DATE)
	private Date				dateOfBirth;

	@ValidUrl
	@Optional
	private String				picture;
	// Derived attributes -----------------------------------------------------


	@Transient
	public String getInitials() {
		DefaultUserIdentity identity = this.getUserAccount().getIdentity();
		String fullName = identity.getFullName();

		String[] parts = fullName.split(", ");
		String[] nameParts = parts[1].split(" ");

		StringBuilder initials = new StringBuilder();
		initials.append(parts[0].charAt(0));

		for (int i = 0; i < Math.min(nameParts.length, 2); i++)
			initials.append(nameParts[i].charAt(0));

		return initials.toString().toUpperCase();
	}

	@Transient
	public boolean isIdentifierValid() {
		if (this.identifierNumber == null || !this.identifierNumber.matches("^[A-Z]{2,3}\\d{6}$"))
			return false;

		String expectedInitials = this.getInitials();
		String actualInitials = this.identifierNumber.substring(0, expectedInitials.length());

		return actualInitials.equals(expectedInitials);
	}

	// Relationships ----------------------------------------------------------


	@Embedded
	private UserIdentity userIdentity;

}
