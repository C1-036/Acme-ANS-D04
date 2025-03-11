
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.customers.Booking;
import acme.entities.customers.BookingRepository;

@Validator
public class NibbleCreditCardValidator extends AbstractValidator<ValidNibbleCreditCard, String> {

	@Autowired
	private BookingRepository repository;

	//ConstraintValidator interface -------------------------------------------


	@Override
	protected void initialise(final ValidNibbleCreditCard annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {

		assert context != null;

		Booking booking = this.repository.findByCreditCard(value);

		boolean result;
		boolean isNull;

		isNull = booking == null || booking.getCreditCard() == null;

		if (!isNull) {
			String codeCreditCard;
			boolean cuatroDigitos;

			codeCreditCard = this.repository.findCreditCardByBookingId(booking.getId());
			cuatroDigitos = value.equals(codeCreditCard) && codeCreditCard.length() == 4;
			super.state(context, cuatroDigitos, "Cuatro digitos", "acme.validaton.booking.nibble.message");
		}
		result = !super.hasErrors(context);

		return result;
	}
}
