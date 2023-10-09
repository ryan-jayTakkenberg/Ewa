import 'bootstrap/dist/css/bootstrap.css';
import { createApp } from 'vue';
import AppComponent from './AppComponent.vue';
import LoginComponent from "@/components/LoginComponent.vue";
import OverviewComponent from "@/components/OverviewComponent.vue";
import ManageComponent from '@/components/manage/ManageComponent.vue';
import NotFoundComponent from '@/components/NotFoundComponent.vue'
import { createRouter, createWebHashHistory } from "vue-router";
import ProductDetailComponent from "@/components/manage/ProductDetailComponent";
import ProjectEditComponent from "@/components/manage/ProjectEditComponent";
import WarehouseDetailComponent from "@/components/manage/WarehouseDetailComponent";
import TeamDetailComponent from "@/components/manage/TeamDetailComponent";

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {
            path: '/',
            component: LoginComponent
        },
        {
            path: '/overview',
            component: OverviewComponent
        },
        {
            path: '/manage',
            component: ManageComponent,
            children: [
                {
                    path: "product",
                    component: ProductDetailComponent
                },
                {
                    path: "project",
                    component: ProjectEditComponent
                },
                {
                    path: "warehouse",
                    component: WarehouseDetailComponent
                },
                {
                    path: "team",
                    component: TeamDetailComponent
                }
            ]
        },
        {
            // https://stackoverflow.com/questions/40193634/vue-router-redirect-on-page-not-found-404
            path: '/:pathMatch(.*)*',
            component: NotFoundComponent
        }
    ]
});

createApp(AppComponent).use(router).mount('#app')