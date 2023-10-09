<template>
  <header>
    <TitleComponent page-title="Users"></TitleComponent>
    <SearchBarComponent placeholder-text="Search Users" class="ml-auto"></SearchBarComponent>
    <button type="button" class="search-button fs-4">
      <span class="material-symbols-outlined add-user-icon">person_add</span>

      Add User
    </button>
  </header>
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

header {
  flex-direction: row;
  display: flex;
  padding: 1rem;
}

.search-button {
  color: white;
  background-color: #c7d02c;;
  font-weight: 500;
  border-radius: 0.5rem /* 8px */;
  line-height: 1.25rem /* 20px */;
  padding: 1rem 2rem;
  margin-left: 1rem;
}

.add-user-icon {
  position: relative;
  top: 2px;
  font-size: 1rem;
}


</style>
