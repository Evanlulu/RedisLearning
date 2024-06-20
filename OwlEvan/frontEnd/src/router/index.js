import { createRouter, createWebHistory } from "vue-router";
import Login from "../components/Login.vue";
import DashBoard from "../components/DashBoard.vue";
import Table from "../components/Table.vue";

const routes = [
    {
        path: "/",
        name: "Login",
        component: Login
    },
    {
        path: '/dashboard',
        name: 'DashBoard',
        component: DashBoard
    },
    {
        path: '/table',
        name: 'Table',
        component: Table
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router