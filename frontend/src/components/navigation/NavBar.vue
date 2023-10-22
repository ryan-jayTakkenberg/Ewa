<script>

import logoExpanded from '/static/images/solar_sedum_logo.svg'
import logoCollapsed from '/static/images/solar_sedum_logo_small.svg'

export default {
  name: 'NavBar',
  data() {
    return {
      activePage: 'overview', // Default overview page
      userRole: 'viewer', // Viewer or Admin role
      sidebarIsExpanded: false,
      logoExpanded: logoExpanded,
      logoCollapsed: logoCollapsed
    }
  },
  methods: {
    handleNavClick(page) {
      this.activePage = page
    },
    logOut() {
      // TODO actually log out
      this.$router.push('/login');
    },
    isPageActive(page) {
      return this.activePage === page
    },
    toggleSidebar() {
      this.sidebarIsExpanded = !this.sidebarIsExpanded;
      this.$emit('sidebar-expanded', this.sidebarIsExpanded); // Use for adjusting sizing of page when sidebar is collapsed
    }
  },
}
</script>
<template>

  <div :class="{'navbar': true, 'collapsed': sidebarIsExpanded}">
    <div class="nav-header">
      <img class="nav-header-logo" alt=" logo" :src="sidebarIsExpanded ? logoCollapsed  : logoExpanded"/>
      <div class="sideBarToggleContainer" @click="toggleSidebar">
        <span :class="{'material-symbols-outlined logoExpanded': sidebarIsExpanded,
             'material-symbols-outlined logoCollapsed': !sidebarIsExpanded}">start</span>
      </div>
    </div>

    <div class="navigation">
      <div class="nav-items">
        <router-link
            to="/overview"
            class="nav-button relative"
            :class="{ active: isPageActive('overview') }"
            @click="handleNavClick('overview')">
          <span class="material-symbols-outlined nav-icon">overview_key</span>
          <p class="nav-text">Overview</p>
          <span class="notification-indicator bg-danger-subtle text-danger">3</span>
        </router-link>

        <router-link
            v-if="(userRole === 'viewer')"
            to="/warehouseViewer"
            class="nav-button"
            :class="{ active: isPageActive('teams') }"
            @click="handleNavClick('teams')">
          <span class="material-symbols-outlined nav-icon">warehouse</span>
          <p class="nav-text">Warehouse</p>
        </router-link>

        <router-link
            v-if="(userRole === 'viewer')"
            to="/test"
            class="nav-button"
            :class="{ active: isPageActive('reports') }"
            @click="handleNavClick('reports')">
          <span class="material-symbols-outlined nav-icon">report</span>
          <p class="nav-text">My Reports</p>
          <span class="notification-indicator">3</span>
        </router-link>

        <router-link
            v-if="(userRole === 'admin')" to="/warehouses" class="nav-button"
            :class="{ active: isPageActive('warehouses') }"
            @click="handleNavClick('warehouses')">
          <span class="material-symbols-outlined nav-icon">warehouse</span>
          <p class="nav-text">Warehouses</p>
        </router-link>

        <router-link
            v-if="(userRole === 'admin')" to="/projects" class="nav-button"
            :class="{ active: isPageActive('projects') }"
            @click="handleNavClick('projects')">
          <span class="material-symbols-outlined nav-icon">assignment</span>
          <p class="nav-text">Projects</p>
        </router-link>

        <router-link
            v-if="(userRole === 'admin')" to="/teams" class="nav-button"
            :class="{ active: isPageActive('teams') }"
            @click="handleNavClick('teams')">
          <span class="material-symbols-outlined nav-icon">groups</span>
          <p class="nav-text">Teams</p>
        </router-link>

        <router-link
            v-if="(userRole === 'admin')" to="/products" class="nav-button"
            :class="{ active: isPageActive('products') }"
            @click="handleNavClick('products')">
          <span class="material-symbols-outlined nav-icon">inventory_2</span>
          <p class="nav-text">Products</p>
        </router-link>

        <router-link
            v-if="(userRole === 'admin')" to="/users" class="nav-button"
            :class="{ active: isPageActive('users') }"
            @click="handleNavClick('users')">
          <span class="material-symbols-outlined nav-icon">manage_accounts</span>
          <p class="nav-text">Users</p>
        </router-link>
      </div>

      <div class="log-out-button" @click="logOut()">
        <span class="material-symbols-outlined nav-icon">logout</span>
        <p class="nav-text">Log out</p>
      </div>

    </div>
  </div>
</template>

<style scoped>

.notification-indicator {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 0.75rem;
  height: 0.75rem;
  margin-left: 0.75rem;
  padding: 0.75rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  color: white;
  background-color: palevioletred;
  border-radius: 9999px;
}

.navbar {
  display: flex;
  flex-direction: column;
  z-index: 1;
  width: 15svw;
  height: 100svh;
  background-color: #fff;
  border-right: 1px solid #e5e5e5;
  transition: 0.3s ease-in-out;
}

.nav-header {
  display: flex;
  align-items: center;
  flex-direction: row;
  justify-content: space-between;
  padding: 1rem;
}

.nav-header-logo {
  height: 10svh;
  width: 125px;
}

.sideBarToggleContainer {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  width: 50px;
  border-radius: 5px;
  cursor: pointer;
}

.logoExpanded {
  transition: 0.2s ease-in-out;
}

.logoCollapsed {
  transform: rotate(180deg);
  transition: 0.2s ease-in-out;
}

.sideBarToggleContainer:hover {
  background: #f5f5f5;
}

.navigation {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
}

.nav-button,
.log-out-button {
  display: flex;
  flex-direction: row;
  align-items: center;
  transition: background-color 0.3s;
  padding: 1.0rem 1.5rem;
  width: 100%;
  cursor: pointer;
}

.log-out-button:hover .nav-text,
.log-out-button:hover .nav-icon {
  color: #c7d02c;
}

.log-out-button:hover {
  background-color: #f9fafb;
}

.nav-button:hover {
  background-color: #f9fafb;
}

.nav-button:hover .nav-icon {
  color: #C7D02C;
}

.nav-button.active {
  border-left: #C7D02C 4px solid;
  background-color: #f9fafb;
  border-top: 0;
}

.nav-button.active .nav-icon {
  color: #c7d02c;
}

.nav-icon {
  width: 1.5rem;
  height: 1.5rem;
  color: #222;
  margin-right: 1rem;
}

.nav-text {
  font-size: 0.875rem;
  font-weight: 500;
  color: #222;
  width: 100%;
  max-height: 20px;
}

.nav-button:hover .nav-text {
  color: #C7D02C;
}

.nav-button.active .nav-text {
  color: #c7d02c;
}

.navbar.collapsed {
  width: 70px;
}

.navbar.collapsed .nav-button {
  padding-left: 1.5rem;
}

.navbar.collapsed .nav-text {
  display: none;
}

.navbar.collapsed .notification-indicator {
  position: relative;
  left: -20px;
  top: -22px;
}

@media only screen and (max-width: 1500px) {

  .navbar {
    width: 20svw;
  }

}

@media only screen and (max-width: 1250px) {

  .navbar {
    width: 25svw;
  }

}

@media only screen and (max-width: 1100px) {

  .navbar {
    width: 30svw;
  }

}

@media only screen and (max-width: 1050px) {

  .navbar {
    width: 35svw;
  }

}

@media only screen and (max-width: 850px) {

  .navbar {
    width: 40svw;
  }

}

@media only screen and (max-width: 750px) {

  .navbar {
    position: absolute;;
    width: 100svw;
  }

}

</style>
