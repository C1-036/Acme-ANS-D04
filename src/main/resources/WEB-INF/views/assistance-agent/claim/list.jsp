
<%@page%>  

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>  

<acme:list>  
    <acme:list-column code="assistance-agent.claim.list.label.registrationMoment" path="registrationMoment" width="20%"/>  
    <acme:list-column code="assistance-agent.claim.list.label.passengerEmail" path="passengerEmail" width="25%"/>  
    <acme:list-column code="assistance-agent.claim.list.label.description" path="description" width="30%"/>  
    <acme:list-column code="assistance-agent.claim.list.label.type" path="type" width="15%"/>  
    <acme:list-column code="assistance-agent.claim.list.label.accepted" path="accepted" width="10%"/>  
</acme:list>  

<jstl:if test="${acme:anyOf(_command, 'listCompleted||listUndergoing')}">  
    <acme:button code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>  
</jstl:if>  
