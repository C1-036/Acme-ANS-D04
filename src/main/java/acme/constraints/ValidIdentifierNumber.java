
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentifierNumberValidator.class)
public @interface ValidIdentifierNumber {

	String message() default "acme.validation.airline-manager.identifier-number.message";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
