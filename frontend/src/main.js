import { createApp } from 'vue';
import App from './App.vue';
import { router } from './router';
import './theme/style.css'
import './tailwind.css';

createApp(App).use(router).mount('#app');