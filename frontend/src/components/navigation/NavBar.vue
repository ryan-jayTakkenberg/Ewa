<script>
/* TODO EMIT FOR sidebarIsExpanded FOR INTERACTION RESPONSIVENESS OTHER COMPONENTS*/
/* (NOT ON MOBILE, SO MENU OVERFLOWS)*/

import logoExpanded from '/static/images/solar_sedum_logo.svg'
import logoCollapsed from '/static/images/solar_sedum_logo_small.svg'

export default {
  name: 'NavBar',
  data() {
    return {
      activePage: 'overview', //Default overview
      userRole: 'admin', // viewer or Admin
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
  <div :class="{'navbar': true, 'expanded': sidebarIsExpanded}">
    <div class="navbar-container">
      <div class="nav-header">
        <img
            class="nav-header-logo"
            alt=" logo"
            :src="sidebarIsExpanded ? logoCollapsed  : logoExpanded"
        />

        <div class="menu-toggle btn" @click="toggleSidebar">
          <span class="material-symbols-outlined">
            {{ sidebarIsExpanded ? 'keyboard_double_arrow_right' : 'keyboard_double_arrow_left' }}</span>
        </div>

      </div>
      <router-link
          to="/overview"
          class="nav-button relative"
          :class="{ active: isPageActive('overview') }"
          @click="handleNavClick('overview')"
      >
        <span class="material-symbols-outlined nav-icon">overview_key</span>
        <p class="nav-text">Overview</p>
        <span class="notification-indicator bg-danger-subtle text-danger">3</span>
      </router-link>

      <router-link
          v-if="(userRole === 'viewer')"
          to="/test"
          class="nav-button"
          :class="{ active: isPageActive('teams') }"
          @click="handleNavClick('teams')"
      >
        <span class="material-symbols-outlined nav-icon">groups</span>
        <p class="nav-text">My Teams</p>
      </router-link>

      <router-link
          v-if="(userRole === 'viewer')"
          to="/test"
          class="nav-button"
          :class="{ active: isPageActive('reports') }"
          @click="handleNavClick('reports')"
      >
        <span class="material-symbols-outlined nav-icon">report</span>
        <p class="nav-text">My Reports</p>
        <span class="notification-indicator">3</span>
      </router-link>


      <router-link
          v-if="(userRole === 'admin')" to="/warehouses" class="nav-button"
          :class="{ active: isPageActive('warehouses') }"
          @click="handleNavClick('warehouses')"
      >
        <span class="material-symbols-outlined nav-icon">warehouse</span>
        <p class="nav-text">Warehouses</p>
      </router-link>

      <router-link
          v-if="(userRole === 'admin')" to="/projects" class="nav-button"
          :class="{ active: isPageActive('projects') }"
          @click="handleNavClick('projects')"
      >
        <span class="material-symbols-outlined nav-icon">assignment</span>
        <p class="nav-text">Projects</p>
      </router-link>

      <router-link
          v-if="(userRole === 'admin')" to="/teams" class="nav-button"
          :class="{ active: isPageActive('teams') }"
          @click="handleNavClick('teams')"
      >
        <span class="material-symbols-outlined nav-icon">groups</span>
        <p class="nav-text">Teams</p>
      </router-link>

      <router-link
          v-if="(userRole === 'admin')" to="/products" class="nav-button"
          :class="{ active: isPageActive('products') }"
          @click="handleNavClick('products')"
      >
        <span class="material-symbols-outlined nav-icon">inventory_2</span>
        <p class="nav-text">Products</p>
      </router-link>

      <router-link
          v-if="(userRole === 'admin')" to="/users" class="nav-button"
          :class="{ active: isPageActive('users') }"
          @click="handleNavClick('users')"
      >
        <span class="material-symbols-outlined nav-icon">person</span>
        <p class="nav-text">Users</p>
      </router-link>

      <router-link
          v-if="(userRole === 'admin')"
          to="/reports"
          class="nav-button"
          :class="{ active: isPageActive('reports') }"
          @click="handleNavClick('reports')"
      >
        <span class="material-symbols-outlined nav-icon">report</span>
        <p class="nav-text">Reports</p>
        <span class="notification-indicator bg-danger-subtle text-danger">3</span>
      </router-link>

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
  color: darkred;
  background-color: palevioletred;
  border-radius: 9999px;
}

.navbar {
  display: flex;
  position: fixed;
  left: 0;
  z-index: 50;
  width: 180px;
  height: 100vh;
  background-color: #fff;
  border-right: 1px solid #e5e5e5;
  padding: 0;
}

.navbar-container {
  display: flex;
  flex-direction: column;
  background-color: #fff;
  overflow: hidden;
  height: 100%;
}

.nav-header {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.nav-header-logo {
  padding: 1rem;
}

.nav-button,
.log-out-button {
  display: inline-flex;
  flex-direction: row;
  align-items: center;
  transition: background-color 0.3s;
  justify-content: flex-start;
  padding: 1.0rem 1.5rem;
}

.log-out-button {
  margin-top: auto;
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
  color: #5B2E18;
}

.nav-text {
  font-size: 0.875rem;
  color: #5B2E18;
  padding-left: 1rem;
}

.nav-button:hover .nav-text {
  color: #C7D02C;
}

.nav-button.active .nav-text {
  color: #c7d02c;
}

.navbar.expanded {
  width: 70px;
}

.navbar.expanded .nav-button {
  padding-left: 1.5rem;
}

.navbar.expanded .nav-text {
  display: none;
}

.navbar.expanded .notification-indicator {
  position: relative;
  left: -20px;
  top: -22px;
}

.menu-toggle {
  display: flex;
  justify-content: center;
  padding: 1rem;
}

.menu-toggle:hover {
  color: #c7d02c;
  transform: translateX(0.5rem);
}

</style>
