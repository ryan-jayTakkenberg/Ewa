<template>

<SolarTitle class="header" page-title="Warehouse"></SolarTitle>
  <div class="body">
    <div class="body-container">
      <div class="action-row">
        <SolarSearchbar class="ml-2" place-holder="Search For Warehouses" @input="handleInputValueChange"></SolarSearchbar>
        <SolarButton class="ml-auto mr-2" button-text="Add Warehouse" @click="showCreateWarehouse = true"></SolarButton>
      </div>

      <SolarTable :columns="['Name', 'Address', 'Postal code', 'Action']">
        <WarehouseRowAdmin
            v-for="(warehouse, index) in warehouses"
            :key="index"
            :warehouse="warehouse"
            @openUpdateWarehouse="openUpdateWarehouse"
            @deleteWarehouse="deleteWarehouse"
        />
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
import NotificationComponent from "@/components/general/NotificationComponent.vue";
import WarehouseRowAdmin from "@/components/warehouse/WarehouseRowAdmin";

export default {
  name: "WarehouseOverview",
  inject: ['warehouseService'],
  components: {
    UpdateWarehouse,
    SolarTitle,
    SolarSearchbar,
    SolarButton,
    SolarTable,
    CreateWarehouse,
    WarehouseRowAdmin,
  },
  data() {
    return {
      warehouses: [],
      selectedWarehouse: null,
      showCreateWarehouse: false,
      showUpdateWarehouse: false,
    };
  },
  async created() {
      await this.getWarehouseList();
      console.log(await this.getWarehouseList());
  },

  computed: {
    filteredWarehouses() {
      return this.warehouses.filter(p => {
        return Object.keys(p).some(key => `${p[key]}`.toLowerCase().includes(this.inputValue));
      })
    },
  },
  methods: {
    async createWarehouse(newWarehouse){
      try {
        await this.warehouseService.asyncSaveWarehouse(newWarehouse)
        await this.getWarehouseList();
      }catch (error){
        console.error("Error occurred during creation of new warehouse", error)
      }
    },
    async updateWarehouse(warehouse){
      try {
        console.log("warehouse update")
        await this.warehouseService.asyncUpdateWarehouse(warehouse)
        await this.getWarehouseList();
      } catch (error){
        console.error("Error occurred during saving of existing warehouse", error)
      }
    },
    async deleteWarehouse(warehouse){
      // Use the browser-native confirmation dialog
      const confirmed = window.confirm("Are you sure you want to delete the warehouse?");

      if (confirmed) {
        try {
          await this.warehouseService.asyncDeleteById(warehouse.id);
          await this.getWarehouseList();
        } catch (error){
          console.error("Error occurred during deleting process", error)
        }
      }
    },
    async getWarehouseList(){
      try {
        this.warehouses = await this.warehouseService.asyncFindAll()
      } catch (error){
        console.error("Error occurred while getting the data from the backend", error)
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
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value;
      // Use this.filterValue to search in the table
    },
  }
}
</script>

<style scoped>

.header {
  flex-direction: row;
  display: flex;
  padding-left: 1rem;
  padding-top: 1rem;
}

.body {
  position: relative;
  overflow-x: auto;
  color: #C7D02C;
}

.body-container {
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 1rem;
  background-color: white;
}

.action-row {
  display: flex;
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}
</style>
