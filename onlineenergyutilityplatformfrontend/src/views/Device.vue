<template>
  <div v-if="currentDevice.userDTO.uuid === currentUser.uuid">
    <h1>{{ currentDevice.description }}</h1>
    <v-container>
      <v-row>
        <v-col><v-date-picker v-model="selectedDay" @change="showChart"></v-date-picker></v-col>
        <v-col>
          <h3>Hourly energy consumption</h3>
          <line-chart :data="charData" xtitle="Hour" ytitle="Consumption (kW)"></line-chart>
        </v-col>
      </v-row>
    </v-container>
  </div>
  <div v-else><unauthorized></unauthorized></div>
</template>

<script>

import unauthorized from "@/components/Unauthorized";
import axios from "axios";

export default {
  name: "Device",
  components: {
    'unauthorized': unauthorized,
  },

  data() {
    return {
      measurements: [],
      currentUser: {},
      currentDevice: {userDTO: {}},
      deviceId: '',
      selectedDay: null,
      charData: {
      },
      origin: window.location.protocol + '//' + window.location.hostname + ':8080'
    }
  },

  async created() {
    this.currentUser = (await axios.get(this.origin+'/users/' + window.sessionStorage.getItem("userId"))).data
    this.deviceId = this.$route.path.split('/')[2]
    this.currentDevice = (await axios.get(this.origin+"/devices/" + this.deviceId)).data
  },

  methods: {
    async showChart(){
      this.measurements = (await axios.get(this.origin+`/measurements/bydevice&date?deviceId=${this.deviceId}&date=${this.selectedDay}`)).data
      this.charData = {}
      this.measurements.forEach(measurement => {
        const date = new Date(measurement.timestamp);
        const hours = date.getHours();
        const minutes = date.getMinutes();
        const key = (hours < 10 ? '0' : '') + hours + ':' + (minutes < 10 ? '0' : '') + minutes;
        this.charData[key] = measurement.consumption
      })
    }
  }
}
</script>

<style scoped>

</style>