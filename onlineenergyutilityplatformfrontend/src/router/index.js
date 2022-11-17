import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/views/Login";
import Home from "@/views/Home";
import Admin from "@/views/Admin";
import AdminDevice from "@/views/AdminDevice";
import Device from "@/views/Device";

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '',
    name: 'home',
    component: Home
  },
  {
    path: '/admin',
    name: 'admin',
    component: Admin
  },
  {
    path: '/admin/devices/:id',
    name: 'admin device',
    component: AdminDevice
  },
  {
    path: '/devices/:id',
    name: 'device',
    component: Device
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
