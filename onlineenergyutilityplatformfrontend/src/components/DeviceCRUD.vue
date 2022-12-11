<template>
  <v-card>
    <v-card-title>Devices</v-card-title>
    <v-data-table :headers="headers" :items="devices" :search="search" class="elevation-1" fixed-header height="350px">
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
      <template v-slot:item.description="{ item }">
        <v-text-field v-model="editedItem.description" dense single-line v-if="item.uuid === editedItem.uuid"></v-text-field>
        <span v-else>{{item.description}}</span>
      </template>
      <template v-slot:item.address="{ item }">
        <v-text-field v-model="editedItem.address" dense single-line v-if="item.uuid === editedItem.uuid"></v-text-field>
        <span v-else>{{item.address}}</span>
      </template>
      <template v-slot:item.maxConsumption="{ item }">
        <v-text-field v-model="editedItem.maxConsumption" dense single-line v-if="item.uuid === editedItem.uuid" type="number"></v-text-field>
        <span v-else>{{item.maxConsumption}}</span>
      </template>
      <template v-slot:item.userDTO="{ item }">
        <v-combobox v-model="editedItem.userDTO" dense single-line v-if="item.uuid === editedItem.uuid" :items="users"
                    @change="renderKey=1-renderKey"
                    :key="renderKey"
                    :item-text="(o) => (o)['name']"></v-combobox>
        <span v-else>{{item.userDTO.name}}</span>
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
        <v-btn text @click="select(item)">Select</v-btn>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import axios from "axios";

export default {
  name: "DeviceCRUD",
  data(){
    return {
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
          text: 'User', value: 'userDTO',
        },
        {
          text: 'Actions', value: 'actions'
        }
      ],
      renderKey: 0,
      users: [],
      devices: [],
      search: '',
      editedIndex: -1,
      editedItem: {
        uuid: '',
        description: '',
        address: '',
        maxConsumption: 0,
        userDTO: {}
      },
      defaultItem: {
        description: '',
        address: '',
        maxConsumption: 0,
        userDTO: {}
      },
      created: false,
      origin: window.location.protocol + '//' + window.location.hostname + ':8080',
    }
  },

  async created(){
    // https://www.npmjs.com/package/vuejs-datetimepicker
    this.users = (await axios.get(this.origin + '/users')).data
    this.devices = (await axios.get(this.origin + '/devices')).data
    this.$root.$on('DeviceCRUD refresh', async () => this.users = (await axios.get(this.origin + '/users')).data)
  },

  methods: {
    editItem (item) {
      this.editedIndex = this.devices.indexOf(item);
      this.editedItem = Object.assign({}, item);
    },

    async deleteItem (item) {
      if (confirm('Are you sure you want to delete this item?')){
        try{
          await axios.delete(this.origin + '/devices/' + item.uuid)
        }catch(e){
          console.log('cannot delete')
        }
      }
      this.devices = (await axios.get(this.origin + '/devices')).data
    },

    async close() {
      this.created = false
      this.editedItem = Object.assign({}, this.defaultItem);
      this.editedIndex = -1;
      this.devices = (await axios.get(this.origin + '/devices')).data
    },

    addNew() {
      if (!this.created){
        this.created = true
        const addObj = Object.assign({}, this.defaultItem);
        addObj.userDTO = this.users[0]
        this.devices.unshift(addObj);
        this.editItem(addObj);
      }
    },

    async save () {
      if (this.editedIndex > -1) {
        if (this.created) {
          this.editedItem.maxConsumption = parseInt(this.editedItem.maxConsumption)
          try{
            await axios.post(this.origin + '/devices', this.editedItem)
          }
          catch(e){
            alert('Invalid data')
          }
          this.created = false
        }
        else{
          console.log(this.editedItem)
          try{
            await axios.put(this.origin + '/devices', this.editedItem)
          }catch(e){
            alert('Invalid data!')
          }
        }
      }
      await this.close()
    },

    select(item){
      this.$router.push('admin/devices/'+item.uuid)
    }
  }
}
</script>

<style scoped>

</style>