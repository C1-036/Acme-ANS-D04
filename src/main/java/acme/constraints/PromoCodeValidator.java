
package acme.constraints;

import java.time.Year;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;

@Validator
public class PromoCodeValidator extends AbstractValidator<ValidPromoCode, String> {

	// Regex pattern without year validation
	private static final String BASE_PATTERN = "^[A-Z]{4}-\\d{2}$";


	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		assert context != null;

		// Permitir valores vacíos (promoCode es opcional)
		if (StringHelper.isBlank(value))
			return true;

		// Verificar que el formato general sea correcto
		boolean matchesFormat = Pattern.matches(PromoCodeValidator.BASE_PATTERN, value);
		if (!matchesFormat) {
			this.state(context, false, "promoCode", "acme.validation.promocode.format");
			return false;
		}

		// Extraer los últimos dos dígitos y verificar que coincidan con el año actual
		String lastTwoDigits = value.substring(value.length() - 2);
		String currentYearLastTwoDigits = String.valueOf(Year.now().getValue()).substring(2);

		boolean validYear = lastTwoDigits.equals(currentYearLastTwoDigits);
		this.state(context, validYear, "promoCode", "acme.validation.promocode.year");

		return validYear;
	}
}
