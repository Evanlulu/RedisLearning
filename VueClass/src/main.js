import Vue from 'vue'
import App from './App.vue'
import HmButton from './components/HmButton.vue'
Vue.config.productionTip = false

Vue.component('HmButton',HmButton);

new Vue({
  render: h => h(App),
}).$mount('#app')
