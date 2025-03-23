
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.assistanceagents.TrackingLog;
import acme.entities.assistanceagents.TrackingLogState;
import acme.internals.helpers.HibernateHelper;

public class TrackingLogResolutionValidator extends AbstractValidator<ValidTrackingLogResolution, TrackingLog> {

	@Override
	public void initialise(final ValidTrackingLogResolution annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (trackingLog == null)
			result = true;
		else {
			TrackingLogState state = trackingLog.getAccepted();
			String resolutionDetails = trackingLog.getResolutionDetails();

			boolean requiresResolution = state == TrackingLogState.ACCEPTED || state == TrackingLogState.REJECTED;
			boolean hasResolutionDetails = resolutionDetails != null && !resolutionDetails.trim().isEmpty();

			result = !requiresResolution || hasResolutionDetails;

			if (!result)
				HibernateHelper.replaceParameter(context, "placeholder", "acme.validation.tracking-log.resolution-details.required");
		}

		return result;
	}
}
