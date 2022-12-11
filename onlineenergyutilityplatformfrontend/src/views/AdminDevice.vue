<template>
  <div>
    <div v-if="currentUser.admin">
      <h1>Device {{ deviceId}}</h1>
      <v-container>
        <v-row>
          <v-col></v-col>
          <v-col>
            <v-card>
              <v-card-title>Add measure</v-card-title>
              <h4>Timestamp</h4>
              <datetime v-model="newTimestamp"></datetime>
              <v-text-field v-model="newItem.consumption" label="Consumption" dense single-line type="number"></v-text-field>
              <v-btn
                  color="primary"
                  class="ml-2 white--text"
                  @click="addNew">
                <v-icon dark>mdi-plus</v-icon>Add
              </v-btn>
            </v-card>
          </v-col>
          <v-col></v-col>
        </v-row>
        <v-row>
          <v-col></v-col>
          <v-col>
            <v-card>
              <v-card-title>Measures</v-card-title>
              <v-data-table :headers="headers" :items="measurements" class="elevation-1" fixed-header height="350px">
                <template v-slot:item.timestamp="{ item }"><span>{{item.timestamp}}</span></template>
                <template v-slot:item.consumption="{ item }">
                  <v-text-field v-model="editedItem.consumption" dense single-line v-if="item.timestamp === editedItem.timestamp" type="number"></v-text-field>
                  <span v-else>{{item.consumption}}</span>
                </template>
                <template v-slot:item.actions="{ item }">
                  <div v-if="item.timestamp === editedItem.timestamp">
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
          </v-col>
          <v-col></v-col>
        </v-row>
        <v-row>
          <v-col></v-col>
          <v-col align="center" @click="$router.push('..')"><v-btn text>Go to the admin page</v-btn></v-col>
          <v-col></v-col>
        </v-row>
      </v-container>
    </div>
    <div v-else>
      <unauthorized></unauthorized>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import datetime from 'vuejs-datetimepicker';
import Unauthorized from "@/components/Unauthorized";

export default {
  name: "AdminDevice",
  components: {
    'datetime': datetime,
    'unauthorized': Unauthorized},
  data(){
    return {
      headers: [
        {
          text: 'Timestamp', value: 'timestamp',
        },
        {
          text: 'Consumption', value: 'consumption',
        },
        {
          text: 'Actions', value: 'actions'
        }
      ],
      measurements: [],
      currentDevice: {},
      editedItem: {
        device: { uuid: 0},
        timestamp: '2022-01-01T00:00:00',
        consumption: 0,
      },
      defaultItem: {
        device: { uuid: 0},
        timestamp: '2022-01-01T00:00:00',
        consumption: 0,
      },
      deviceId: '',
      newItem: {
        device: { uuid: 0},
        timestamp: '',
        consumption: 0,
      },
      newTimestamp:'2022-01-01 00:00:00',
      currentUser: {},
      origin: window.location.protocol + '//' + window.location.hostname + ':8080',
    }
  },

  async created() {
    this.currentUser = (await axios.get(this.origin+'/users/'+window.sessionStorage.getItem("userId"))).data
    this.deviceId = this.$route.path.split('/')[3]
    this.currentDevice = (await axios.get(this.origin+"/devices/"+this.deviceId)).data
    this.measurements = (await axios.get(this.origin+"/measurements/bydevice/"+this.deviceId)).data
    this.editedItem.device.uuid = this.currentDevice.uuid
  },

  methods:{
    editItem (item) {
      this.editedIndex = this.measurements.indexOf(item);
      this.editedItem = Object.assign({}, item);
    },

    async deleteItem (item) {
      if (confirm('Are you sure you want to delete this item?')){
        try{
          await axios.delete(this.origin+`/measurements?deviceId=${this.deviceId}&timestamp=${item.timestamp}`)
        }catch(e){
          alert('cannot delete')
        }
      }
      this.measurements = (await axios.get(this.origin+"/measurements/bydevice/"+this.deviceId)).data
    },

    async close() {
      this.editedItem = Object.assign({}, this.defaultItem);
      this.editedIndex = -1;
      this.measurements = (await axios.get(this.origin+"/measurements/bydevice/"+this.deviceId)).data
    },

    async addNew() {
      this.newItem.device.uuid = this.currentDevice.uuid
      this.newItem.timestamp = new Date(this.newTimestamp)
      this.newItem.consumption = parseInt(this.newItem.consumption)
      console.log(this.newItem)
      try{
        await axios.post(this.origin+'/measurements', this.newItem)
      }
      catch(e){
        alert('Invalid data!')
      }
      this.measurements = (await axios.get(this.origin+"/measurements/bydevice/"+this.deviceId)).data
    },

    async save () {
      if (this.editedIndex > -1) {
        try{
          await axios.put(this.origin+'/measurements', this.editedItem)
        }catch(e){
          alert('Invalid data!')
        }
      }
      await this.close()
    },
  }
}
</script>

<style scoped>

</style>