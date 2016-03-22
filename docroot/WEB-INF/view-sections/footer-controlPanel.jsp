<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script>	
	<%if(renderRequest.getAttribute("scrollToPortlet") != null){ %>
	   	document.getElementById("bonita-controlPanel").scrollIntoView(true);
	<%} %>
</script>