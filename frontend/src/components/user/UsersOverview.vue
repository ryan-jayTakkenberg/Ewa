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
    DeleteUserModal,
    UpdateUserModal,
  },
  data() {
    return {
      inputValue: '', // Store the input value for searching users
      users: [],
      selectedUser: null,  // The selected user for editing / deleting
      checkedUsers: [], // A list of the selected users ID's for editing / deleting multiple users at once
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
  },
  created() {
    this.fetchUsers();
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
      this.closeModal(); // Close the modal
    },
    deleteUser(userID) {
      this.users = this.users.filter(user => user.id !== userID)
      this.closeModal();
    },
    getSelectedUsers() {
      return this.users.filter(user => this.checkedUsers.includes(user.id));
    },
    deleteSelectedUsers() {
      // Remove the selected users from the users array
      this.checkedUsers.forEach(userId => {
        const index = this.users.findIndex(user => user.id === userId);
        if (index !== -1) {
          this.users.splice(index, 1);
        }
      })
      // Clear the checkedUsers array
      this.checkedUsers = [];
    },
    updateSelectedUsers() {
      //TODO

    },
    findSelectedRouteFromParam(route) {
      if (route && route.params.id) {
        const userId = parseInt(route.params.id);
        return this.users.find(user => user.id === userId);
      }
      return null;
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
    closeModal() {
      this.$router.push(this.$route.matched[0].path);
    },
    toggleCheckbox(user, isChecked) {
      if (isChecked) {
        this.checkedUsers.push(user.id);
      } else {
        this.checkedUsers = this.checkedUsers.filter(id => id !== user.id);
      }
      console.log(this.checkedUsers);
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
  <div class="users-header">
    <TitleComponent page-title="Users"></TitleComponent>
  </div>
  <div class="users-body">
    <div class="users-container">
      <div class="users-action-row">
        <SolarDropdownMenuButton text-button="Action">
          <!-- Edit multiple Users -->
          <SolarDropdownMenuItem text-menu-item="Edit Users" @click="updateSelectedUsers"></SolarDropdownMenuItem>
          <!-- Delete multiple Users -->
          <SolarDropdownMenuItem text-menu-item="Delete Users" @click="deleteSelectedUsers"></SolarDropdownMenuItem>
        </SolarDropdownMenuButton>
        <SolarSearchbar place-holder="Search For Users" @search="handleInputValueChange"></SolarSearchbar>
        <SolarButton class="ml-auto" button-text="Add User" @click="openCreateModal"></SolarButton>
      </div>
      <SolarTable :columns="['User', 'Function', 'Last Logged In', 'Action']">
        <UsersRowComponent
            v-for="(user, index) in filterUsers"
            :key="index"
            :user="user"
            @on-click-edit-user="openEditModal"
            @on-click-delete-user="openDeleteModal"
            @toggle-checkbox="toggleCheckbox(user, $event)"
            :is-checked="checkedUsers.includes(user.id)"> <!-- Pass user and checkbox state -->
        </UsersRowComponent>
      </SolarTable>
    </div>
  </div>
  <!-- Conditionally render modals based on route -->
  <CreateUserModal
      v-if="$route.path.includes('create')" :on-close="closeModal" @create-user="createUser"/>
  <UpdateUserModal
      v-if="$route.path.includes('edit')" :user="selectedUser" :on-close="closeModal" @update-user="updateUser"/>
  <DeleteUserModal
      v-if="$route.path.includes('delete')" :user="selectedUser" @delete-user="deleteUser" :on-close="closeModal"/>
</template>

<style scoped>
.users-header {
  flex-direction: row;
  display: flex;
  padding: 1rem;
}

.users-action-row {
  display: flex;
  margin-bottom: 1rem;
  margin-top: 1rem;
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
