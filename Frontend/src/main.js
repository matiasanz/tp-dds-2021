import Vue from 'vue'
import App from './App.vue'
import router from './Plugins/router'
import store from './Plugins/storage'

import * as VueGoogleMaps from 'vue2-google-maps'

Vue.use(VueGoogleMaps, {
  load: {
    key: 'AIzaSyDWtbvaL0K0XnkOv1R3bwEEeGJ9zhPjzy4'
    , libraries: 'places'
  }
})

Vue.config.productionTip = false

function capitalize(value){
    if (!value) return ''
    value = value.toString().replace('_', ' ')
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase()
}

Vue.filter('capitalize', capitalize)

Vue.filter('plural', value => capitalize(value)+'s')

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
