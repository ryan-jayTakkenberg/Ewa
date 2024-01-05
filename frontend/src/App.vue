<template>
  <div class="app-container">
    <app-navbar
        v-if="isLoggedIn"
        :sidebarIsExpanded="isSideBarExpanded"
        @sidebar-expanded="updateSidebarState"
    ></app-navbar>

    <div v-if="fetchedData || !isLoggedIn" class="router-view" :class="{ 'router-view-responsive': true }">
      <router-view></router-view>
    </div>

    <app-notification ref="notificationComponent"></app-notification>

  </div>
</template>

<script>

import {getAPI, responseOk} from "@/backend";
import {getJWT, isAdmin} from "@/data";
import Team from "@/models/team";
import User from "@/models/user";
import Product from "@/models/product";
import Project from "@/models/project";
import Warehouse from "@/models/warehouse";
import Orders from "@/models/order";
import NavBarComponent from "@/components/navigation/NavBarComponent.vue";
import NotificationComponent from "@/components/general/NotificationComponent.vue";
import {ProductAdaptor} from "@/service/product-adaptor";
import {OrderAdaptor} from "@/service/order-adaptor";
import {ProjectAdaptor} from "@/service/project-adaptor";
import {ReportAdaptor} from "@/service/report-adaptor";
import {WarehouseAdaptor} from "@/service/warehouse-adaptor";
import {TeamsAdaptor} from "@/service/teams-adaptor";
import {UserAdaptor} from "@/service/user-adaptor";

export default {
  name: 'App',
  components: {
    'app-navbar': NavBarComponent,
    'app-notification': NotificationComponent
  },

  data() {
    return {
      isLoggedIn: false,
      isSideBarExpanded: false,
      fetchedData: false,
      warehouseService: new WarehouseAdaptor(),
      projectService: new ProjectAdaptor(),
      teamsService: new TeamsAdaptor(),
      reportService: new ReportAdaptor(),
      productService: new ProductAdaptor(),
      userService: new UserAdaptor(),
      orderService: new OrderAdaptor(),
    }
  },

  provide() {

    return {
      orderService: this.orderService,
      warehouseService: this.warehouseService,
      projectService: this.projectService,
      teamsService: this.teamsService,
      reportService: this.reportService,
      userService: this.userService,
      productService: this.productService,
    }
  },

  created() {
    console.log('Configured routerGuard');

    const path = this.$router.currentRoute.value.path;
    const isResetPasswordRoute = path.startsWith('/');

    if (!getJWT() && !isResetPasswordRoute) {
      this.$router.push('/login');
    }
  },

  methods: {

    updateSidebarState(isExpanded) {
      this.isSideBarExpanded = isExpanded;
    },

  },

  watch: {
    async '$route'(to, from) {
      // Stop processing if the route hasn't changed
      if (from === to) {
        return;
      }

      // If the current route is '/reset-password', do nothing further
      if (to.path === '/reset-password') {
        return;
      }

      // Check user's login status
      this.isLoggedIn = responseOk(await getAPI("/api/authentication/status"));

      // Redirect to login if not logged in and not on the login page
      if (!this.isLoggedIn && to.path !== '/login') {
        this.$router.push('/login');
      }

      // Logic for fetching data if logged in
      if (this.isLoggedIn) {
        if (to.path === '/login') {
          this.$router.push(isAdmin() ? '/admin-overview' : '/viewer-overview');
        }
        if (!this.fetchedData) {
          try {
            const [teams, users, products, projects, warehouses, orders] = await Promise.all([
              Team.getDatabase(),
              this.userService.fetchAll(),
              this.productService.fetchAll(),
              Project.getDatabase(),
              Warehouse.getDatabase(),
              this.orderService.fetchAll(),
            ]);

            Team.teams = teams;
            User.users = users;
            Product.products = products;
            Project.projects = projects;
            Warehouse.warehouses = warehouses;
            Orders.orders = orders;

            this.fetchedData = true;
          } catch (error) {
            console.error(error);
          }
        }
      }
    }
  },

  beforeUnmount() {
    console.log('App.unmounted() has been called.')
    // this.theFetchInterceptor.unregister();
  },

}

</script>

<style>

*, *::before, *::after {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Montserrat", sans-serif, medium;
  /*outline: 1px solid red;*/
}

.app-container {
  display: flex;
  flex-direction: row;
  height: 100vh;
  width: 100vw;
}

.router-view {
  width: 100vw;
  height: 100%;
  margin-left: auto;
  overflow: auto;
}

@media only screen and (max-width: 750px) {

  .router-view-responsive {
    width: calc(100vw - 70px);
    margin-left: auto;
  }

}

</style>
