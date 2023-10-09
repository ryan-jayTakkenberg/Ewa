import {createRouter, createWebHistory} from 'vue-router';

import LoginComponent from "@/components/LoginComponent.vue";
import OverviewComponent from "@/components/OverviewComponent.vue";
import WarehouseDetailComponent from "@/components/manage/WarehouseDetailComponent.vue";
import TeamDetailComponent from "@/components/manage/TeamDetailComponent.vue";
import NotFoundComponent from "@/components/NotFoundComponent.vue";
import ProductDetailComponent from "@/components/manage/ProductDetailComponent";
import ProjectDetailComponent from "@/components/manage/ProjectDetailComponent";
import UsersComponent from "@/components/UsersComponent.vue";

const routes = [
    {path: '/', redirect: '/login'},
    {path: '/login', component: LoginComponent},
    {path: '/overview', component: OverviewComponent},
    {path: '/warehouses', component: WarehouseDetailComponent},
    {path: '/projects', component: ProjectDetailComponent},
    {path: '/teams', component: TeamDetailComponent},
    {path: '/products', component: ProductDetailComponent},
    {path: '/users', component: UsersComponent},
    {path: '/reports', component: ProductDetailComponent},
    {path: '/:pathMatch(.*)*', component: NotFoundComponent}
];
export const router = createRouter({
    history: createWebHistory(),
    routes,
})
