<script>
export default {
  name: "SolarDropdownMenuItem",
  emits: ["item-click"],
  props: {
    textMenuItem: {
      type: String,
      required: true,
    },
  },
  methods: {
    handleClick() {
      this.$emit("item-click");
    },
  }
}
</script>

<template>
  <li>
    <p class="menuItem" @click="handleClick">{{ textMenuItem }}</p>
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
