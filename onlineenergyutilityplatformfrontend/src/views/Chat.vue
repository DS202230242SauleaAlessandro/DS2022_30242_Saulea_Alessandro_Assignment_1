<template>
  <div>
    <h1>Chat box</h1>
    <v-container>
      <v-row>
        <v-col>
          <v-card>
            <v-card-title>Conversations</v-card-title>
            <v-data-table :headers="headers" :items="users" :search="search" class="elevation-1" fixed-header height="350px">
              <template v-slot:top>
                <div class="d-flex w-100">
                  <v-text-field v-model="search" append-icon="mdi-magnify" label="Search" dense outlined></v-text-field>
                </div>
              </template>
              <template v-slot:item.name="{ item }"><span>{{item.name}}</span></template>
              <template v-slot:item.actions="{ item }"><v-btn text @click="select(item)">Select</v-btn></template>
            </v-data-table>
          </v-card>
        </v-col>
        <v-col>
          <v-card v-if="userSelected !== null">
            <v-card-title> {{ userSelected.name }} </v-card-title>
            <div id="chat">
              <div v-for="msg in currentMessages" :key="msg.message">
                <div v-if="msg.from === currentUser.uuid" align="right">
                  <v-chip class="ma-2" color="primary">{{msg.message}}</v-chip>
                </div>
                <div v-else>
                  <v-chip class="ma-2">{{msg.message}}</v-chip>
                </div>
              </div>
            </div>
            <div class="d-flex">
              <v-text-field
                  v-model="newMessage"
                  label="Type message"
                  rounded
                  outlined
                  dense
                  @change="sendTypingNotification()"
              ></v-text-field>
              <v-btn class="mr-4" @click="sendMessage(newMessage)">Send</v-btn>
            </div>
            <p> {{ notifyMessage }}</p>
          </v-card>

        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import axios from "axios";
import App from "../App"
import Stomp from "stompjs";
import SockJS from "sockjs-client";

export default {
  name: "Chat",
  components: {App},
  data() {
    return {
      users: [],
      currentUser: null,
      headers: [
        {
          text: 'Name', value: 'name',
        },
        {
          text: 'Actions', value: 'actions', width: '200px'
        }
      ],
      search: '',
      messages: {},
      currentMessages: [],
      userSelected: null,
      origin: window.location.protocol + '//' + window.location.hostname + ':8080',
      newMessage: '',
      stompClient: null,
      notifyMessage: '',
    }
  },

  async created() {
    this.currentUser = (await axios.get(this.origin+'/users/'+window.sessionStorage.getItem("userId"))).data
    this.users = (await axios.get(this.origin+'/users')).data.filter(user => user.uuid !== this.currentUser.uuid)
    this.users.forEach(user => this.messages[user.uuid] = [])

    this.stompClient = Stomp.over(new SockJS(this.origin + '/gs-guide-websocket'));
    this.stompClient.connect({username: window.sessionStorage.getItem("userId")}, () => {
      this.users.forEach(user => {
        this.stompClient.subscribe('/topic/msg_from_' + user.uuid, payload => {
          const message = JSON.parse(payload.body)
          this.notifyMessage = ''
          if (message.to === this.currentUser.uuid){
            this.messages[message.from].push(message)

            this.stompClient.send('/app/send_read', {},
                JSON.stringify({'from': this.currentUser.uuid, 'to': this.userSelected.uuid}))
          }
        })

        this.stompClient.subscribe('/topic/read_from_' + user.uuid, payload => {
          const message = JSON.parse(payload.body)
          if (message.to === this.currentUser.uuid){
            this.notifyMessage = 'Seen from ' + this.users.find(usr => usr.uuid === message.from).name
          }
        })

        this.stompClient.subscribe('/topic/typing_from_' + user.uuid, payload => {
          const message = JSON.parse(payload.body)
          if (message.to === this.currentUser.uuid){
            this.notifyMessage = this.users.find(usr => usr.uuid === message.from).name + ' is typing'
          }
        })
      })
    })
  },

  methods: {
    select(item){
      this.userSelected = item
      this.currentMessages = this.messages[item.uuid]

      this.stompClient.send('/app/send_read', {},
          JSON.stringify({'from': this.currentUser.uuid, 'to': this.userSelected.uuid}))
    },

    sendMessage(msg){
      const msgObj = {'message': msg, 'from': this.currentUser.uuid, 'to': this.userSelected.uuid}
      this.currentMessages.push(msgObj)
      this.stompClient.send('/app/send_msg', {}, JSON.stringify(msgObj))

      var element = document.getElementById("chat");
      element.scrollTop = element.scrollHeight;
    },

    sendTypingNotification(){
      this.stompClient.send('/app/send_typing', {},
          JSON.stringify({'from': this.currentUser.uuid, 'to': this.userSelected.uuid}))
    }
  }
}
</script>

<style scoped>
#chat {
  height: 600px;
  overflow: auto;
}
</style>