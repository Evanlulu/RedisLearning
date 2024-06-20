import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import '@mdi/font/css/materialdesignicons.css' // Ensure you are using css-loader


const app = createApp(App);
app.use(router);
app.use(vuetify);
app.provide('router', router);
app.mount('#app');
