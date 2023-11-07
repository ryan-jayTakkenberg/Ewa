<script>
import User from "@/models/user";

import TitleComponent from "@/components/general/SolarTitle.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import SolarSearchbar from "@/components/general/SolarSearchbar.vue";
import SolarButton from "@/components/general/SolarButton.vue";
import DeleteUserModal from "@/components/user/user-components/DeleteUserModal.vue";
import UsersRowComponent from "@/components/user/user-components/UsersRowComponent.vue";
import UpdateUserModal from "@/components/user/user-components/UpdateUserModal.vue";
import CreateUserModal from "@/components/user/user-components/CreateUserModal.vue";
import DeleteMultipleUsersModal from "@/components/user/user-components/DeleteMultipleUsersModal.vue";

export default {
  name: "UsersOverview",
  components: {
    TitleComponent,
    SolarDropdownMenuItem,
    SolarDropdownMenuButton,
    SolarSearchbar,
    SolarButton,
    SolarTable,
    UsersRowComponent,
    CreateUserModal,
    UpdateUserModal,
    DeleteUserModal,
    DeleteMultipleUsersModal,
  },
  created() {
    this.fetchUsers();
  },
  data() {
    return {
      inputValue: '', // Store the input value for searching users
      users: [],
      selectedUser: null,  // The selected user for editing / deleting
      checkedUsers: [], // A list of the selected users for editing / deleting multiple users at once
    };
  },
  computed: {
    filterUsers() {
      return this.users.filter(p => {
        for (let key of Object.keys(p)) {
          if (`${p[key]}`.toLowerCase().includes(this.inputValue)) {
            return true;
          }
        }
        return false;
      });
    },
    isActionButtonDisabled() {
      return this.checkedUsers.length === 0;
    },
  },
  methods: {
    fetchUsers() {
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
    createUser(newUser) {
      this.users.push(newUser);
      this.closeModal();
    },
    updateUser(updatedUser) {
      // Find the index of the user to be updated in the users array
      const index = this.users.findIndex(user => user.id === updatedUser.id);
      if (index !== -1) {
        // Update the user data in the array
        this.users[index] = updatedUser;
      }
      this.closeModal();
    },
    deleteUser(userID) {
      this.users = this.users.filter(user => user.id !== userID);
      this.closeModal();
    },
    deleteCheckedUsers() {
      // Get the IDs of the users to delete
      const userIdsToDelete = this.checkedUsers.map(user => user.id);

      // Uncheck the selected users in the UsersRowComponent
      this.users.forEach(user => {
        user.isChecked = false;
      });

      // Remove the selected users from the users array based on their IDs
      this.users = this.users.filter(user => !userIdsToDelete.includes(user.id));

      // Clear the checkedUsers array
      this.checkedUsers = [];

      this.closeModal();
    },
    editCheckedUsersOneByOne() {
      // TODO edit checkedUsers oneByONe
      //  Check if there are any selected users
      // if (this.checkedUsers.length > 0) {
      //   const userToEdit = this.checkedUsers[0];
      //   this.openEditModal(userToEdit);
      //
      //   // Remove the first user from the array
      //   this.checkedUsers.splice(0, 1);
      // }
    },
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value.trim().toLowerCase();  // Use this.filterValue to search in the table
    },
    openCreateModal() {
      this.$router.push(`${this.$route.matched[0].path}/create`);
    },
    openEditModal(user) {
      this.$router.push(`${this.$route.matched[0].path}/edit/${user.id}`);
      this.selectedUser = user;
    },
    openDeleteModal(user) {
      this.$router.push(`${this.$route.matched[0].path}/delete/${user.id}`);
      this.selectedUser = user;
    },
    openDeleteMultipleUsersModal() {
      this.$router.push(`${this.$route.matched[0].path}/delete-users`);
    },
    closeModal() {
      this.$router.push(this.$route.matched[0].path);
    },
    toggleCheckbox(user) {
      const userIndex = this.checkedUsers.findIndex(u => u.id === user.id);

      if (userIndex !== -1) {
        // User is checked, so remove them from the array
        this.checkedUsers = this.checkedUsers.filter(u => u.id !== user.id);
      } else {
        // User is not checked, so add them to the array
        this.checkedUsers = [...this.checkedUsers, user];
      }
      console.log(this.checkedUsers);
    },
    closeDropdown() {
      this.$refs.dropdownButton.hideDropdown(); // Call the hideDropdown method from the ref
    },
    findSelectedRouteFromParam(route) {
      if (route && route.params.id) {
        const userId = parseInt(route.params.id);
        return this.users.find(user => user.id === userId);
      }
      return null;
    },
  },
  watch: {
    '$route'(to) {
      this.selectedUser = this.findSelectedRouteFromParam(to);
    }
  }
}
</script>

<template>
  <TitleComponent class="header" page-title="Users"></TitleComponent>
  <div class="body">
    <div class="body-container">
      <div class="action-row">
        <SolarDropdownMenuButton
            text-button="Action" ref="dropdownButton"
            :disabled="isActionButtonDisabled">
          <SolarDropdownMenuItem
              text-menu-item="Edit Users"
              @click="editCheckedUsersOneByOne" @item-click="closeDropdown">
          </SolarDropdownMenuItem>
          <SolarDropdownMenuItem
              text-menu-item="Delete Users"
              @click="openDeleteMultipleUsersModal" @item-click="closeDropdown">
          </SolarDropdownMenuItem>
        </SolarDropdownMenuButton>
        <SolarSearchbar place-holder="Search For Users" @search="handleInputValueChange"></SolarSearchbar>
        <SolarButton class="ml-auto" button-text="Add User" @click="openCreateModal"></SolarButton>
      </div>

      <SolarTable :columns="['User', 'Function', 'Last Logged In', 'Action']">
        <UsersRowComponent
            v-for="(user) in filterUsers" :key="user.id" :user="user"
            @edit="openEditModal"
            @delete="openDeleteModal"
            @toggle="toggleCheckbox(user, $event)"> <!-- Pass user and checkbox state -->
        </UsersRowComponent>
      </SolarTable>
    </div>
  </div>

  <!-- Conditionally render modals based on route -->
  <CreateUserModal
      v-if="$route.path.includes('create')"
      :on-close="closeModal"
      @create-user="createUser"/>
  <UpdateUserModal
      v-if="$route.path.includes('edit')"
      :on-close="closeModal"
      :user="selectedUser"
      @update-user="updateUser"/>
  <DeleteUserModal
      v-if="$route.path.includes('delete') && selectedUser != null"
      :on-close="closeModal"
      :user="selectedUser"
      @delete-user="deleteUser"/>
  <DeleteMultipleUsersModal
      v-if="$route.path.includes('delete-users')"
      :on-close="closeModal"
      :users-to-delete="checkedUsers"
      @delete-users="deleteCheckedUsers">
  </DeleteMultipleUsersModal>
</template>

<style scoped>
.header {
  flex-direction: row;
  display: flex;
  padding: 1rem;
}

.body {
  position: relative;
  overflow-x: auto;
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
  margin-bottom: 1rem;
}
</style>
