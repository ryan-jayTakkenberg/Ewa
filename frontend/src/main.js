import { createApp } from 'vue';
import AppComponent from './AppComponent.vue';
import LoginComponent from '@/components/LoginComponent.vue';
import NotFoundComponent from '@/components/NotFoundComponent.vue'
import { createRouter, createWebHashHistory } from "vue-router";

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {
            path: '/',
            component: LoginComponent
        },
        {
            // https://stackoverflow.com/questions/40193634/vue-router-redirect-on-page-not-found-404
            path: '/:pathMatch(.*)*',
            component: NotFoundComponent
        }
    ]
});

createApp(AppComponent).use(router).mount('#app')