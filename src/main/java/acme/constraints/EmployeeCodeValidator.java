
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.principals.UserAccount;
import acme.realms.AssistanceAgents;

public class EmployeeCodeValidator implements ConstraintValidator<ValidEmployeeCode, String> {

	private AssistanceAgents agent;


	@Override
	public boolean isValid(final String employeeCode, final ConstraintValidatorContext context) {
		if (this.agent == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Agent is null").addConstraintViolation();
			return false;
		}

		// Obtener el UserAccount
		UserAccount userAccount = this.agent.getUserAccount();
		if (userAccount == null || userAccount.getIdentity() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("User account or identity is missing").addConstraintViolation();
			return false;
		}

		// Obtener la identidad del usuario
		DefaultUserIdentity identity = userAccount.getIdentity();
		String initials = this.getInitials(identity.getName(), identity.getSurname());

		// Verificar que el código de empleado no sea nulo o vacío
		if (employeeCode == null || employeeCode.isBlank()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Employee code cannot be empty").addConstraintViolation();
			return false;
		}

		// Verificar el patrón del código de empleado (XX000000 o XXX000000)
		boolean isValidPattern = employeeCode.matches("^[A-Z]{2,3}\\d{6}$");
		if (!isValidPattern) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Invalid employee code format").addConstraintViolation();
			return false;
		}

		// Comparar iniciales con los primeros caracteres del código de empleado
		String codeInitials = employeeCode.substring(0, initials.length());
		if (!initials.equals(codeInitials)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Employee code must start with the initials of the agent").addConstraintViolation();
			return false;
		}

		return true;
	}

	private String getInitials(final String name, final String surname) {
		String firstInitial = name != null && !name.isBlank() ? String.valueOf(name.charAt(0)) : "";
		String lastInitial = surname != null && !surname.isBlank() ? String.valueOf(surname.charAt(0)) : "";

		return (firstInitial + lastInitial).toUpperCase();
	}
}
