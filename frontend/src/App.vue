<template>
  <div class="app-container">
    <NavBar
        v-if="isLoggedIn === true"
        :sidebarIsExpanded="isSideBarExpanded"
        @sidebar-expanded="updateSidebarState"
    ></NavBar>

    <div class="router-view">
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
      isLoggedIn: true,
      isSideBarExpanded: false,
    }
  },
  methods: {
    updateSidebarState(isExpanded) {
      this.isSideBarExpanded = isExpanded;
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
  height: 100svh;
  width: 100svw;
}

.router-view {
  width: 100%;
  height: 100%;
}

@media screen and (min-width: 769px) {
  /* Adjust the width when sidebar is expanded */
  .router-view-collapsed {
    width: calc(100% - 200px);
    margin-left: auto;
  }

  .app-container {
    display: flex;
    flex-direction: row;
  }

}
</style>

