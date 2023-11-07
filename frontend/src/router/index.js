import {createRouter, createWebHistory} from 'vue-router';

import LoginComponent from "@/components/login/LoginComponent.vue";
import OverviewComponent from "@/components/OverviewComponent.vue";
import WarehouseDetailComponent from "@/components/warehouse/WarehouseDetailComponent.vue";
import NotFoundComponent from "@/components/NotFoundComponent.vue";
import ProductOverview from "@/components/product/ProductOverview";
import ProjectDetailComponent from "@/components/project/ProjectDetailComponent.vue";
import ReportComponent from "@/components/manage/ReportComponent.vue";
import TeamsDetailComponent from "@/components/team/TeamsDetailComponent.vue";
import warehouseOverviewComponent from "@/components/warehouseView/warehouseOverviewComponent.vue";

// User
import UsersOverview from "@/components/user/UsersOverview.vue";
import CreateUserModal from "@/components/user/user-modals/CreateUserModal.vue";
import UpdateUserModal from "@/components/user/user-modals/UpdateUserModal.vue";
import DeleteUserModal from "@/components/user/user-modals/DeleteUserModal.vue";
import DeleteMultipleUsersModal from "@/components/user/user-modals/DeleteMultipleUsersModal.vue";

const routes = [
    {path: '/', redirect: '/overview'},
    {path: '/login', component: LoginComponent},
    {path: '/overview', component: OverviewComponent},
    {path: '/warehouses', component: WarehouseDetailComponent},
    {path: '/projects', component: ProjectDetailComponent},
    {path: '/teams', component: TeamsDetailComponent},
    {path: '/products', component: ProductOverview},
    {
        path: '/users',
        component: UsersOverview,
        children:
            [
                {path: 'create', component: CreateUserModal},
                {path: 'edit/:id', component: UpdateUserModal},
                {path: 'delete/:id', component: DeleteUserModal},
                {path: 'delete-users', component: DeleteMultipleUsersModal},
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
