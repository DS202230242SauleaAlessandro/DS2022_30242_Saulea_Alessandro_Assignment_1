<template>
  <v-card>
    <v-card-title>Users</v-card-title>
    <v-data-table :headers="headers" :items="users" :search="search" class="elevation-1" fixed-header height="350px">
      <template v-slot:top>
        <div class="d-flex w-100">
          <v-text-field v-model="search" append-icon="mdi-magnify" label="Search" dense outlined></v-text-field>
          <v-btn
              color="primary"
              class="ml-2 white--text"
              @click="addNew">
            <v-icon dark>mdi-plus</v-icon>Add
          </v-btn>
        </div>
      </template>
      <template v-slot:item.uuid="{ item }"><span>{{item.uuid}}</span></template>
      <template v-slot:item.name="{ item }">
        <v-text-field v-model="editedItem.name" dense single-line v-if="item.uuid === editedItem.uuid"></v-text-field>
        <span v-else>{{item.name}}</span>
      </template>
      <template v-slot:item.username="{ item }">
        <v-text-field v-model="editedItem.username" dense single-line v-if="item.uuid === editedItem.uuid"></v-text-field>
        <span v-else>{{item.username}}</span>
      </template>
      <template v-slot:item.password="{ item }">
        <v-text-field v-model="editedItem.password" dense single-line v-if="item.uuid === editedItem.uuid"></v-text-field>
        <span v-else>{{item.password}}</span>
      </template>
      <template v-slot:item.admin="{ item }">
        <v-checkbox v-model="editedItem.admin" v-if="item.uuid === editedItem.uuid"></v-checkbox>
        <span v-else>{{item.admin}}</span>
      </template>
      <template v-slot:item.actions="{ item }">
        <div v-if="item.uuid === editedItem.uuid">
          <v-icon color="red" class="mr-3" @click="close">
            mdi-window-close
          </v-icon>
          <v-icon color="green"  @click="save">
            mdi-content-save
          </v-icon>
        </div>
        <div v-else>
          <v-icon color="green" class="mr-3" @click="editItem(item)">
            mdi-pencil
          </v-icon>
          <v-icon color="red" @click="deleteItem(item)">
            mdi-delete
          </v-icon>
        </div>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import axios from "axios";

export default {
  name: "UserCRUD",
  data(){
    return {
      headers: [
        {
          text: 'UUID', value: 'uuid',
        },
        {
          text: 'Name', value: 'name',
        },
        {
          text: 'Username', value: 'username'
        },
        {
          text: 'Password', value: 'password',
        },
        {
          text: 'Admin', value: 'admin',
        },
        {
          text: 'Actions', value: 'actions'
        }
      ],
      users: [],
      search: '',
      editedIndex: -1,
      editedItem: {
        uuid: 0,
        name: '',
        username: '',
        password: '',
        isAdmin: false
      },
      defaultItem: {
        name: '',
        username: '',
        password: '',
        admin: false
      },
      created: false,
      currentUser: {},
      origin: window.location.protocol + '//' + window.location.hostname + ':8080',
    }
  },

  async created(){
    this.currentUser = (await axios.get(this.origin+'/users/'+window.sessionStorage.getItem("userId"))).data
    this.users = (await axios.get(this.origin+'/users')).data.filter(user => user.uuid !== this.currentUser.uuid)
  },

  methods: {
    editItem (item) {
      this.editedIndex = this.users.indexOf(item);
      this.editedItem = Object.assign({}, item);
    },

    async deleteItem (item) {
      if (confirm('Are you sure you want to delete this item?')){
        try{
          await axios.delete(this.origin+'/users/'+item.uuid)
          this.$root.$emit('DeviceCRUD refresh', 'list of users refreshed')
        }catch(e){
          alert('cannot delete user because it has devices')
        }
      }
      this.users = (await axios.get(this.origin+'/users')).data.filter(user => user.uuid !== this.currentUser.uuid)
    },

    async close() {
      this.created = false
      this.editedItem = Object.assign({}, this.defaultItem);
      this.editedIndex = -1;
      this.users = (await axios.get(this.origin+'/users')).data.filter(user => user.uuid !== this.currentUser.uuid)
    },

    addNew() {
      if (!this.created){
        this.created = true
        const addObj = Object.assign({}, this.defaultItem);
        this.users.unshift(addObj);
        this.editItem(addObj);
      }
    },

    async save () {
      if (this.editedIndex > -1) {
        if (this.created) {
          try{
            this.editedItem.isAdmin = this.editedItem.admin
            await axios.post(this.origin+'/users', this.editedItem)
            this.$root.$emit('DeviceCRUD refresh', 'list of users refreshed')
          }
          catch(e){
            alert('Username already used!')
          }
          this.created = false
        }
        else{
          try{
            this.editedItem.isAdmin = this.editedItem.admin
            await axios.put(this.origin+'/users', this.editedItem)
            this.$root.$emit('DeviceCRUD refresh', 'list of users refreshed')
          }catch(e){
            alert('Username already used!')
          }
        }
      }
      await this.close()
    },
  }
}
</script>

<style scoped>

</style>