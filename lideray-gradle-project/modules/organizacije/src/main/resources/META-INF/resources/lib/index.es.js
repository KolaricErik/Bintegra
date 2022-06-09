import Vue from 'vue/dist/vue.common';

export default function(portletNamespace) {

	Vue.component('todo-item', {
		props: ['todo'],
		data() {
			return {
				isPost: false,
			}
		},
		template: `
			<div v-if="this.isPost === false">
				<h1> {{ todo.title }} </h1>
				<button v-on:click="info(todo)">info</button>
			</div>
			<div v-else-if="this.isPost === true">
				<h1> {{ todo.title }} </h1>
				<p> {{ todo.body}} </p>
				<button v-on:click="skrijInfo(todo)">skrij info</button>
			</div>
			`,
		methods: {
			info: function(data) {
				this.isPost = true;
				console.log(data)

			},
			skrijInfo: function(){
				this.isPost = false;

			}

		}
	});


		new Vue({
			el: `#${portletNamespace}-2`,
			data: {
				post: []

			},
			mounted() {
				/*					TU FETCHA ENDPOINT				*/
				fetch("https://jsonplaceholder.typicode.com/posts").then(e => {
					e.json().then(j => {
						console.log(j)
						this.post = j;
					})
				})
			}
		});


}