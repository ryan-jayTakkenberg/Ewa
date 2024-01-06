<template>
  <tr class="table-row">
    <th scope="row" class="d-flex align-items-center px-6 py-4 text-gray-900 whitespace-nowrap">
      <div class="pl-3">
        <div class="text-base font-semibold">{{ warehouse.name }}</div>
      </div>
    </th>
    <td class="px-6 py-4">{{ warehouse.city }}</td>
    <td class="px-6 py-4">
      <div class="flex items-center">{{ warehouse.address }}</div>
      <div class="font-normal text-gray-500">{{ warehouse.postalCode }}</div>
    </td>

    <td class="px-6 py-4 teams">
      <div v-if="warehouse.teams && warehouse.teams.length > 0">
        <div v-for="team in warehouse.teams" :key="team.id">{{ team.name }}</div>
      </div>
      <div v-else>N/A</div>
    </td>
  </tr>
</template>

<script>
import Warehouse from "@/models/warehouse";
import ShowDetailWarehouseComponent from "@/components/warehouseViewer/WarehouseDetailViewerComponent.vue";


export default {
  name: "warehouseRowComponent",
  props: {
    warehouse: {
      type: Warehouse,
      required: true,
    },
    isChecked: {
      type: Boolean,
      default: false,
      required: true,
    },
  },

  data() {
    return {
      checked: this.isChecked,
      // showedit: false,
    };
  },

  methods: {},

}

</script>

<style scoped>

.table-row {
  background-color: white;
  border-bottom-width: 1px;
}

.teams {
  display: flex;
  flex-direction: column;
}

</style>
