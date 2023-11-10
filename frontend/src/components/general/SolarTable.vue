<script>
export default {
  name: "SolarTable",
  props: {
    columns: {
      type: Array,
      required: true,
    },
  }
}
</script>

<template>
  <table class="solar-table-container">
    <thead class="solar-table-header">
    <tr>
      <th scope="col" class="px-6 py-3"></th>
      <th v-for="(column, index) in columns" :key="index" class="px-6 py-3">{{ column }}</th>
    </tr>
    </thead>
    <tbody>

    <slot></slot>

    </tbody>
  </table>
</template>

<style scoped>
.solar-table-container {
  width: 100%;
  font-size: 0.875rem;
  line-height: 1.25rem;
  text-align: left;
  color: rgb(51, 51, 51);
}

.solar-table-header {
  color: rgb(55 65 81);
  text-transform: uppercase;
  background-color: rgb(248, 248, 248);
}
</style>
