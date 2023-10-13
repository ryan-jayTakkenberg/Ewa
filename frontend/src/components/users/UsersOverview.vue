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
      inputValue: '', // Store the input value for searching users
      users: [...User.users],
      selectedUser: null,  // Track the selected user for editing
      isEditUserModalOpen: false,
    };
  },
  created() {
    if (!this.users?.length) {
      // Keep updating the list if the database has not returned all the data yet
      const fetchingInterval = setInterval(() => {
        if (!User.fetching) {
          this.users = [...User.users];
          clearInterval(fetchingInterval);
        }
      }, 100);
    }
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
    toggleCheckbox(user, isChecked) {
      user.isChecked = isChecked;  // Update the user's isChecked state
      console.log(user.isChecked, user.id); // Log the updated isChecked state
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
        <ButtonComponent button-text="Add User" :on-click="openEditUserModal"></ButtonComponent>
      </div>

      <SolarTable :columns="['User', 'Function', 'Last Logged In', 'Action']">
        <UsersRowComponent
            v-for="(user, index) in users"
            :key="index"
            :user="user"
            :isChecked="user.isChecked"
            @click-edit-user="openEditUserModal"
            @toggle-checkbox="toggleCheckbox(user, $event)"><!-- Pass user and checkbox state -->
        </UsersRowComponent>
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
