<script>

import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import WarehouseRowComponent from "@/components/warehouseViewer/WarehouseRowViewerComponent.vue";
import {getId} from "@/data";
import WarehouseProductsComponent from "@/components/warehouseViewer/WarehouseProductsComponent.vue";

export default {
  name: "WarehouseViewerOverview",
  inject: ['warehouseService', 'userService'],
  components: {
    WarehouseRowComponent,
    SolarTable,
    TitleComponent,
    SearchBarComponent,
    WarehouseProductsComponent,
  },

  data() {
    return {
      userId: getId(),
      inputValue: '',
      allWarehouses: [],
      userWarehouses: [],
      selectedWarehouse: null,
      checkedTeams: [],
      currentTeam: null,
    };
  },

  async created() {
    await this.fetchCurrentUserTeam();
    await this.getWarehouse();
  },

  methods: {

    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value;
    },

    async fetchCurrentUserTeam() {
      this.currentUser = await this.userService.getById(this.userId);
      this.currentTeam = this.currentUser.team
      console.log("current user:", this.currentUser);
    },

    async getWarehouse() {
      this.allWarehouses = await this.warehouseService.asyncFindAll();
      console.log("all warehouses: ", this.allWarehouses);
      this.userWarehouses = this.allWarehouses.filter(warehouse => {
        return warehouse.teams && warehouse.teams.some(team => team.id === this.currentTeam.id);
      });
      console.log("user warehouses: ", this.userWarehouses);
    },

    showWarehouseProducts(warehouse) {
      if (this.selectedWarehouse === warehouse) {
        this.selectedWarehouse = null;
      } else {
        this.selectedWarehouse = warehouse;
      }
    },

  },

}
</script>

<template>

  <div class="warehouses-header"><TitleComponent page-title="Warehouse"></TitleComponent></div>

  <div class="warehouses-body">
    <div class="warehouses-container">
      <div class="warehouses-action-row">

      <!-- Searchbar -->
      <SearchBarComponent class="mx-4" place-holder="Search For Warehouses" @input="handleInputValueChange"></SearchBarComponent></div>

      <SolarTable :columns="['Warehouse', 'Location', 'Address', 'Team(s)']">
        <WarehouseRowComponent
            v-for="(warehouse) in userWarehouses"
            :key="warehouse.id"
            :warehouse="warehouse"
            :isChecked="warehouse.isChecked"
            @select-warehouse="showWarehouseProducts">
        </WarehouseRowComponent>
      </SolarTable>

      <!-- Add WarehouseProductsComponent and pass selected warehouse's products -->
      <WarehouseProductsComponent
          v-if="selectedWarehouse"
          :selectedWarehouse="selectedWarehouse"
      ></WarehouseProductsComponent>

    </div>
  </div>

</template>

<style scoped>

.warehouses-header {
  flex-direction: row;
  display: flex;
  padding: 1rem;
}

.warehouses-action-row {
  display: flex;
  margin-bottom: 1rem;
}

.warehouses-body {
  position: relative;
  overflow-x: auto;
}

.warehouses-container {
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 1rem;
  background-color: white;
}

</style>
