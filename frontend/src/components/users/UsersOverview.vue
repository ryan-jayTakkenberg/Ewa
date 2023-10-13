<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import ButtonComponent from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import UsersRowComponent from "@/components/users/UsersRowComponent.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import User from "@/models/user";
import EditUserModal from "@/components/users/EditUserModal.vue";

export default {
  name: "UsersOverview",
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
      users: [
        new User(1, "example1@company.com", "Full Name 1", "Admin", "3 February 2023"),
        new User(2, "example2@company.com", "Full Name 2", "Viewer", "3 February 2023"),
        new User(3, "example1@company.com", "Full Name 1", "Admin", "3 February 2023"),
        new User(4, "example2@company.com", "Full Name 2", "Viewer", "3 February 2023")
      ],
      selectedUser: null,  // Track the selected user for editing
    };
  },
  methods: {
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value;
      // Use this.inputValue to search in the table
    },
    openEditUserModal(user) {
      this.selectedUser = user;  // Set the selected user
      this.isEditUserModalOpen = true;  // Open the modal
    },
    closeEditUserModal() {
      this.isEditUserModalOpen = false;
    },
  },
}
</script>

<template>
  <div class="users-header">
    <TitleComponent page-title="Users"></TitleComponent>
  </div>

  <div class="users-body">
    <div class="users-container">
      <div class="users-action-row">
        <!-- Action Dropdown Button -->
        <SolarDropdownMenuButton text-button="Action">
          <SolarDropdownMenuItem text-menu-item="Edit Users" :on-click="openEditUserModal"></SolarDropdownMenuItem>
          <SolarDropdownMenuItem text-menu-item="Delete Users" :on-click="openDeleteUserModal"></SolarDropdownMenuItem>
        </SolarDropdownMenuButton>
        <!-- Searchbar -->
        <SearchBarComponent place-holder="Search For Users" class="ml-auto" @input="handleInputValueChange"
        ></SearchBarComponent>
        <ButtonComponent button-text="Add User" on-click="openEditUserModal"></ButtonComponent>
      </div>

      <SolarTable :columns="['User', 'Function', 'Last Logged In', 'Action']">
        <UsersRowComponent
            v-for="(user, index) in users"
            :key="index"
            :user-email="user.email"
            :user-name="user.name"
            :user-role="user.userRole"
            :user-last-logged-in="user.lastLoggedIn"
            @click-edit-user="openEditUserModal(user)"
        ></UsersRowComponent>
      </SolarTable>

    </div>
  </div>
  <EditUserModal v-if="isEditUserModalOpen" :on-close="closeEditUserModal" :user="selectedUser"></EditUserModal>

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
