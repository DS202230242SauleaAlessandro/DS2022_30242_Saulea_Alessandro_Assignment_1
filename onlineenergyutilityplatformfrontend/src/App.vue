<template>
  <v-app>
    <v-app-bar
      app
      color="primary"
      dark
    >
      <div class="d-flex align-center">
        <v-img
          alt="Vuetify Logo"
          class="shrink mr-2"
          contain
          src="https://cdn.vuetifyjs.com/images/logos/vuetify-logo-dark.png"
          transition="scale-transition"
          width="40"
        />

        <h1>Online Energy Utility Platform</h1>
      </div>

      <v-spacer></v-spacer>
      <div v-if="currentUser != null">
        <v-chip class="ma-2" color="primary">
          Bine ai venit, <b>{{ currentUser.name}}</b>!
        </v-chip>
        <v-btn v-if="currentUser.admin" text @click="$router.push({path: '/admin'})">
          Administration
        </v-btn>
        <v-btn class="mr-4"
               text
               @click="logout">
          Log out
        </v-btn>
      </div>
    </v-app-bar>

    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script>

import axios from "axios";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

export default {
  name: 'App',

  data(){
    return{
      currentUser: null
    }
  },

  async created(){
    const userId = window.sessionStorage.getItem("userId")
    const origin = window.location.protocol + '//' + window.location.hostname + ':8080'

    if (userId !== null){
      try{
        this.currentUser = (await axios.get(origin + "/users/" +window.sessionStorage.getItem("userId"))).data

        const stompClient = Stomp.over(new SockJS(origin + '/gs-guide-websocket'));
        stompClient.connect({username: window.sessionStorage.getItem("userId")}, () => {
          stompClient.subscribe('/topic/notify', payload => {
            const notification = JSON.parse(payload.body)
            if (notification.userId === window.sessionStorage.getItem("userId")) alert(notification.message)
          })
        })
      }catch(e) {
        console.log('no user logged in')
      }
    }
  },

  methods:{
    async logout(){
      window.sessionStorage.setItem("userId", null)
      window.location.href = '/login'
    }
  }
};
</script>
