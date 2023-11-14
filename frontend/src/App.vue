<template>
  <div class="app-container">
    <NavBar
        v-if="isLoggedIn"
        :sidebarIsExpanded="isSideBarExpanded"
        @sidebar-expanded="updateSidebarState"
    ></NavBar>

    <div class="router-view" :class="{ 'router-view-responsive': true }">
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
import {getKey} from "@/data";

export default {
  name: 'App',
  components: {NavBar},
  data() {
    return {
      isLoggedIn: getKey(),
      isSideBarExpanded: false,
      fetchedData: false,
    }
  },
  provide() {
    return {
      warehouseService: new WarehouseAdaptor(CONFIG.BACKEND_URL+"/warehouses"),
      teams: new TeamsAdaptor(CONFIG.BACKEND_URL+"api/teams")
    }
  },
  methods: {
    updateSidebarState(isExpanded) {
      this.isSideBarExpanded = isExpanded;
    }
  },
  watch: {
    '$route'(from, to) {
      if (from === to) {
        return;
      }

      this.isLoggedIn = getKey();
      if (!this.isLoggedIn) {
        this.$router.push('/login');
      } else if (!this.fetchedData) {
        // This only fetches the data accessible to the logged-in user
        (async () => {
          try {
            const [teams, users, products, projects, warehouses] = await Promise.all([
              Team.getDatabase(),
              User.getDatabase(),
              Product.getDatabase(),
              Project.getDatabase(),
              Warehouse.getDatabase(),
            ]);

            Team.teams = teams;
            User.users = users;
            Product.products = products;
            Project.projects = projects;
            Warehouse.warehouses = warehouses;

            this.fetchedData = true;
          } catch (error) {
            console.error(error);
          }
        })();
      }
    }
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

