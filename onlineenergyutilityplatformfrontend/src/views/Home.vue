<template>
  <div class="about">
    <h1>Welcome to the Online Energy Utility Platform!</h1>
    <v-container>
      <v-row>
        <v-col>
          <v-card>
            <v-card-title>Devices</v-card-title>
            <v-data-table :headers="headers" :items="devices" :search="search" class="elevation-1" fixed-header height="350px">
              <template v-slot:top>
                <div class="d-flex w-100">
                  <v-text-field v-model="search" append-icon="mdi-magnify" label="Search" dense outlined></v-text-field>
                </div>
              </template>
              <template v-slot:item.uuid="{ item }"><span>{{item.uuid}}</span></template>
              <template v-slot:item.name="{ item }"><span>{{item.name}}</span></template>
              <template v-slot:item.description="{ item }"><span>{{item.description}}</span></template>
              <template v-slot:item.address="{ item }"><span>{{item.address}}</span></template>
              <template v-slot:item.maxConsumption="{ item }"><span>{{item.maxConsumption}}</span></template>
              <template v-slot:item.actions="{ item }"><v-btn text @click="select(item)">Select</v-btn></template>
            </v-data-table>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Home",

  data(){
    return {
      currentUser: {},
      devices: [],
      headers: [
        {
          text: 'UUID', value: 'uuid',
        },
        {
          text: 'Description', value: 'description',
        },
        {
          text: 'Address', value: 'address'
        },
        {
          text: 'Max consumption', value: 'maxConsumption',
        },
        {
          text: 'Actions', value: 'actions'
        }
      ],
      search: '',
      origin: window.location.protocol + '//' + window.location.hostname + ':8080',
    }
  },
  async created(){
    this.currentUser = (await axios.get(this.origin+"/users/"+window.sessionStorage.getItem("userId"))).data
    this.devices = (await axios.get(this.origin+`/users/${this.currentUser.uuid}/devices`)).data
  },

  methods:{
    select(item){
      this.$router.push('devices/'+item.uuid)
    }
  }
}
</script>

<style scoped>

</style>