<template>
  <tr class="table-row">
    <!-- Warehouse Item -->
    <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap">
      <div class="pl-3">
        <div class="text-base font-semibold">{{ warehouse.name }}</div>
        <div class="font-normal text-gray-500">{{ warehouse.id }}</div>
      </div>
    </th>
    <td class="px-6 py-4">{{ warehouse.city }}</td>
    <td class="px-6 py-4">
      <div class="flex items-center">{{ warehouse.address }}</div>
      <div class="font-normal text-gray-500">{{ warehouse.postalCode }}</div>
    </td>

    <td>{{ warehouse.teams ? warehouse.teams.map(team => team.name).join(', ') : 'N/A' }}</td>

    <td class="px-6 py-4">
      <div @click="showMore" class="edit-user-btn">{{ showedit ? 'Close details' : 'See details' }}</div>
    </td>
  </tr>

  <tr v-if="showedit">
    <td colspan="4">
      <ShowDetailWarehouseComponent
          :is-open="true"
          @close-detail="closeDetail"
      ></ShowDetailWarehouseComponent>
    </td>
  </tr>
</template>

<script>
import Warehouse from "@/models/warehouse";
import ShowDetailWarehouseComponent from "@/components/warehouseViewer/WarehouseDetailViewerComponent.vue";


export default {
  name: "warehouseRowComponent",
  components: { ShowDetailWarehouseComponent },
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
      showedit: false,
    };
  },
  methods: {
    showMore() {
      this.showedit = !this.showedit;
    },
    toggleCheckbox() {
      this.$emit('toggle-checkbox', !this.checked);
    },
    closeDetail() {
      this.showedit = false;
    },
  },
};
</script>

<style scoped>
.edit-user-btn {
  font-weight: 500;
  color: rgb(37, 99, 235);
  cursor: pointer;
}

.edit-user-btn:hover {
  text-decoration-line: underline;
}

.table-row {
  background-color: white;
  border-bottom-width: 1px;
}

.table-row:hover {
  background-color: rgb(249, 250, 251);
}
</style>
