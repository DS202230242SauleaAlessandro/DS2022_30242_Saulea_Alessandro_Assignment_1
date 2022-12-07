<template>
  <div>
    <div v-if="currentUser.admin">
      <h1>Administration page</h1>
      <v-container>
        <v-row>
          <v-col>
            <UserCRUD></UserCRUD>
          </v-col>
          <v-col>
            <DeviceCRUD></DeviceCRUD>
          </v-col>
        </v-row>
        <v-row>
          <v-col></v-col>
          <v-col align="center" @click="goHome()"><v-btn text>Go to the main page</v-btn></v-col>
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

import userCRUD from "@/components/UserCRUD";
import deviceCRUD from "@/components/DeviceCRUD";
import axios from "axios";
import unauthorized from "@/components/Unauthorized";

export default {
  name: "Admin",
  components: {
    'UserCRUD': userCRUD,
    'DeviceCRUD': deviceCRUD,
    'Unauthorized': unauthorized
  },

  data(){
    return {
      currentUser: {}
    }
  },

  async created(){
    this.currentUser = (await axios.get('http://localhost:8080/users/'+window.sessionStorage.getItem("userId"))).data
  },

  methods: {
    goHome(){
      this.$router.push('.')
      this.$router.go(0)
    }
  }
}
</script>

<style scoped>

</style>