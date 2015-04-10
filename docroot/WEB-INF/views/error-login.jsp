<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects />

<h5>No se pudo realizar la conexion de manera exitosa contra el servidor de Bonita. Esto se puede deber a</h5>
<h6>La contraseña quedo desactualizada en el servidor Bonita o</h6>
<h6>La URL que indica el servidor Bonita no esta accesible.</h6>
<a href="<%= renderRequest.getAttribute("updateDataActionUrl")%>" class="btn btn-primary">Actualizar Datos</a>