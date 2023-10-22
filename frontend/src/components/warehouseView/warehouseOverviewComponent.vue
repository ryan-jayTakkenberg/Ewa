<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";

import SolarTable from "@/components/general/SolarTable.vue";

import WarehouseRowComponent from "@/components/warehouseView/WarehouseRowComponent.vue";
import Warehouse from "@/models/warehouse";




export default {
  name: "UsersOverview",
  components: {

    WarehouseRowComponent,
    SolarTable,
    TitleComponent,
    SearchBarComponent,
  },
  data() {
    return {
      inputValue: '', // Store the input value for searching users
      warehouses: [...Warehouse.warehouses],
      selectedWarehouse: null,  // Track the selected user for editing
      checkedTeams: [],
    };
  },
  computed: {
    filteredWarehouses() {
      // Filter teams based on filterValue (you may need to adjust the property you're filtering by)
      return this.warehouses.filter(warehouse => {
        return warehouse.name.includes(this.inputValue);
      });

    },
  },
  methods: {
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value;
      // Use this.filterValue to search in the table
    },
  },
}
</script>

<template>
  <div class="users-header">
    <TitleComponent page-title="Team"></TitleComponent>
  </div>

  <div class="users-body">
    <div class="users-container">
      <div class="users-action-row">

        <!-- Searchbar -->
        <SearchBarComponent place-holder="Search For Teams" class="ml-auto" @input="handleInputValueChange"
        ></SearchBarComponent>
      </div>


      <SolarTable :columns="['Warehouse', 'locatie', 'address', 'Action']">
        <WarehouseRowComponent
            v-for="(warehouse, index) in filteredWarehouses"
            :key="index"
            :warehouse="warehouse"
        :isChecked="warehouse.isChecked"
            ><!-- Pass a single warehouse object and checkbox state -->
        </WarehouseRowComponent>
      </SolarTable>



    </div>
  </div>
</template>

<style scoped>
.users-header {
  flex-direction: row;
  display: flex;
  padding: 1rem;
}

.users-action-row {
  display: flex;
  margin-bottom: 1rem /* 16px */;
}

.users-body {
  position: relative;
  overflow-x: auto;
}

.users-container {
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 1rem /* 16px */;
  background-color: white;
}


</style>
