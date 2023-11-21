import {createRouter, createWebHistory} from 'vue-router';

import LoginComponent from "@/components/login/LoginComponent.vue";
import NotFoundComponent from "@/components/NotFoundComponent.vue";
import ProductOverview from "@/components/product/admin/ProductOverview";
import ReportComponent from "@/components/manage/ReportComponent.vue";
import TeamsOverview from "@/components/team/TeamsOverview.vue";
import UsersOverview from "@/components/user/UsersOverview.vue";
import UpdateWarehouse from "@/components/warehouse/warehousePopUps/UpdateWarehouse";
import ProductOverviewViewer from "@/components/product/viewer/ProductOverviewViewer";
import ViewerOverviewComponent from "@/components/overview/ViewerOverviewComponent";
import AdminOverviewComponent from "@/components/overview/AdminOverviewComponent";
import OrdersOverview from "@/components/order/OrdersOverview.vue";
import ProjectOverview from "@/components/project/ProjectOverview.vue";
import WarehouseOverview from "@/components/warehouse/WarehouseOverview.vue";
import warehouseOverview from "@/components/warehouse/WarehouseOverview.vue";

const routes = [
    {path: '/', redirect: '/login'},
    {path: '/login', component: LoginComponent},
    {path: '/viewer-overview', component: ViewerOverviewComponent},
    {path: '/admin-overview', component: AdminOverviewComponent},
    {path: '/orders', component: OrdersOverview},
    {path: '/warehouses', component: WarehouseOverview,
        children:
            {path: ":id", component: UpdateWarehouse}
    },
    {path: '/projects', component: ProjectOverview},
    {path: '/teams', component: TeamsOverview},
    {path: '/products', component: ProductOverview},
    {path: '/productViewer', component: ProductOverviewViewer},
    {path: '/users', component: UsersOverview,},
    {path: '/reports', component: ReportComponent},
    {path: '/warehouseViewer', component: warehouseOverview},
    {path: '/:pathMatch(.*)*', component: NotFoundComponent}
];

export const router = createRouter({
    history: createWebHistory(),
    routes,
})
