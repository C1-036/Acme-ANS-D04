
package acme.constrains;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;

@Validator
public class PromoCodeValidator extends AbstractValidator<ValidPromoCode, String> {

	// Regex pattern for promo code validation
	private static final String PROMO_CODE_PATTERN = "^[A-Z]{4}-\\d{2}$";

	// AbstractValidator interface --------------------------------------------


	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		assert context != null;

		if (StringHelper.isBlank(value))
			return true;

		boolean matchesFormat = Pattern.matches(PromoCodeValidator.PROMO_CODE_PATTERN, value);
		this.state(context, matchesFormat, "promoCode", "acme.validation.promocode.format");

		return matchesFormat;
	}
}
