
<%@page%>  

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>  

<acme:list>  
    <acme:list-column code="assistanceagent.claim.list.label.registrationMoment" path="registrationMoment" width="20%"/>  
    <acme:list-column code="assistanceagent.claim.list.label.passengerEmail" path="passengerEmail" width="25%"/>  
    <acme:list-column code="assistanceagent.claim.list.label.description" path="description" width="30%"/>  
    <acme:list-column code="assistanceagent.claim.list.label.type" path="type" width="15%"/>  
    <acme:list-column code="assistanceagent.claim.list.label.accepted" path="accepted" width="10%"/>  
</acme:list>  

<jstl:if test="${_command == 'listCompleted'}">  
    <acme:button code="assistanceagent.claim.form.button.create" action="/assistanceagent/claim/create"/>  
</jstl:if>  
