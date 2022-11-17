<template>
  <v-container>
    <v-row>
      <v-col></v-col>
      <v-col>
        <v-form
            ref="form"
        >
          <v-text-field
              v-model="username"
              label="Username"
              required
          ></v-text-field>

          <v-text-field
              v-model="password"
              type="password"
              label="Password"
              required
          ></v-text-field>

          <v-btn
              color="success"
              class="mr-4"
              @click="login"
          >
            Login
          </v-btn>
        </v-form>
      </v-col>
      <v-col></v-col>
    </v-row>
  </v-container>
</template>

<script>

  import axios from "axios";

  export default {
    name: 'Login',
    data() {
      return {
        username: '',
        password: ''
      }
    },

    methods:{
      async login(){
        try{
          const result = await axios.post("http://localhost:8080/users/login", {
            username: this.username,
            password: this.password
          })
          window.sessionStorage.setItem("userId", result.data.uuid)
          console.log(result.data)
          if (result.data.admin) window.location.href = '/admin'
          else window.location.href = '/'

        }catch(e){
          alert('Invalid credentials')
        }
      }
    }
  }
</script>
