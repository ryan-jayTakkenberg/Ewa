<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import ButtonComponent from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import Warehouse from "@/models/warehouse";
import WarehouseRowComponent from "@/components/warehouseView/WarehouseRowComponent";


export default {
  name: "WarehouseOverview",
  components: {
    SolarDropdownMenuItem,
    SolarDropdownMenuButton,
    SolarTable,
    TitleComponent,
    SearchBarComponent,
    ButtonComponent,
    WarehouseRowComponent,
  },
  data() {
    return {
      inputValue: '', // Store the input value for searching warehouses
      warehouses: [...Warehouse.warehouses],
      selectedWarehouse: null,  // Track the selected warehouse for editing
      isEditUserModalOpen: false,
      checkedWarehouses: [],
    };
  },
  created() {
    if (!this.warehouses?.length) {
      // Keep updating the list if the database has not returned all the data yet
      const fetchingInterval = setInterval(() => {
        if (!Warehouse.fetching) {
          this.projects = [...Warehouse.projects];
          clearInterval(fetchingInterval);
        }
      }, 100);
    }
  },
  methods: {
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value;
      // Use this.filterValue to search in the table
    },
    openEditUserModal(warehouse) {
      if (!warehouse) {
        // No user selected
        return;
      }
      this.selectedWarehouse = warehouse;  // Set the selected user
      this.isEditUserModalOpen = true;  // Open the modal
    },
    closeEditUserModal() {
      this.isEditUserModalOpen = false;
    },
    toggleCheckbox(warehouse, isChecked) {
      if (isChecked) {
        this.checkedWarehouses.push(warehouse.id);
      } else {
        this.checkedWarehouses = this.checkedWarehouses.filter(id => id !== warehouse.id);
      }
      console.log(this.checkedWarehouses);
    }, getSelectedUsers() {
      return this.warehouses.filter(warehouse => this.checkedWarehouses.includes(warehouse.id));
    }
  },
}
</script>

<template>
  <div class="users-header">
    <TitleComponent page-title="Warehouse"></TitleComponent>
  </div>

  <div class="users-body">
    <div class="users-container">
      <div class="users-action-row">
        <!-- Action Dropdown Button -->
        <SolarDropdownMenuButton text-button="Action">
          <SolarDropdownMenuItem text-menu-item="Edit Warehouse" @click="openEditUserModal(getSelectedUsers()[0])"></SolarDropdownMenuItem>
          <SolarDropdownMenuItem text-menu-item="Delete Warehouse" @click="openDeleteUserModal"></SolarDropdownMenuItem>
        </SolarDropdownMenuButton>

        <!-- Searchbar -->
        <SearchBarComponent place-holder="Search For Warehouses" class="ml-auto" @input="handleInputValueChange"
        ></SearchBarComponent>
        <ButtonComponent button-text="Add Warehouse" :onClick="openEditUserModal"></ButtonComponent>
      </div>

      <SolarTable :columns="['Warehouse', 'Address', 'Postalcode']">
        <WarehouseRowComponent
            v-for="(warehouse, index) in warehouses"
            :key="index"
            :warehouse="warehouse"
            :isChecked="warehouse.isChecked"
            @click-edit-user="openEditUserModal"
            @toggle-checkbox="toggleCheckbox(warehouse, $event)" ><!-- Pass user and checkbox state -->
        </WarehouseRowComponent>
      </SolarTable>


    </div>
  </div>

<!--  <ProjectEditComponent v-if="isEditUserModalOpen" :on-close="closeEditUserModal" :project="selectedProject" ></ProjectEditComponent>-->

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
