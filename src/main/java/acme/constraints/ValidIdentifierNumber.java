
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)  // Aplica a clases completas
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentifierNumberValidator.class)  // Asocia con el validador
public @interface ValidIdentifierNumber {

	// Mensaje de error por defecto
	String message() default "El identificador no es válido para el AirlineManager.";

	// Grupos de validación estándar
	Class<?>[] groups() default {};

	// Carga útil opcional para la validación
	Class<? extends Payload>[] payload() default {};
}
