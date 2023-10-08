import {createApp} from 'vue';
import AppComponent from './AppComponent.vue';
import {router} from './router';
import 'bootstrap/dist/css/bootstrap.css';
import './tailwind.css';


createApp(AppComponent).use(router).mount('#app');
