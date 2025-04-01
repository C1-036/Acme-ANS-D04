
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.features.assistanceagents.AssistanceAgentRepository;
import acme.realms.AssistanceAgent;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Autowired
	private AssistanceAgentRepository repository;


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgents, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (assistanceAgents == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			DefaultUserIdentity identity = assistanceAgents.getIdentity();

			if (identity != null && identity.getName() != null && identity.getSurname() != null) {
				String name = identity.getName().trim();
				String surname = identity.getSurname().trim();

				char nameInitial = name.charAt(0);
				char surnameInitial = surname.split(" ")[0].charAt(0);

				String expectedPrefix = "" + nameInitial + surnameInitial;
				String identifier = assistanceAgents.getEmployeeCode();

				boolean matchesPattern = identifier != null && identifier.matches("^[A-Z]{2,3}\\d{6}$");
				boolean startsWithPrefix = identifier != null && identifier.startsWith(expectedPrefix);

				// Verificamos si ya existe otro manager con el mismo identifier
				boolean alreadyExists = this.repository.existsByEmployeeCode(identifier);
				AssistanceAgent existing = this.repository.findByEmployeeCode(identifier);

				// Es válido si no existe o si existe pero es el mismo
				boolean isUnique = !alreadyExists || existing != null && existing.getId() == assistanceAgents.getId();

				boolean isValidIdentifier = matchesPattern && startsWithPrefix && isUnique;

				super.state(context, isValidIdentifier, "employeeCode", "acme.validation.assistanceAgent.employeeCode.invalid.message");
			}
		}

		result = !super.hasErrors(context);
		return result;
	}
}
