import {createRouter, createWebHistory} from 'vue-router';

import LoginComponent from "@/login/LoginComponent.vue";
import OverviewComponent from "@/components/OverviewComponent.vue";
import UserOverviewComponent from "@/components/overview/UserOverviewComponent.vue"
import WarehouseDetailComponent from "@/components/manage/WarehouseDetailComponent.vue";
import NotFoundComponent from "@/components/NotFoundComponent.vue";
import ProductDetailComponent from "@/components/manage/ProductDetailComponent";
import ProjectDetailComponent from "@/components/projects/ProjectDetailComponent.vue";
import ReportComponent from "@/components/manage/ReportComponent.vue";
import UsersOverview from "@/components/users/UsersOverview.vue";
import TeamsDetailComponent from "@/Teams/TeamsDetailComponent.vue";

const routes = [
    {path: '/', redirect: '/overview'},
    {path: '/login', component: LoginComponent},
    {path: '/overview', component: OverviewComponent},
    {path: '/userOverview', component: UserOverviewComponent},
    {path: '/warehouses', component: WarehouseDetailComponent},
    {path: '/projects', component: ProjectDetailComponent},
    {path: '/teams', component: TeamsDetailComponent},
    {path: '/products', component: ProductDetailComponent},
    {path: '/users', component: UsersOverview},
    {path: '/reports', component: ReportComponent},
    {path: '/:pathMatch(.*)*', component: NotFoundComponent}
];
export const router = createRouter({
    history: createWebHistory(),
    routes,
})
