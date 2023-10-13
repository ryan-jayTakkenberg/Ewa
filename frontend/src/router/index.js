import {createRouter, createWebHistory} from 'vue-router';

import LoginComponent from "@/components/LoginComponent.vue";
import OverviewComponent from "@/components/OverviewComponent.vue";
import WarehouseDetailComponent from "@/components/manage/WarehouseDetailComponent.vue";
import TeamDetailComponent from "@/components/manage/TeamDetailComponent.vue";
import NotFoundComponent from "@/components/NotFoundComponent.vue";
import ProductDetailComponent from "@/components/manage/ProductDetailComponent";
import ProjectDetailComponent from "@/components/manage/ProjectDetailComponent";
import ReportComponent from "@/components/manage/ReportComponent.vue";
import UsersOverview from "@/components/users/UsersOverview.vue";

const routes = [
    {path: '/', redirect: '/overview'},
    {path: '/login', component: LoginComponent},
    {path: '/overview', component: OverviewComponent},
    {path: '/warehouses', component: WarehouseDetailComponent},
    {path: '/projects', component: ProjectDetailComponent},
    {path: '/teams', component: TeamDetailComponent},
    {path: '/products', component: ProductDetailComponent},
    {path: '/users', component: UsersOverview},
    {path: '/reports', component: ReportComponent},
    {path: '/:pathMatch(.*)*', component: NotFoundComponent}
];
export const router = createRouter({
    history: createWebHistory(),
    routes,
})
