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
import WarehouseOverviewAdminOverview from "@/components/warehouse/WarehouseOverview.vue";
import WarehouseOverviewViewerComponent from "@/components/warehouseViewer/WarehouseOverviewViewerComponent.vue";

const routes = [
    {path: '/', redirect: '/login'},
    {path: '/login', name: 'LOGIN', component: LoginComponent},
    {path: '/viewer-overview', name: 'VIEWER-OVERVIEW', component: ViewerOverviewComponent},
    {path: '/admin-overview', name: 'ADMIN-OVERVIEW', component: AdminOverviewComponent},
    {path: '/orders', name: 'ORDERS', component: OrdersOverview},
    {path: '/warehouses', name: 'WAREHOUSES-ADMIN', component: WarehouseOverviewAdminOverview},
    {path: '/warehouseViewer', name: 'WAREHOUSES-VIEWER', component: WarehouseOverviewViewerComponent},
    {path: '/projects', name: 'PROJECTS', component: ProjectOverview},
    {path: '/teams', name: 'TEAMS', component: TeamsOverview},
    {path: '/products', name: 'PRODUCTS-ADMIN', component: ProductOverview},
    {path: '/productViewer', name: 'PRODUCTS-VIEWER', component: ProductOverviewViewer},
    {path: '/users', name: 'USERS', component: UsersOverview,},
    {path: '/reports', name: 'REPORTS', component: ReportComponent},
    {path: '/reset-password', name: 'RESET-PASSWORD', component: PasswordResetComponent },
    {path: '/:pathMatch(.*)*', name: 'NOT-FOUND', component: NotFoundComponent}
];

export const router = createRouter({
    history: createWebHistory(),
    routes,
})
