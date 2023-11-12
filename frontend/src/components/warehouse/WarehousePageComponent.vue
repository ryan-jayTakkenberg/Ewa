<template>
<solarTitle class="title" page-title="Warehouse"></solarTitle>
  <div class="body">
    <div class="contentBox">
      <div class="contentHeader">
        <SolarSearchbar place-holder="Search For Warehouses"></SolarSearchbar>
        <SolarButton class="ml-auto" button-text="Add Warehouse" @click="showCreateWarehouse = true"></SolarButton>
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
            <div class="editWarehouseButton" @click="openUpdateWarehouse(warehouse)" >Edit Warehouse</div>
            <div class="deleteWarehouseButton" @click="deleteWarehouse(warehouse)">Delete Warehouse</div>
          </td>
        </tr>
      </SolarTable>
    </div>
  </div>
  <CreateWarehouse
    v-if="showCreateWarehouse"
    @close-pop-up="closePopUp"
    @create-warehouse="createWarehouse"
  />
  <UpdateWarehouse
    v-if="showUpdateWarehouse"
    :warehouse="selectedWarehouse"
    @close-pop-up="closePopUp"
    @update-warehouse="updateWarehouse"
  />

</template>

<script>
import SolarTitle from "@/components/general/SolarTitle";
import SolarSearchbar from "@/components/general/SolarSearchbar";
import SolarButton from "@/components/general/SolarButton";
import SolarTable from "@/components/general/SolarTable";
import CreateWarehouse from "@/components/warehouse/warehousePopUps/CreateWarehouse";
import UpdateWarehouse from "@/components/warehouse/warehousePopUps/UpdateWarehouse";

export default {
  name: "warehousePageComponent",
  inject: ['warehouseService'],
  components: {
    UpdateWarehouse,
    SolarTitle,
    SolarSearchbar,
    SolarButton,
    SolarTable,
    CreateWarehouse,
  },
  data() {
    return {
      warehouses: [],
      warehouseId: 2000,
      selectedWarehouse: null,
      showCreateWarehouse: false,
      showUpdateWarehouse: false,
    };
  },
  async created() {
      await this.getWarehouseList();
  },
  methods: {
    createWarehouse(newWarehouse){
      newWarehouse.id = this.warehouseId += Math.floor(Math.random() * 9 + 1);
      this.warehouses.push(newWarehouse);
    },
    updateWarehouse(warehouse){
      Object.assign(this.selectedWarehouse, warehouse);
      this.selectedWarehouse = null
    },
    deleteWarehouse(warehouse){
      // Use the browser-native confirmation dialog
      const confirmed = window.confirm("Are you sure you want to delete the warehouse?");

      if (confirmed) {
        const index = this.warehouses.findIndex(w => w.id === warehouse.id);

        if (index !== -1) {
          this.warehouses.splice(index, 1);
        }
      }
    },
    openUpdateWarehouse(warehouse){
      this.selectedWarehouse = warehouse;
      this.showUpdateWarehouse = true;
    },
    closePopUp(){
      this.showCreateWarehouse = false;
      this.showUpdateWarehouse = false;
    },
    async getWarehouseList(){
      try {
        this.warehouses = await this.warehouseService.asyncFindAll()
      } catch (error){
        console.error("Error occurred while getting the data from the backend", error)
      }
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