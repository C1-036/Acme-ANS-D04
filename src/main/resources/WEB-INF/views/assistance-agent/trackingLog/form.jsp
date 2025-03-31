<%--
- trackingLog.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="assistance-agent.trackingLog.form.label.stepUndergoing" path="stepUndergoing"/>
    <acme:input-moment code="assistance-agent.trackingLog.form.label.lastUpdateMoment" path="lastUpdateMoment"/>
    <acme:input-double code="assistance-agent.trackingLog.form.label.resolutionPercentage" path="resolutionPercentage"/>
    <acme:input-select code="assistance-agent.trackingLog.form.label.accepted" path="accepted" choices="ACCEPTED,REJECTED,PENDING"/>
    <acme:input-textarea code="assistance-agent.trackingLog.form.label.resolutionDetails" path="resolutionDetails"/>

    <jstl:if test="${_command == 'show'}">
        <acme:input-moment code="assistance-agent.trackingLog.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
        <acme:input-textbox code="assistance-agent.trackingLog.form.label.stepUndergoing" path="stepUndergoing" readonly="true"/>
        <acme:input-double code="assistance-agent.trackingLog.form.label.resolutionPercentage" path="resolutionPercentage" readonly="true"/>
    </jstl:if>

    <jstl:choose>
        <jstl:when test="${_command == 'show' && draftMode == false}">
            <acme:button code="assistance-agent.trackingLog.form.button.add" action="/assistance-agent/trackingLog/add?masterId=${id}"/>
        </jstl:when>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
            <acme:button code="assistance-agent.trackingLog.form.button.add" action="/assistance-agent/trackingLog/add?masterId=${id}"/>
            <acme:submit code="assistance-agent.trackingLog.form.button.update" action="/assistance-agent/trackingLog/update"/>
            <acme:submit code="assistance-agent.trackingLog.form.button.delete" action="/assistance-agent/trackingLog/delete"/>
            <acme:submit code="assistance-agent.trackingLog.form.button.publish" action="/assistance-agent/trackingLog/publish"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="assistance-agent.trackingLog.form.button.create" action="/assistance-agent/trackingLog/create"/>
        </jstl:when>
    </jstl:choose>
</acme:form>
