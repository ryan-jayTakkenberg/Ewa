<script>
import User from "@/models/user";

import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import ButtonComponent from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import UsersRowComponent from "@/components/users/UsersRowComponent.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import EditUserModal from "@/components/users/EditUserModal.vue";
import DeleteUserModal from "@/components/users/DeleteUserModal.vue";
import CreateUserModal from "@/components/users/CreateUserModal.vue";

export default {
  name: "UsersOverview",
  components: {
    CreateUserModal,
    DeleteUserModal,
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
      isCreateUserModalOpen: false,
      isEditUserModalOpen: false,
      isDeleteUserModalOpen: false,
      checkedUsers: [], // A list of the selected users ID's
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
  // TODO Temporary explanation:
  /**
   * "user" in openEditUserModal() is NOT a User object when clicking the EDIT or DELETE options in the dropdown,
   * instead "user" is a click event object !! this will throw an error later on !
   *
   * A list of all checked users ID's has been added to data() to keep track of the checked users
   * A new function called getSelectedUsers() has been added to get the user objects from the ID's
   * This getSelectedUsers() is called, and only the first (for now) object is parsed to the openEditUserModal(). See line 99 !
   * This fixes the above mentioned problem (for now)
   *
   * TODO implement a way to display more than one edit popup (and support an array of users to edit)
   */
  methods: {
    addUser(newUser) {
      // Add the new user to the users array
      this.users.push(newUser);
    },

    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value;
      // Use this.inputValue to search in the table
    },
    openDeleteUserModal(user) {
      if (!user) {
        // No user selected
        return;
      }
      this.selectedUser = user;  // Set the selected user
      this.isDeleteUserModalOpen = true;  // Open the modal
    },
    openEditUserModal(user) {
      if (!user) {
        // No user selected
        return;
      }
      this.selectedUser = user;  // Set the selected user
      this.isEditUserModalOpen = true;  // Open the modal
    },
    openCreateUserModal() {
      this.isCreateUserModalOpen = true;  // Open the modal
    },

    closeEditUserModal() {
      this.isEditUserModalOpen = false;
    },
    closeDeleteUserModal() {
      this.isDeleteUserModalOpen = false;
    },
    closeCreateUserModal() {
      this.isCreateUserModalOpen = false;
    },
    toggleCheckbox(user, isChecked) {
      if (isChecked) {
        this.checkedUsers.push(user.id);
      } else {
        this.checkedUsers = this.checkedUsers.filter(id => id !== user.id);
      }
      console.log(this.checkedUsers);
    },
    getSelectedUsers() {
      return this.users.filter(user => this.checkedUsers.includes(user.id));
    }
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
        <SolarDropdownMenuButton text-button="Action">
          <SolarDropdownMenuItem
              text-menu-item="Edit Users" @click="openEditUserModal(getSelectedUsers()[0])"></SolarDropdownMenuItem>
          <SolarDropdownMenuItem
              text-menu-item="Delete Users" @click="openDeleteUserModal"></SolarDropdownMenuItem>
        </SolarDropdownMenuButton>

        <SearchBarComponent
            class="ml-auto" place-holder="Search For Users"
            @input="handleInputValueChange"></SearchBarComponent>

        <ButtonComponent button-text="Add User" @click="openCreateUserModal"></ButtonComponent>
      </div>
      <SolarTable :columns="['User', 'Function', 'Last Logged In', 'Action']">
        <UsersRowComponent
            v-for="(user, index) in users"
            :key="index"
            :user="user"
            :isChecked="user.isChecked"
            @click-edit-user="openEditUserModal"
            @toggle-checkbox="toggleCheckbox(user, $event)"> <!-- Pass user and checkbox state -->
        </UsersRowComponent>
      </SolarTable>
    </div>
  </div>

  <CreateUserModal v-if="isCreateUserModalOpen" :on-close="closeCreateUserModal" @addUser="addUser"></CreateUserModal>
  <EditUserModal v-if="isEditUserModalOpen" :user="selectedUser" :on-close="closeEditUserModal"></EditUserModal>

  <DeleteUserModal v-if="isDeleteUserModalOpen" :user="selectedUser" on-delete="" :on-close="closeDeleteUserModal">
  </DeleteUserModal>
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
