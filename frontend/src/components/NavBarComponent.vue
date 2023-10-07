<template>
  <div>

    <span @click="openNav" class="material-symbols-outlined">menu</span>
    <div id="mySidenav" :style="{ width: sidebarWidth }" class="sidenav">
      <div class="loggedInAs">
        <h1 class="textLoggedIn">You are logged in as:<strong> Admin </strong></h1>

      </div>
      <div class="navbarComponents">
        <a class="closebtn" @click="closeNav">&times;</a>
        <router-link id="routerLink" to="/overview"><span class="material-symbols-outlined">overview</span>Overview</router-link>
        <router-link id="routerLink" to="/manage"><span class="material-symbols-outlined">settings</span>Manage</router-link>
        <a id="routerLink" ><span class="material-symbols-outlined">warehouse</span>Warehouse</a>
        <a id="routerLink" ><span class="material-symbols-outlined">tactic</span>Project</a>
        <a id="routerLink" ><span class="material-symbols-outlined">groups</span>Teams</a>
      </div>

      <div class="notification-bell" @click="toggleNotifications">
        <i class="material-symbols-outlined">notifications</i>
        <div class="notification-count">{{ listArray.length }}</div>
      </div>

      <div v-if="showNotifications" class="notification-window">
        <h2>Notifications</h2>
        <ul>
          <li v-for="item in listArray" :key="item">{{ item }}</li>
        </ul>
      </div>

      <div class="log-out">
        <button class="logOutbtn" @click="logOut">Log out</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "NavBarComponent",
  data() {
    return {
      showNotifications: false,
      sidebarWidth: "250px",
      loggedIn:"admin",
      listArray: ["Zonnepanelen zijn bijna op",
         "hout is bijna op"
        ,"personeels tekort warehouse 4",

      ]
    };
  },
  methods: {
    openNav() {
      this.sidebarWidth = "250px";
    },
    closeNav() {
      this.sidebarWidth = "0px";
    }, logOut() {
      this.$router.push('/');
    }, toggleNotifications() {
      this.showNotifications = !this.showNotifications;
    },
  },
};
</script>

<style>
.logOutbtn {
  padding: 10px;
  margin: 10px;

}

.logOutbtn:hover {
  background-color: #818181;
}

.log-out {
  border-top: 1px solid black;
}

img {
  padding: 2px 2px 2px;
  width: 30px;
  height: 30px;
  align-items: end;
}

.navbarComponents {
  border-top: 1px solid black;
}
li{
  padding: 5px;
}


.notification-bell {
  cursor: pointer;
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.notification-bell i {
  font-size: 24px;
  margin-right: 10px; /* Ruimte tussen het bel-icoontje en de teller */
}

.notification-count {
  background-color: red;
  color: white;
  border-radius: 50%;
  padding: 3px 8px;
  font-size: 14px;
}
.notification-window {
  border-top: 1px solid black;
  padding: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.notification-window h2 {
  margin: 0 0 10px;
  font-size: 18px;
}
.sidenav {
  height: 100%; /* 100% Full-height */
  width: 0; /* 0 width - change this with JavaScript */
  background-color: #f5f5f5; /* Black*/
  overflow-x: hidden; /* Disable horizontal scroll */
  padding-top: 30px; /* Place content 60px from the top */
  transition: 0.5s; /* 0.5 second transition effect to slide in the sidenav */
}

/* The navigation menu links */
.sidenav a {
  padding: 8px 8px 8px 32px;
  text-decoration: none;
  font-size: 15px;
  color: black;
  display: block;
  transition: 0.3s;
}
.textLoggedIn{
  padding: 3px 6px 6px 10px;
  text-decoration: none;
  font-size: 15px;
  color: #818181;
  display: block;


}
#routerLink:hover{
  font-size: 18px;

}
/* When you mouse over the navigation links, change their color */
.sidenav a:hover {
  color: #818181;
}
.sidenav .closebtn {
  top: 0;
  right: 25px;
  font-size: 36px;
  margin-left: 25px;
}
.forecasting ul {
  list-style-type: none;
  padding: 0;
}

.forecasting li {
  padding: 5px;
}



/* On smaller screens, where height is less than 450px, change the style of the sidenav (less padding and a smaller font size) */
@media screen and (max-height: 450px) {
  .sidenav {
    padding-top: 15px;
  }

  .sidenav a {
    font-size: 18px;
  }
}

/* https://fonts.google.com/icons?selected=Material+Symbols+Outlined:home:FILL@0;wght@400;GRAD@0;opsz@24 */
.material-symbols-outlined {
  font-variation-settings: 'FILL' 0,
  'wght' 400,
  'GRAD' 0,
  'opsz' 24
}

</style>