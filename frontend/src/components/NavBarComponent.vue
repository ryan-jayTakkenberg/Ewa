<template>
  <div>


    <span @click="openNav" class="material-symbols-outlined">menu</span>
    <div id="mySidenav" :style="{ width: sidebarWidth }" class="sidenav">
      <div class="loggedInAs">
        <h1 class="textLoggedIn">You are logged in as:<strong> {{ loggedIn }}</strong></h1>
        <button @click="loggedInUpdate">change</button>
      </div>
      <div class="navbarComponents">
        <a class="closebtn" @click="closeNav">&times;</a>
        <router-link to="/overview"><span class="material-symbols-outlined">overview</span>Overview</router-link>
        <router-link to="/manage"><span class="material-symbols-outlined">settings</span>Manage</router-link>
        <a><span class="material-symbols-outlined">warehouse</span>Warehouse</a>
        <a><span class="material-symbols-outlined">tactic</span>Project</a>
        <a><span class="material-symbols-outlined">groups</span>Teams</a>
      </div>
      <div class="notification">
        <a>Notification</a>
        <i class="material-symbols-outlined">notifications</i>
        <span class="count">{{ listArray.length }}</span>
      </div>

      <div class="forecasting">
        <li v-for="item in listArray" :key="item">{{ item }}</li>
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
      sidebarWidth: "0px",
      notificationCount: 5,
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
      alert("je bent uitgelogd")
    },
    loggedInUpdate(){
      this.loggedIn = this.loggedIn === "admin" ? "guest" : "admin";
    }
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

.forecasting {
  height: auto;
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


.notification {
  border-top: 1px solid black;
  padding-bottom: 10px;
  display: flex;
  flex-direction: row;
  align-items: center; /* Hiermee wordt de afbeelding verticaal gecentreerd */
}

.sidenav {
  height: 100%; /* 100% Full-height */
  width: 0; /* 0 width - change this with JavaScript */
  position: fixed; /* Stay in place */
  z-index: 1; /* Stay on top */
  top: 0; /* Stay at the top */
  left: 0;
  background-color: #E5E5E5; /* Black*/
  overflow-x: hidden; /* Disable horizontal scroll */
  padding-top: 60px; /* Place content 60px from the top */
  transition: 0.5s; /* 0.5 second transition effect to slide in the sidenav */
}

/* The navigation menu links */
.sidenav a {
  padding: 8px 8px 8px 32px;
  text-decoration: none;
  font-size: 15px;
  color: #818181;
  display: block;
  transition: 0.3s;
}
.textLoggedIn{
  padding: 6px 6px 6px 28px;
  text-decoration: none;
  font-size: 15px;
  color: #818181;
  display: block;

}

/* When you mouse over the navigation links, change their color */
.sidenav a:hover {
  color: #f1f1f1;
}

/* Position and style the close button (top right corner) */
.sidenav .closebtn {
  position: absolute;
  top: 0;
  right: 25px;
  font-size: 36px;
  margin-left: 50px;
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