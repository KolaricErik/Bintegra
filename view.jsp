<%@ include file="/init.jsp" %>

<!--
<div id="<portlet:namespace />-1">
	<p>A friendly reversible message from Vue.js:</p>
	<p>{{message}}</p>
	<button v-on:click="reverseMessage">Reverse message, pretty please</button>
</div>

<hr />


-->



<div id="<portlet:namespace />-3">
	<p>Button to call api</p>
	<p>{{result}}</p> <br>
	<button v-on:click="fetchAPIData">Fetch api data</button>
</div>

<div id="<portlet:namespace />-4">
	<p>Button to create user</p>
	<p>{{result}}</p> <br>
	<button v-on:click="createUser">Create user</button>
</div>

<div id="<portlet:namespace />-5">
	<p>Logged in: {{loginUser}}</p>
    <p>Login:</p>
    <input v-model="loginUsername" placeholder="Mail"/>
    <input v-model="loginPassword" placeholder="Password"/>
	<button v-on:click="login">Login</button>
</div>

<div id="<portlet:namespace />-6">
	<p>Register:</p>
	<input v-model="registerScreenName" placeholder="Screen name"/>
	<input v-model="registerUsername" placeholder="Mail"/>
	<input v-model="registerPassword" placeholder="Password"/>
	<input v-model="firstName" placeholder="First name"/>
	<input v-model="lastName" placeholder="Last name"/>
	<button v-on:click="register">Register</button>
</div>

<div id="<portlet:namespace />-7">
	<p>Create organization:</p>
	<input v-model="organizationName" placeholder="Organization name"/>
	<input v-model="organizationId" placeholder=" Organization ID"/>
	<button v-on:click="createOrganization">Create</button>
</div>

<aui:script require="<%= mainRequire %>">
	main.default('<portlet:namespace />');
</aui:script>