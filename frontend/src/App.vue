<template>
  <div class="app-container">
    <NavBar
        v-if="isLoggedIn"
        :sidebarIsExpanded="isSideBarExpanded"
        @sidebar-expanded="updateSidebarState"
    ></NavBar>

    <div class="router-view" :class="{ 'router-view-responsive': isLoggedIn }">
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

// This only fetches the data accessible to the logged in user
// // TODO should be placed at the login page if no session is present
(async () => {
  Team.teams = await Team.getDatabase();
  User.users = await User.getDatabase();
  Product.products = await Product.getDatabase();
  Project.projects = await Project.getDatabase();
  Warehouse.warehouses = await Warehouse.getDatabase();
})();

export default {
  name: 'App',
  components: {NavBar},
  data() {
    return {
      isLoggedIn: getKey(),
      isSideBarExpanded: false,
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
    '$route'() {
      console.log(getKey());
      this.isLoggedIn = getKey();
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

