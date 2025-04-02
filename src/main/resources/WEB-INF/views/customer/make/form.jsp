<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="passenger.booking.form.label.booking"
        path="bookingReference" readonly="true"/>
    <acme:input-select code="passenger.booking.form.label.passenger"
        path="passenger" choices="${passengers}" />

	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.make.form.button.link"
             action="/customer/make/create?bookingId=${id}" />
		</jstl:when>
		<jstl:when test="${_command == 'delete'}">
			<acme:submit code="customer.make.form.button.unlink"
				action="/customer/make/delete" />
		</jstl:when>
	</jstl:choose>
</acme:form>