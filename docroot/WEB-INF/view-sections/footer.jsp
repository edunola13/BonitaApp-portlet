<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
<script>
	$(document).ready(function(){
		var locale= "<%=renderRequest.getLocale() %>";
	   	$('#tabla-bonita').dataTable( { //CONVERTIMOS NUESTRO LISTADO DE LA FORMA DEL JQUERY.DATATABLES- PASAMOS EL ID DE LA TABLA
	        "sPaginationType": "full_numbers", //DAMOS FORMATO A LA PAGINACION(NUMEROS)
	        "language": change_locale_i18n(locale.split("_")[0]),
	        "search": {"search": "<%=(renderRequest.getAttribute("bosSearch") != null) ? renderRequest.getAttribute("bosSearch") : "" %>"}
	    } );
	})		
	
	<%if(renderRequest.getAttribute("scrollToPortlet") != null){ %>
		document.getElementById("bonita-userApps").scrollIntoView(true);
	<%} %>
</script>

<%
Long taskId= (Long)renderRequest.getAttribute("autoload-taskId");
// taskId= 420026l; //De Prueba
if(taskId != null){
%>
<portlet:resourceURL var="autoloadDoTaskAjax" id="doTaskAjax">
	<portlet:param name="taskId" value="<%=Long.toString(taskId) %>"/>						
</portlet:resourceURL>
<portlet:actionURL var="autoloadDoTask" name="doTask">
	<portlet:param name="taskId" value="<%=Long.toString(taskId) %>"/>							
</portlet:actionURL>
<script>
	function executeAjax(){
		doTask("<%=autoloadDoTaskAjax %>");
	}

	$(document).ready(function(){
		<%if(renderRequest.getPreferences().getValue("doTaskAjax", "true").equals("true")){ %>
			window.setTimeout(executeAjax,2000);			
		<%}else{%>
			window.location= '<%=autoloadDoTask %>';
		<%} %>
	})
</script>
<% }%>