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
import {getJWT} from "@/data";
import {ReportAdaptor} from "@/service/report-adaptor";
import Orders from "@/models/order";
import {AdminOverviewAdaptor} from "@/service/admin-overview-adaptor";
import {UserAdaptor} from "@/service/user-adaptor";
import {ProjectAdaptor} from "@/service/project-adaptor";

export default {
  name: 'App',
  components: {NavBar},
  data() {
    return {
      isLoggedIn: false,
      isSideBarExpanded: false,
      fetchedData: false,
    }
  },
  provide() {
    return {
      warehouseService: new WarehouseAdaptor(CONFIG.BACKEND_URL+"/api/warehouses"),
      projectService: new ProjectAdaptor(CONFIG.BACKEND_URL+"/api/projects"),
      teams: new TeamsAdaptor(CONFIG.BACKEND_URL+"api/teams"),
      reportService: new ReportAdaptor(CONFIG.BACKEND_URL+"/api/viewer-overview"),
      userService: new UserAdaptor(CONFIG.BACKEND_URL+"/api/users"),
      adminOverviewService: new AdminOverviewAdaptor(CONFIG.BACKEND_URL+"/api/adminview")
    }
  },
  methods: {
    updateSidebarState(isExpanded) {
      this.isSideBarExpanded = isExpanded;
    }
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
            Product.productInfos = productInfos;
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
  created() {
    const path = this.$router.currentRoute.value.path;
    const isResetPasswordRoute = path.startsWith('/');

    if (!getJWT() && !isResetPasswordRoute) {
      this.$router.push('/login');
    }
  }


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