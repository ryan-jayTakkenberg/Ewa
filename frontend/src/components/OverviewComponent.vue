<template>

  <div class="main">

    <NavBarComponent></NavBarComponent>

    <div class="overviewContainer">

      <div>lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum</div>

    </div>

  </div>

</template>

<script>

import NavBarComponent from "@/components/NavBarComponent.vue";

export default {
  name: "OverviewComponent",
  components: {
    NavBarComponent,
  },
};
</script>


<style scoped>


.main {
  display: flex;
  height: auto;
  width: 100svw;
}

.overviewContainer {
  min-height: 100svh;
  width: 100%;
  background: aquamarine; /* test */
}

</style>