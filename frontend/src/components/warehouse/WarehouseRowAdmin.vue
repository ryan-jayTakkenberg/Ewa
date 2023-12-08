<template>
  <tr class="tableRow">
    <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap">
      <div class="pl-3">
        <div class="text-base font-semibold">{{ this.warehouse.name }}</div>
        <div class="font-normal text-gray-500">{{ this.warehouse.city }}</div>
      </div>
    </th>
    <td class="px-6 py-4">{{this.warehouse.address}}</td>
    <td class="px-6 py-4">{{this.warehouse.postalCode}}</td>
    <td>
      <div class="showInventoryButton" @click="this.showDetails = !this.showDetails" >{{ showDetails ? 'Hide Inventory' : 'Show Inventory' }}</div>
      <div class="editWarehouseButton" @click="openUpdateWarehouse()" >Edit Warehouse</div>
      <div class="deleteWarehouseButton" @click="deleteWarehouse()">Delete Warehouse</div>
    </td>
  </tr>
  <SolarTable :columns="['Product', 'Amount']" v-if="this.showDetails" class="detail-warehouse">
    <tr class="tableRow" v-for="(product, index) in warehouse.products" :key="index">
      <td class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap pl-8">{{ product['product']['name'] }}</td>
      <td class="px-6 py-4">{{ product.amount }}</td>
    </tr>
  </SolarTable>
</template>

<script>
import Warehouse from "@/models/warehouse";
import SolarTable from "@/components/general/SolarTable";

export default {
  name: "WarehouseRowAdmin",
  components: {
    SolarTable,
  },
  props: {
    warehouse: Warehouse
  },
  data() {
    return {
      showDetails: false,
    };
  },
  methods: {
    openUpdateWarehouse() {
      this.$emit('openUpdateWarehouse', this.warehouse);
    },
    deleteWarehouse() {
      this.$emit('deleteWarehouse', this.warehouse);
    },
  },
}
</script>

<style scoped>
.tableRow{
  background-color: white;
  border-top-width: 1px;
}

.tableRow:last-child{
  border-bottom-width: 1px;
}

.tableRow:hover{
  background-color: rgb(249 250 251);
}

.editWarehouseButton{
  font-weight: 500;
  color: rgb(37 99 235);
  cursor: pointer;
}

.editWarehouseButton:hover{
  text-decoration-line: underline;
}

.deleteWarehouseButton{
  font-weight: 500;
  color: red;
  cursor: pointer;
}

.deleteWarehouseButton:hover{
  text-decoration-line: underline;
}

.showInventoryButton{
  font-weight: 500;
  color: #c7d02c;
  cursor: pointer;
}

.showInventoryButton:hover{
  text-decoration-line: underline;
}

.detail-warehouse {
  border: 1px solid #ccc;
  padding: 10px;
  background-color: #fff;
}
</style>