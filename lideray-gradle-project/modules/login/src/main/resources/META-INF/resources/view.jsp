<%@ include file="/init.jsp" %>

<!--
<div id="<portlet:namespace />-1">
	<p>A friendly reversible message from Vue.js:</p>
	<p>{{message}}</p>
	<button v-on:click="reverseMessage">Reverse message, pretty please</button>
</div>

<hr />






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
-->
<div style="margin: 2em;">
	<div style="float: right;" id="<portlet:namespace />-5">
		<p>Prijavljeni kot: {{loginUser}}</p>
		<h2>Prijava:</h2>
		<input id="login1" v-model="loginUsername" placeholder="Mail"/> <br>
		<input type="password" id="login2" v-model="loginPassword" placeholder="Geslo"/>
		<button v-on:click="login">Login</button>
	</div>

	<div style="float: right;" id="<portlet:namespace />-6">
		<h2>Registracija:</h2>
		<input id="register1" v-model="registerScreenName" placeholder="Uporabniško_ime"/> <br>
		<input id="register2" v-model="registerUsername" placeholder="Mail"/> <br>
		<input type="password" id="register3" v-model="registerPassword" placeholder="Geslo"/> <br>
		<input id="register4" v-model="firstName" placeholder="Ime "/> <br>
		<input id="register5" v-model="lastName" placeholder="Priimek "/>
		<button v-on:click="register">Register</button>
	</div>

	<div id="<portlet:namespace />-7">
		<h2>Ustvari organizacijo:</h2>
		<input id="org1" v-model="organizationName" placeholder="Ime organizacije"/>
		<input id="org2" v-model="organizationId" placeholder=" Organizacija ID"/>
		<button v-on:click="createOrganization">Create</button>
		<br>
		<br>
	</div>

	<div id="<portlet:namespace />-8">
		<h2>Organizacije</h2>

			<organizacije
					v-bind:key="item.id"
					v-bind:org="item"
					v-for="item in post"
			/>

	</div>
</div>

<aui:script require="<%= mainRequire %>">
	main.default('<portlet:namespace />');
</aui:script>