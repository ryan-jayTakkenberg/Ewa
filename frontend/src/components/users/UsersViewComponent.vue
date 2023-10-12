<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import ButtonComponent from "@/components/general/SolarButton.vue";
import EditUserModal from "@/components/modals/EditUserModal.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import UsersRowComponent from "@/components/users/UsersRowComponent.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";

export default {
  name: "UsersComponent",
  components: {
    SolarDropdownMenuItem,
    SolarDropdownMenuButton,
    UsersRowComponent,
    SolarTable,
    TitleComponent,
    SearchBarComponent,
    ButtonComponent,
    EditUserModal,
  },
  data() {
    return {
      isEditUserModalOpen: false,
      inputValue: '', // Store the input value
    };
  },
  methods: {
    handleInputValueChange(value) {
      this.inputValue = value;
      // Use this.inputValue to search in the table
    },
    openEditUserModal() {
      this.isEditUserModalOpen = true;
    },
    closeEditUserModal() {
      this.isEditUserModalOpen = false;
    },
  }
}
</script>

<template>
  <div class="header">
    <TitleComponent page-title="Users"></TitleComponent>
  </div>

  <div class="body">
    <EditUserModal v-if="isEditUserModalOpen" :on-close="closeEditUserModal"></EditUserModal>
    <div class="body-container">
      <div class="action-row">
        <!-- Action Dropdown Button -->
        <SolarDropdownMenuButton text-button="Action">

          <!-- Edit User/Users-->
          <SolarDropdownMenuItem
              text-menu-item="Edit Users"
              on-click="OpenEditUsersModal"
          ></SolarDropdownMenuItem>

          <!-- Delete User/Users-->
          <SolarDropdownMenuItem
              text-menu-item="Delete Users"
              on-click="OpenDeleteUserModal"
          ></SolarDropdownMenuItem>
        </SolarDropdownMenuButton>

        <!-- Searchbar -->
        <SearchBarComponent
            place-holder="Search For Users"
            class="ml-auto"
            @input="handleInputValueChange"
        ></SearchBarComponent>

        <p>Input value in parent: {{ inputValue }}</p>
        <ButtonComponent button-text="Add User" :on-click="openEditUserModal"></ButtonComponent>
      </div>
      <SolarTable :columns="['User', 'Function', 'Last Logged In', 'Action']">
        <UsersRowComponent
            user-email="example@company.com"
            user-name="Example"
            user-function="Admin"
            user-last-logged-in="1 February 2023"
            :on-click-edit-user="openEditUserModal"
        ></UsersRowComponent>
      </SolarTable>

    </div>
  </div>
</template>


<style scoped>
.header {
  flex-direction: row;
  display: flex;
  padding: 1rem;
}

.action-row {
  display: flex;
  margin-bottom: 1rem /* 16px */;
}

.body {
  position: relative;
  overflow-x: auto;
}

.body-container {
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 1rem /* 16px */;
  background-color: white;
}


</style>
