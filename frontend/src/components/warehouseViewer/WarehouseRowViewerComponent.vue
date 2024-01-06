<template>
  <tr class="table-row" @click="selectWarehouse">
    <th scope="row" class="d-flex align-items-center px-6 py-4 text-gray-900 whitespace-nowrap">
      <div class="py-4">
        <div class="text-base font-semibold">{{ warehouse.name }}</div>
      </div>
    </th>
    <td class="px-6 py-4">{{ warehouse.city }}</td>
    <td class="px-6 py-4">
      <div class="flex items-center">{{ warehouse.address }}</div>
      <div class="font-normal text-gray-500">{{ warehouse.postalCode }}</div>
    </td>

    <td class="px-6 py-4">
      <div v-if="warehouse.teams && warehouse.teams.length > 0">
        <div v-for="team in warehouse.teams" :key="team.id">{{ team.name }}</div>
      </div>
      <div v-else>N/A</div>
    </td>
  </tr>
</template>

<script>

import Warehouse from "@/models/warehouse";

export default {
  name: "warehouseRowComponent",
  props: {
    warehouse: {
      type: Warehouse,
      required: true,
    },
  },

  data() {
    return {
      checked: this.isChecked,
      showProducts: false,
    };
  },

  methods: {

    selectWarehouse() {
      this.$emit("select-warehouse", this.warehouse);
    },

  },

}

</script>

<style scoped>

.table-row {
  background-color: white;
  border-bottom-width: 1px;
  cursor: pointer;
}

</style>
