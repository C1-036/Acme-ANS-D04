
package acme.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

@Constraint(validatedBy = PromoCodeValidator.class)
@ReportAsSingleViolation

@Length(min = 7, max = 7)
@Pattern(regexp = "^[A-Z]{4}-[0-9]{2}$")

public @interface ValidPromoCode {

	// Standard validation properties -----------------------------------------

	String message() default "Invalid promo code format";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
