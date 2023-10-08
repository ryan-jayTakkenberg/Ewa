<template>
  <NavBar v-if="isLoggedIn === false"></NavBar>
  <router-view ></router-view>
</template>

<script>
import Team from "@/models/team";
import User from "@/models/user";
import NavBar from "@/components/navigation/NavBar.vue";

// This only fetches the data accessible to the logged in user
// TODO should be placed at the login page if no session is present
(async () => {
  Team.teams = await Team.getDatabase();
  User.users = await User.getDatabase();
})();

export default {
  name: 'AppComponent',
  components: {NavBar},
  data(){
    return {
      isLoggedIn: false,
    }
  }
}
</script>

<style>

*, *::before, *::after {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Montserrat", sans-serif;
  /*outline: 1px solid red;*/
}

</style>

