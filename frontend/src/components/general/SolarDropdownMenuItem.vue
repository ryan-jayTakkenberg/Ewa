<script>
export default {
  name: "SolarDropdownMenuItem",
  props: {
    textMenuItem: {
      type: String,
      required: true,
    },
    onClick: {
      type: Function,
      required: true,
    }
  }
}
</script>

<template>
  <li @click="onClick">
    <p class="menuItem">{{ textMenuItem }}</p>
  </li>
</template>

<style scoped>
li {
  border: 0;
  cursor: pointer;
}

.menuItem {
  display: block;
  padding: 0.5rem 1rem;
}

.menuItem:hover {
  color: white;
}
</style>
