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

  methods: {
    closeModal() {
      this.$router.push(this.$route.matched[0].path);
    },
    findSelectedRouteFromParam(route) {
      if (route && route.params.id) {
        const userId = parseInt(route.params.id);
        return this.users.find(user => user.id === userId);
      }
      return null;
    },
    addUser(newUser) {
      this.users.push(newUser);
    },
    deleteUser(userID) {
      this.users = this.users.filter(user => user.id !== userID)
      this.isDeleteUserModalOpen = false;  // Open the modal
    },
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value;  // Use this.inputValue to search in the table
    },
    openDeleteUserModal(user) {
      this.$router.push(this.$route.matched[0].path + "/delete/" + user.id);
      this.selectedUser = user;  // Set the selected user
      // Open the modal
    },
    openEditUserModal(user) {
      this.$router.push(this.$route.matched[0].path + "/edit/" + user.id);
      this.selectedUser = user;  // Set the selected user
      this.isEditUserModalOpen = true;  // Open the modal
    },
    openCreateUserModal(user) {
      this.$router.push(this.$route.matched[0].path + "/create/" + user.id);
      this.isCreateUserModalOpen = true;  // Open the modal
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
  watch: {
    '$route'(to) {
      this.selectedUser = this.findSelectedRouteFromParam(this.$route);
      const modalType = to.path.includes('edit') ? 'isEditUserModalOpen' : to.path.includes('delete') ? 'isDeleteUserModalOpen' : null;

      // Close any open modals
      this.isCreateUserModalOpen = false;
      this.isEditUserModalOpen = false;
      this.isDeleteUserModalOpen = false;

      // Open the appropriate modal based on the route type
      if (modalType === 'isEditUserModalOpen') {
        this.isEditUserModalOpen = true;
      } else if (modalType === 'isDeleteUserModalOpen') {
        this.isDeleteUserModalOpen = true;
      }
    },
  }
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
          <SolarDropdownMenuItem text-menu-item="Edit Users" @click="openEditUserModal(getSelectedUsers()[0])">
          </SolarDropdownMenuItem>
          <SolarDropdownMenuItem text-menu-item="Delete Users" @click="openDeleteUserModal">
          </SolarDropdownMenuItem>
        </SolarDropdownMenuButton>

        <SearchBarComponent
            class="ml-auto" place-holder="Search For Users"
            @input="handleInputValueChange">
        </SearchBarComponent>

        <ButtonComponent button-text="Add User" @click="openCreateUserModal"></ButtonComponent>
      </div>
      <SolarTable :columns="['User', 'Function', 'Last Logged In', 'Action']">
        <UsersRowComponent
            v-for="(user, index) in users"
            :key="index"
            :user="user"
            :isChecked="user.isChecked"
            @on-click-edit-user="openEditUserModal"
            @on-click-delete-user="openDeleteUserModal"
            @toggle-checkbox="toggleCheckbox(user, $event)"> <!-- Pass user and checkbox state -->
        </UsersRowComponent>
      </SolarTable>
    </div>
  </div>

  <CreateUserModal
      v-if="isCreateUserModalOpen"
      :on-close="closeModal">
  </CreateUserModal>

  <EditUserModal
      v-if="isEditUserModalOpen"
      :user="selectedUser"
      :on-close="closeModal">
  </EditUserModal>

  <DeleteUserModal
      v-if="isDeleteUserModalOpen"
      :user="selectedUser"
      @delete-user="deleteUser"
      :on-close="closeModal">
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
