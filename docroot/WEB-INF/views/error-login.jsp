<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects />

<h3>No se pudo realizar la conexion de manera exitosa contra el servidor de Bonita.
Probablemente sea porque la contraseña quedo desactualizada.</h3>
<a href="<%= renderRequest.getAttribute("updateDataActionUrl")%>" class="btn btn-primary">Actualizar Datos</a>