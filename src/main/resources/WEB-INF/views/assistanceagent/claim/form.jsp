<%@page%>  

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>  

<acme:form>  
    <acme:input-moment code="assistanceagent.claim.form.label.registrationMoment" path="registrationMoment"/>  
    <acme:input-textbox code="assistanceagent.claim.form.label.passengerEmail" path="passengerEmail"/>  
    <acme:input-textarea code="assistanceagent.claim.form.label.description" path="description"/>  
    <acme:input-select code="assistanceagent.claim.form.label.type" path="type" choices="${claimTypes}"/>  
    <acme:input-select code="assistanceagent.claim.form.label.accepted" path="accepted" choices="${claimStates}"/>  

    <jstl:choose>    
        <jstl:when test="${_command == 'show'}">  
            <acme:submit code="assistanceagent.claim.form.button.update" action="/assistanceagent/claim/update"/>  
        </jstl:when>  
        <jstl:when test="${acme:anyOf(_command, 'update|delete')}">  
            <acme:submit code="assistanceagent.claim.form.button.update" action="/assistanceagent/claim/update"/>  
            <acme:submit code="assistanceagent.claim.form.button.delete" action="/assistanceagent/claim/delete"/>  
        </jstl:when>  
        <jstl:when test="${_command == 'create'}">  
            <acme:submit code="assistanceagent.claim.form.button.create" action="/assistanceagent/claim/create"/>  
        </jstl:when>    
    </jstl:choose>  
</acme:form>  
