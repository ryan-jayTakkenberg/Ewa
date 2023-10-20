import {createRouter, createWebHistory} from 'vue-router';

import LoginComponent from "@/login/LoginComponent.vue";
import OverviewComponent from "@/components/OverviewComponent.vue";
import WarehouseDetailComponent from "@/components/warehouse/WarehouseDetailComponent.vue";
import NotFoundComponent from "@/components/NotFoundComponent.vue";
import ProductDetailComponent from "@/components/product/ProductDetailComponent";
import ProjectDetailComponent from "@/components/project/ProjectDetailComponent.vue";
import ReportComponent from "@/components/manage/ReportComponent.vue";
import UsersOverview from "@/components/user/UsersOverview.vue";
import TeamsDetailComponent from "@/Teams/TeamsDetailComponent.vue";
import EditUserModal from "@/components/user/EditUserModal.vue";
import DeleteUserModal from "@/components/user/DeleteUserModal.vue";
import CreateUserModal from "@/components/user/CreateUserModal.vue";
import warehouseOverviewComponent from "@/components/warehouseView/warehouseOverviewComponent.vue";


const routes = [
    {path: '/', redirect: '/overview'},
    {path: '/login', component: LoginComponent},
    {path: '/overview', component: OverviewComponent},
    {path: '/warehouses', component: WarehouseDetailComponent},
    {path: '/projects', component: ProjectDetailComponent},
    {path: '/teams', component: TeamsDetailComponent},
    {path: '/products', component: ProductDetailComponent},
    {
        path: '/users',
        component: UsersOverview,
        children:
            [
                {path: 'create', component: CreateUserModal},
                {path: 'edit/:id', component: EditUserModal},
                {path: 'delete/:id', component: DeleteUserModal},
            ]
    },
    {path: '/reports', component: ReportComponent},
    {path: '/warehouseViewer', component: warehouseOverviewComponent},
    {path: '/:pathMatch(.*)*', component: NotFoundComponent}
];
export const router = createRouter({
    history: createWebHistory(),
    routes,
})
