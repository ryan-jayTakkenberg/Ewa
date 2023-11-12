<template>
<solarTitle class="title" page-title="Warehouse"></solarTitle>
  <div class="body">
    <div class="contentBox">
      <div class="contentHeader">
        <SolarSearchbar place-holder="Search For Warehouses"></SolarSearchbar>
        <SolarButton class="ml-auto" button-text="Add Warehouse"></SolarButton>
      </div>
      <SolarTable :columns="['Name', 'Address', 'Postal code', 'Action']">
        <tr class="tableRow" v-for="warehouse in warehouses" :key="warehouse.id">
          <td class="w-4 p-4">
            <div class="flex items-center">
              <input v-show="false"
                     type="checkbox"
                     class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded">
            </div>
          </td>
          <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap">
            <div class="pl-3">
              <div class="text-base font-semibold">{{ warehouse.name }}</div>
              <div class="font-normal text-gray-500">{{ warehouse.city }}</div>
            </div>
          </th>
          <td class="px-6 py-4">{{warehouse.address}}</td>
          <td class="px-6 py-4">{{warehouse.postalCode}}</td>
          <td>
            <div class="editWarehouseButton">Edit Warehouse</div>
            <div class="deleteWarehouseButton">Delete Warehouse</div>
          </td>
        </tr>
      </SolarTable>
    </div>
  </div>
</template>

<script>
import SolarTitle from "@/components/general/SolarTitle";
import SolarSearchbar from "@/components/general/SolarSearchbar";
import SolarButton from "@/components/general/SolarButton";
import SolarTable from "@/components/general/SolarTable";
import Warehouse from "@/models/warehouse";

export default {
  name: "warehousePageComponent",
  components: {
    SolarTitle,
    SolarSearchbar,
    SolarButton,
    SolarTable,
  },
  data() {
    return {
      warehouses: [],
      warehouseId: 2000,
      selectedWarehouse: null,
    };
  },
  created() {
    const amountOfWarhouses = 6;

    for (let i = 0; i < amountOfWarhouses; i++){
      const newWarehouse = Warehouse.createSampleOffer(this.warehouseId);
      this.warehouses.push(newWarehouse);
      this.warehouseId += Math.floor(Math.random() * 9 + 1);
    }
  }
}
</script>

<style scoped>
.title {
  flex-direction: row;
  display: flex;
  padding-left: 1rem;
  padding-top: 1rem;
}

.body {
  position: relative;
  overflow-x: auto;
}

.contentBox{
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 1rem;
  padding-left: 1rem;
  background-color: white;
}

.contentHeader{
  display: flex;
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}

.tableRow{
  background-color: white;
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
</style>