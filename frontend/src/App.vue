<template>
  <div class="app-container">
    <NavBar
        v-if="isLoggedIn"
        :sidebarIsExpanded="isSideBarExpanded"
        @sidebar-expanded="updateSidebarState"
    ></NavBar>

    <div v-if="fetchedData || !isLoggedIn" class="router-view" :class="{ 'router-view-responsive': true }">
      <router-view></router-view>
    </div>

    <NotificationComponent ref="notificationComponent"></NotificationComponent>

  </div>
</template>

<script>

import Team from "@/models/team";
import User from "@/models/user";
import Product from "@/models/product";
import Project from "@/models/project";
import Warehouse from "@/models/warehouse";
import NavBar from "@/components/navigation/NavBar.vue";
import {WarehouseAdaptor} from "@/service/warehouse-adaptor";
import {TeamsAdaptor} from "@/service/teams-adaptor";
import CONFIG from "@/app-config";
import {getAPI, responseOk} from "@/backend";
import {getJWT, isAdmin} from "@/data";
import {ReportAdaptor} from "@/service/report-adaptor";
import Orders from "@/models/order";
import {AdminOverviewAdaptor} from "@/service/admin-overview-adaptor";
import {UserAdaptor} from "@/service/user-adaptor";
import {ProjectAdaptor} from "@/service/project-adaptor";
// import {FetchInterceptor} from "@/service/fetch-interceptor";
// import {SessionService} from "@/service/session-service";
// import {shallowReactive} from "vue";
import NotificationComponent from "@/components/general/NotificationComponent.vue";

export default {
  name: 'App',
  components: {NotificationComponent, NavBar},

  data() {
    return {
      isLoggedIn: false,
      isSideBarExpanded: false,
      fetchedData: false,
    }
  },

  provide() {

    // create a singleton reactive service tracking the authorisation data of the session
    // this.theSessionService = shallowReactive(
    //     new SessionService(CONFIG.BACKEND_URL+'/authentication', CONFIG.JWT_STORAGE_ITEM));
    // this.theFetchInterceptor =
    //     new FetchInterceptor(this.theSessionService,*/ this.$router);

    return {
      warehouseService: new WarehouseAdaptor(CONFIG.BACKEND_URL+"/api/warehouses"),
      projectService: new ProjectAdaptor(CONFIG.BACKEND_URL+"/api/projects"),
      teamsService: new TeamsAdaptor(CONFIG.BACKEND_URL+"api/teams"),
      reportService: new ReportAdaptor(CONFIG.BACKEND_URL+"/api/viewer-overview"),
      userService: new UserAdaptor(CONFIG.BACKEND_URL+"/api/users"),
      adminOverviewService: new AdminOverviewAdaptor(CONFIG.BACKEND_URL+"/api/adminview"),
      // sessionService: this.theSessionService,
    }
  },

  created() {
    console.log('Configured routerGuard');
    // this.$router.beforeEach(this.routerGuard);

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

    // routerGuard(to,from) {
    //
    //   this.isLoggedIn = responseOk(getAPI("/api/authentication/status"));
    //   if (to.name === 'LOGIN') {
    //     if (this.isLoggedIn && isAdmin()) {
    //       console.log("Intercepted route from '" + from.name + "' to '" + to.name + "'");
    //       return '/admin-overview';
    //     } else if (getJWT() && !isAdmin()) {
    //       console.log("Intercepted route from '" + from.name + "' to '" + to.name + "'");
    //       return '/viewer-overview';
    //     }
    //   }
    //
    //   if (to.name === 'OVERVIEW') {
    //     if (this.isLoggedIn && isAdmin()) {
    //       console.log("Intercepted route from '" + from.name + "' to '" + to.name + "'");
    //       return '/admin-overview';
    //     } else if (getJWT() && !isAdmin()) {
    //       console.log("Intercepted route from '" + from.name + "' to '" + to.name + "'");
    //       return '/viewer-overview';
    //     }
    //   }
    //
    // }
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
            const [teams, users, productInfos, projects, warehouses, orders] = await Promise.all([
              Team.getDatabase(),
              User.getDatabase(),
              Product.getDatabase(),
              Project.getDatabase(),
              Warehouse.getDatabase(),
              Orders.getDatabase(),
            ]);

            Team.teams = teams;
            User.users = users;
            Product.products = productInfos;
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
    this.theFetchInterceptor.unregister();
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

body{
  overflow: hidden;
}

@media only screen and (max-width: 750px) {
  .router-view-responsive {
    width: calc(100vw - 70px);
    margin-left: auto;
  }
}
</style>