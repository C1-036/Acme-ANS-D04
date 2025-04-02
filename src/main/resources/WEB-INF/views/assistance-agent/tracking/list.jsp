<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistance-agent.tracking.list.label.lastUpdateMoment" path="lastUpdateMoment" width="20%"/>
	<acme:list-column code="assistance-agent.tracking.list.label.resolutionDetails" path="resolutionDetails" width="20%"/>
	<acme:list-column code="assistance-agent.tracking.list.label.stepUndergoing" path="stepUndergoing" width="20%"/>
	<acme:list-column code="assistance-agent.tracking.list.label.accepted" path="accepted" width="20%"/>
	<acme:list-column code="assistance-agent.tracking.list.label.resolutionPercentage" path="resolutionPercentage" width="20%"/>
</acme:list>

<acme:button code="assistance-agent.tracking.list.button.create" action="/assistance-agent/tracking/create?claimId=${claimId}"/>
