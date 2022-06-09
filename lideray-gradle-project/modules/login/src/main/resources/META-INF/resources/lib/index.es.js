import Vue from 'vue/dist/vue.common';

export default function(portletNamespace) {

	const auth = "dGVzdEBsaWZlcmF5LmNvbTpnZXNsbw==";

	new Vue({
		el: `#${portletNamespace}-1`,
		data: {
			message: 'Hello from Vue.js!'
		},
		methods: {
			reverseMessage: function() {
				this.message = this.message
					.split('')
					.reverse()
					.join('');
			}
		}
	});

	Vue.component('todo-item', {
		props: ['todo'],
		template: '<li>{{ todo.text }}</li>'
	});

	new Vue({
		el: `#${portletNamespace}-2`,
		data: {
			groceryList: [
				{id: 0, text: 'Vegetables'},
				{id: 1, text: 'Cheese'},
				{id: 2, text: 'Whatever else humans are supposed to eat'}
			]
		}
	});

	const fetchUsers = new Vue({
		el: `#${portletNamespace}-3`,

		data: {
			result: "",
			responseAvailable: false,

		},
		methods: {
			fetchAPIData( ) {

				this.responseAvailable = false;
				fetch("http://localhost:8080/o/headless-admin-user/v1.0/user-accounts", {
					"method": "GET",
					"headers": {
						"Authorization": "Basic " + auth
					}
				})
				.then(response => {
					if(response.ok){
						return response.json()
					} else{
						alert("Server returned " + response.status + " : " + response.statusText);
					}
				})
				.then(response => {
					this.result = "";
					for (const item of response.items) {
						this.result += item.emailAddress + ", ";
					}
					console.log(this.result);
					console.log(Object.values(response));
					this.responseAvailable = true;

				})
				.catch(err => {
					console.log(err);
				});
			}
		}
	})

	const createAccount = new Vue({
		el: `#${portletNamespace}-4`,

		data: {
			result: ""

		},
		methods: {
			createUser( ) {
				const data =
					{
						"additionalName": "string",
						"alternateName": "string",
						"birthDate": "2022-05-24T15:19:24.179Z",
						"emailAddress": "user@created.com",
						"familyName": "Created",
						"givenName": "User",
						"honorificPrefix": "string",
						"honorificSuffix": "string",
						"jobTitle": "string",
					};

				fetch("http://localhost:8080/o/headless-admin-user/v1.0/user-accounts", {
					"method": "POST",
					"headers": {
						"Accept" : "application/json",
						"Content-Type": "application/json",
						"Authorization": "Basic " + auth
					},
					"body": JSON.stringify(data)
				})
					.then(response => {
						if(response.ok){
							return response.json()
						} else{
							alert("Server returned " + response.status + " : " + response.statusText);
						}
					})
					.then(response => {
						this.result = response;

					})
					.catch(err => {
						console.log(err);
					});
			}
		}
	})

	const login = new Vue({
		el: `#${portletNamespace}-5`,

		data: {
			loginUsername: "",
			loginPassword: "",
			result: "",
			loginUser: ""

		},
		mounted() {
			if(localStorage.loginUser){
				this.loginUser = localStorage.loginUser;
			}
		},
		watch: {
			loginUser(newLogin){
				localStorage.loginUser = newLogin;
			}
		},
		methods: {
			login( ) {

				console.log(this.loginUsername);

				fetch("http://localhost:8080/o/headless-admin-user/v1.0/user-accounts", {
					"method": "GET",
					"headers": {
						"Authorization": "Basic " + auth
					}
				})
					.then(response => {
						if(response.ok){
							return response.json()
						} else{
							alert("Server returned " + response.status + " : " + response.statusText);
						}
					})
					.then(response => {
						this.result = null;
						for (const item of response.items) {
							if(this.loginUsername === item.emailAddress && this.loginPassword === item.honorificPrefix){
								this.result = item;
								this.loginUser = item.emailAddress;
								this.loginUsername = "";
								this.loginPassword = "";
							}
						}
						console.log(this.result);
						document.getElementById("login1").value = "";
						document.getElementById("login2").value = "";
					})
					.catch(err => {
						console.log(err);
					});
			}
		}
	})

	const register = new Vue({
		el: `#${portletNamespace}-6`,

		data: {
			registerUsername: "",
			registerPassword: "",
			firstName: "",
			lastName: "",
			registerScreenName: "",
			result: ""

		},
		methods: {
			register( ) {

				console.log(this.registerUsername);

				let data =
					{

						"alternateName": `${this.registerScreenName}`,
						"birthDate": "2022-05-24T15:19:24.179Z",
						"emailAddress": `${this.registerUsername}`,
						"familyName": `${this.lastName}`,
						"givenName": `${this.firstName}`,
						"honorificPrefix": `${this.registerPassword}`,
						"jobTitle": "none"
					};





				fetch("http://localhost:8080/o/headless-admin-user/v1.0/user-accounts", {
					"method": "GET",
					"headers": {
						"Authorization": "Basic " + auth
					}
				})
					.then(response => {
						if(response.ok){
							return response.json()
						} else{
							alert("Server returned " + response.status + " : " + response.statusText);
						}
					})
					.then(response => {
						let taken = false;
						for (const item of response.items) {
							if(this.registerUsername === item.emailAddress){
								this.result = "username already exists";
								console.log(this.result);
								taken = true;
								break;
							}
						}

						if(!taken) {
							fetch("http://localhost:8080/o/headless-admin-user/v1.0/user-accounts", {
								"method": "POST",
								"headers": {
									"Authorization": "Basic " + auth,
									"Accept": "application/json",
									"Content-Type": "application/json"
								},
								"body": JSON.stringify(data)
							})
								.then(response => {
									console.log(data);
									if (response.ok) {
										return response.json()
									} else {
										alert("Server returned " + response.status + " : " + response.statusText);
									}
								})
								.then(response => {
									console.log(response);

									this.registerUsername = "";
									this.registerPassword = "";
									this.firstName = "";
									this.lastName = "";
									this.registerScreenName = "";

									document.getElementById("register1").value = "";
									document.getElementById("register2").value = "";
									document.getElementById("register3").value = "";
									document.getElementById("register4").value = "";
									document.getElementById("register5").value = "";
								})
								.catch(err => {
									console.log(err);
								});
						}
					})
					.catch(err => {
						console.log(err);
					});
			}
		}
	})

	const createOrganization = new Vue({
		el: `#${portletNamespace}-7`,

		data: {
			result: "",
			organizationName: "",
			organizationId: ""

		},
		methods: {
			createOrganization( ) {

				let data =
					{
						"id": `${this.organizationId}`,
						"name": `${this.organizationName}`
					};

				fetch("http://localhost:8080/o/headless-admin-user/v1.0/organizations", {
					"method":"GET",
					"headers": {
						"Authorization": "Basic " + auth,
						"Accept" : "application/json",
						"Content-Type": "application/json"
					}
				})
					.then(response => {
						console.log(response);
						if(response.ok){
							return response.json()
						} else{
							alert("Server returned " + response.status + " : " + response.statusText);
						}
					})
					.then(response => {
						let taken = false;
						this.result = "";
						console.log(response);
						for (const item of response.items) {
							if(this.organizationName === item.name && this.organizationName === item.id){
								this.result = "username already exists";
								console.log(this.result);
								taken = true;
								break;
							}
						}

						if(!taken) {
							fetch("http://localhost:8080/o/headless-admin-user/v1.0/organizations", {
								"method": "POST",
								"headers": {
									"Authorization": "Basic " + auth,
									"Accept": "application/json",
									"Content-Type": "application/json"
								},
								"body": JSON.stringify(data)
							})
								.then(response => {
									if (response.ok) {
										return response.json()
									} else {
										alert("Server returned " + response.status + " : " + response.statusText);
									}
								})
								.then(response => {
									console.log("Organization created sucessfully");

									this.organizationName = "";
									this.organizationId = "";

									document.getElementById("org1").value = "";
									document.getElementById("org2").value = "";
									location.reload();
								})
								.catch(err => {
									console.log(err);
								});
						}
					})
					.catch(err => {
						console.log(err);
					});
			}
		}
	})
/*
	const addUserToOrganization = new Vue({
		el: `#${portletNamespace}-9`,

		data: {
			users : {},
			organizations : ""
		},
		mounted() {
			console.log("Boi");
			fetch("http://localhost:8080/o/headless-admin-user/v1.0/user-accounts", {
				"method":"GET",
				"headers": {
					"Authorization": "Basic " + auth,
					"Accept" : "application/json",
					"Content-Type": "application/json"
				}
			})
				.then(response => {
					console.log(response);
					if(response.ok){
						return response.json()
					} else{
						alert("Server returned " + response.status + " : " + response.statusText);
					}
				})
				.then(response => {
					for (const item of response.items) {
						users.push(item.name);
					}
					console.log(users);

					if(!taken) {
						fetch("http://localhost:8080/o/headless-admin-user/v1.0/organizations", {
							"method": "POST",
							"headers": {
								"Authorization": "Basic " + auth,
								"Accept": "application/json",
								"Content-Type": "application/json"
							},
							"body": JSON.stringify(data)
						})
							.then(response => {
								if (response.ok) {
									return response.json()
								} else {
									alert("Server returned " + response.status + " : " + response.statusText);
								}
							})
							.then(response => {
								console.log("Organization created sucessfully");
							})
							.catch(err => {
								console.log(err);
							});
					}
				})
				.catch(err => {
					console.log(err);
				});
		},
		methods: {
			addUserToOrganization( ) {
			}
		}
	})
*/
	Vue.component('organizacije', {
		props: ['org', 'usersReady'],
		data() {
			return {
				isPost: false,
			}
		},
		template: `
<div>
			<div v-if="this.isPost === false">
				<h3> {{ org.name }} </h3>
				<button v-on:click="info(org.id)">info</button>
			</div>
			<div v-else-if="this.isPost === true">
				<h3> {{ org.name }} </h3>
				<div id="usersReady"></div>
				<!--
				<input v-model="userToOrganization" placeholder="Dodaj uporabnika"/>
				<input v-model="jobTitle" placeholder="Delovno mesto"/>
				<button v-on:click="dodajUporabnik()">Dodaj uporabnika</button>
				<br>
				-->
				<button v-on:click="skrijInfo()">skrij info</button>
			</div>
			<hr>
</div>			
			`,
		methods: {
			info: function(orgId) {
				this.isPost = true;
				this.organizacijaId = orgId;

				fetch(`http://localhost:8080/o/headless-admin-user/v1.0/organizations/${orgId}/user-accounts`, {
					"method":"GET",
					"headers": {
						"Authorization": "Basic " + auth,
						"Accept" : "application/json",
						"Content-Type": "application/json"
					}
				})
					.then(response => {
						if (response.ok) {
							return response.json()
						} else {
							alert("Server returned " + response.status + " : " + response.statusText);
						}
					})
					.then(response => {
						this.users = response.items;

						this.usersReady = "<ul>";

						for (const user of this.users) {
							this.usersReady += "<li>" + user.emailAddress + "</li>";
						}

						this.usersReady += "</ul>";

						document.getElementById("usersReady").innerHTML = "Niste del te organizacije";
						for (const user of this.users) {
							if (localStorage.loginUser === user.emailAddress){
								document.getElementById("usersReady").innerHTML = this.usersReady;
							}
						}

					})
					.catch(err => {
						console.log(err);
					});

			},
			skrijInfo: function(){
				this.isPost = false;

			},
			dodajUporabnik: function(){
				console.log(this.jobTitle);
				console.log(this.organizacijaId);
				let userId = "";


				fetch("http://localhost:8080/o/headless-admin-user/v1.0/user-accounts", {
					"method":"GET",
					"headers": {
						"Authorization": "Basic " + auth,
						"Accept" : "application/json",
						"Content-Type": "application/json"
					}
				})
					.then(response => {
						if (response.ok) {
							return response.json()
						} else {
							alert("Server returned " + response.status + " : " + response.statusText);
						}
					})
					.then(response => {
						this.users = response.items;

						console.log(this.users);
						for (const user of this.users) {
							if (user.emailAddress === this.userToOrganization){
								userId = user.id;
							}
						}
						console.log("User id: "+userId);

						let roleId = 20111; //organization user

						fetch(`/v1.0/roles/${roleId}/association/user-account/${userId}/organization/${this.organizacijaId}`, {
							"method": "POST",
							"headers": {
								"Authorization": "Basic " + auth,
								"Accept": "application/json",
								"Content-Type": "application/json"
							}
						})
							.then(response => {
								if (response.ok) {
									return response.json()
								} else {
									alert("Server returned " + response.status + " : " + response.statusText);
								}
							})
							.then(response => {
								console.log("");
							})
							.catch(err => {
								console.log(err);
							});
					})
					.catch(err => {
						console.log(err);
					});


			}
		}
	});

	new Vue({
		el: `#${portletNamespace}-8`,
		data: {
			post: [],
			userToOrganization: "",
			organizacijaId: "",
			jobTitle: "",
			users: [],
			usersReady:""
		},
		mounted() {
			/*					TU FETCHA ENDPOINT				*/
			fetch("http://localhost:8080/o/headless-admin-user/v1.0/organizations", {
				"method":"GET",
				"headers": {
					"Authorization": "Basic " + auth,
					"Accept" : "application/json",
					"Content-Type": "application/json"
				}
			})
			.then(e => {
				e.json().then(j => {
					console.log(j)
					console.log(j.items)
					this.post = j.items;
				})
			})
		}
	});
}