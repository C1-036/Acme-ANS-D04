
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.customers.Booking;
import acme.entities.customers.BookingRepository;

@Validator
public class NibbleCreditCardValidator extends AbstractValidator<ValidNibbleCreditCard, String> {

	private BookingRepository	repository;
	private Booking				booking;

	//ConstraintValidator interface -------------------------------------------


	@Override
	protected void initialise(final ValidNibbleCreditCard annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		boolean isNull;

		isNull = this.booking == null || this.booking.getCreditCard() == null;

		if (!isNull) {
			String codeCreditCard;
			boolean cuatroDigitos;

			codeCreditCard = this.repository.findCreditCardByBookingId(this.booking.getId());
			cuatroDigitos = value.equals(codeCreditCard) && codeCreditCard.length() == 4;
			super.state(context, cuatroDigitos, "Cuatro digitos", "acme.validaton.booking.nibble.message");
		}
		result = !super.hasErrors(context);

		return result;
	}
}
