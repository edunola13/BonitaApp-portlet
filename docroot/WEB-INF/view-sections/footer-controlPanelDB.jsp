<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects />

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.2/css/select2.min.css" rel="stylesheet" />
<style>
.select2-search__field{
	width: 95% !important;
}
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.2/js/select2.min.js"></script>
<script>	
	<%if(renderRequest.getAttribute("scrollToPortlet") != null){ %>
	   	document.getElementById("bonita-controlPanel").scrollIntoView(true);
	<%} %>
</script>
<script type="text/javascript">
	function select2Process(){
		$('#bpmPanel-selectProcessDB').select2();
	}	
	function select2Cases(){
  		$('#bpmPanel-selectCasesDB').select2();
	}
	select2Process();
	select2Cases();
</script>	