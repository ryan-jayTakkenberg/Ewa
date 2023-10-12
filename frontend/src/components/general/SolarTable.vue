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
      <th v-for="(column, index) in columns" :key="index" class="px-6 py-3">
        {{ column }}
      </th>
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
  font-size: 0.875rem /* 14px */;
  line-height: 1.25rem /* 20px */;
  text-align: left;
  color: rgb(107 114 128);
}

.solar-table-header {
  color: rgb(55 65 81);
  text-transform: uppercase;
  background-color: rgb(249 250 251);
}
</style>
