import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import TDesign from 'tdesign-vue-next';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

const vuetify = createVuetify({
  components,
  directives,
})

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)
app.use(TDesign);
app.use(ElementPlus)
app.use(vuetify)
app.mount('#app')
