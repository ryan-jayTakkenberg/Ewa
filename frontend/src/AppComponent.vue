<template>
  <router-view></router-view>
<!--  <nav-bar-component></nav-bar-component>-->
</template>

<script>

// import NavBarComponent from "@/components/NavBarComponent.vue";


export default {
  name: 'AppComponent',
  components: {
    // NavBarComponent
  }
}
</script>

<style>

*, *::before, *::after {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Montserrat", sans-serif;
  //outline: 1px solid red;
}

</style>
