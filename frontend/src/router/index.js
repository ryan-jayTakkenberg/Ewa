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
import EditUserModal from "@/components/users/EditUserModal.vue";
import DeleteUserModal from "@/components/users/DeleteUserModal.vue";
import CreateUserModal from "@/components/users/CreateUserModal.vue";
import warehouseOverviewComponent from "@/components/warehouseView/warehouseOverviewComponent.vue";

const routes = [
    {path: '/', redirect: '/overview'},
    {path: '/login', component: LoginComponent},
    {path: '/overview', component: OverviewComponent},
    {path: '/userOverview', component: UserOverviewComponent},
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
