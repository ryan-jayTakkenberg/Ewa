import {createRouter, createWebHistory} from 'vue-router';
import PasswordResetComponent from "@/components/login/PasswordResetComponent.vue";
import LoginComponent from "@/components/login/LoginComponent.vue";
import NotFoundComponent from "@/components/NotFoundComponent.vue";
import ProductOverview from "@/components/product/admin/ProductOverview";
import ReportComponent from "@/components/manage/ReportComponent.vue";
import TeamsOverview from "@/components/team/TeamsOverview.vue";
import UsersOverview from "@/components/user/UsersOverview.vue";
import ProductOverviewViewer from "@/components/product/viewer/ProductOverviewViewer";
import ViewerOverviewComponent from "@/components/overview/ViewerOverviewComponent";
import AdminOverviewComponent from "@/components/overview/AdminOverviewComponent";
import OrdersOverview from "@/components/order/OrdersOverview.vue";
import ProjectOverview from "@/components/project/ProjectOverview.vue";
import WarehouseOverview from "@/components/warehouse/WarehouseOverview.vue";
import warehouseOverviewComponent from "@/components/warehouseView/warehouseOverviewComponent.vue";

const routes = [
    {path: '/', redirect: '/login'},
    {path: '/login', name: 'LOGIN', component: LoginComponent},
    {path: '/viewer-overview', name: 'VIEWER-OVERVIEW', component: ViewerOverviewComponent},
    {path: '/admin-overview', name: 'ADMIN-OVERVIEW', component: AdminOverviewComponent},
    {path: '/orders', component: OrdersOverview},
    {path: '/warehouses', component: WarehouseOverview},
    {path: '/projects', component: ProjectOverview},
    {path: '/teams', component: TeamsOverview},
    {path: '/products', component: ProductOverview},
    {path: '/productViewer', component: ProductOverviewViewer},
    {path: '/users', component: UsersOverview,},
    {path: '/reports', component: ReportComponent},
    {path: '/reset-password', component: PasswordResetComponent },
    {path: '/warehouseViewer', component: warehouseOverviewComponent},
    {path: '/:pathMatch(.*)*', component: NotFoundComponent}
];

export const router = createRouter({
    history: createWebHistory(),
    routes,
})
