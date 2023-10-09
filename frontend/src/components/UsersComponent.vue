<template>
  <TitleComponent page-title="Users"></TitleComponent>
  <SearchBarComponent search-bar-text="Search Users"></SearchBarComponent>

</template>

<script>

import TitleComponent from "@/general/TitleComponent.vue";
import SearchBarComponent from "@/general/SearchBarComponent.vue";

export default {
  name: "UsersComponent",
  components: {
    TitleComponent,
    SearchBarComponent
  },
}
</script>

<style scoped>
</style>
