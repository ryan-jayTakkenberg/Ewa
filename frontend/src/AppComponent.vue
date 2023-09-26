<template>
  <router-view></router-view>
  <nav-bar-component></nav-bar-component>
</template>

<script>

import NavBarComponent from "@/components/NavBarComponent.vue";

export default {
  name: 'AppComponent',
  components: {
    NavBarComponent

  }
}
</script>

<style>
/** {*/
/*  outline: 1px solid red;*/
/*}*/
* {
  margin: 0;
  padding: 0;
}
</style>
