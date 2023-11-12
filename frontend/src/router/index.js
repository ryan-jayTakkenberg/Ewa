import {createRouter, createWebHistory} from 'vue-router';

import LoginComponent from "@/components/login/LoginComponent.vue";
import OverviewComponent from "@/components/OverviewComponent.vue";
import WarehousePageComponent from "@/components/warehouse/WarehousePageComponent.vue";
import NotFoundComponent from "@/components/NotFoundComponent.vue";
import ProductOverview from "@/components/product/ProductOverview";
import ProjectDetailComponent from "@/components/project/ProjectDetailComponent.vue";
import ReportComponent from "@/components/manage/ReportComponent.vue";
import TeamsDetailComponent from "@/components/team/TeamsDetailComponent.vue";
import warehouseOverviewComponent from "@/components/warehouseView/warehouseOverviewComponent.vue";
import UsersOverview from "@/components/user/UsersOverview.vue";
import UpdateWarehouse from "@/components/warehouse/warehousePopUps/UpdateWarehouse";

const routes = [
    {path: '/', redirect: '/overview'},
    {path: '/login', component: LoginComponent},
    {path: '/overview', component: OverviewComponent},
    {path: '/warehouses', component: WarehousePageComponent,
        children: [
            {path: ":id", component: UpdateWarehouse}
        ]
    },
    {path: '/projects', component: ProjectDetailComponent},
    {path: '/teams', component: TeamsDetailComponent},
    {path: '/products', component: ProductOverview},
    {path: '/users', component: UsersOverview,},
    {path: '/reports', component: ReportComponent},
    {path: '/warehouseViewer', component: warehouseOverviewComponent},
    {path: '/:pathMatch(.*)*', component: NotFoundComponent}
];

export const router = createRouter({
    history: createWebHistory(),
    routes,
})
