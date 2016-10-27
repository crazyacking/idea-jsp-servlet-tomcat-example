<%@ page import="net.therap.mealplanner.entity.Dish" %>
<%@ page import="net.therap.mealplanner.entity.Meal" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: rumman
  Date: 10/24/16
  Time: 11:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<%@ include file="header.jsp" %>

<body>
<%@ include file="topbar.jsp" %>

<div class="container-fluid">
    <div class="row">

        <%@ include file="sidebar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <a href="<% out.print(request.getContextPath());%>/add-meal" class="btn btn-success pull-right">Add Meal</a>

            <h2 class="sub-header">Meal List</h2>

            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Day</th>
                        <th>Items</th>
                        <th>Type</th>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        // retrieve your list from the request, with casting
                        ArrayList<Meal> mealList = (ArrayList<Meal>) request.getAttribute("mealList");
                        // print the information about every dish of the list
                        for (Meal meal : mealList) { %>
                    <tr>
                        <td><% out.print(meal.getName()); %></td>
                        <td><% out.print(meal.getDay()); %></td>
                        <td>
                            <%
                                for (Dish dish : meal.getDishSet()) {
                                    out.print(dish.getName() + " ");
                                }
                            %>
                        </td>
                        <td><% out.print(meal.getMenuType().getCategory()); %></td>
                        <td>
                            <a href="<% out.print(request.getContextPath());%>/edit-meal/?id=<% out.print(meal.getId()); %>"><span
                                    class="glyphicon glyphicon-edit"></span><a/></td>
                        <%--<td><a href="http://www.google.com"><span class="glyphicon glyphicon-trash"></span><a/></td>--%>
                        <td><a href="#"
                               data-href="<% out.print(request.getContextPath());%>/delete-meal/?id=<% out.print(meal.getId()); %>"
                               data-toggle="modal" data-target="#confirm-delete" class="delete-user-item"><span
                                class="glyphicon glyphicon-trash"></span></a></td>
                    </tr>

                    <% } %>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>

</body>
<div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Confirm Delete</h4>
            </div>

            <div class="modal-body">
                <p>You are about to delete a meal, this procedure is irreversible.</p>

                <p>Do you want to proceed?</p>

                <p class="debug-url"></p>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <a href="#" class="btn btn-danger btn-ok">Delete</a>
            </div>
        </div>
    </div>
</div>

<script>
    $('.delete-user-item').on('click', function (e) {
        var criteria_id = $(this).attr("data-href");
        $('.btn-ok').attr("href", criteria_id);
    });
</script>
</html>

