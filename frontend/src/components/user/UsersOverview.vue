<script>
import User from "@/models/user";
import TitleComponent from "@/components/general/SolarTitle.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import SolarSearchbar from "@/components/general/SolarSearchbar.vue";
import SolarButton from "@/components/general/SolarButton.vue";
import DeleteUserModal from "@/components/user/user-modals/DeleteUserModal.vue";
import UpdateUserModal from "@/components/user/user-modals/UpdateUserModal.vue";
import CreateUserModal from "@/components/user/user-modals/CreateUserModal.vue";
import DeleteMultipleUsersModal from "@/components/user/user-modals/DeleteMultipleUsersModal.vue";
import UsersRowComponent from "@/components/user/UsersRowComponent.vue";

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
      users: [...User.users],
      selectedUser: null,  // The selected user for editing / deleting
      checkedUsers: [], // A list of the selected users for editing / deleting multiple users at once
      showCreateModal: false,
      showUpdateModal: false,
      showDeleteModal: false,
      showDeleteMultipleModal: false,
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
      console.log(this.users)
    },
    async createUser(createdUser) {
      this.closeModal();
      let newUser = await createdUser.putDatabase();
      if (newUser) {
        this.users.push(newUser);
      }
    },

    async editUser(updated) {
      // Assuming there is only one user in this.selectedProducts
      let edited = this.selectedUser;

      if (edited) {
        let index = this.users.findIndex(p => p.id === edited.id);
        edited.injectAttributes(updated);
        let user = await edited.putDatabase();

        if (user) {
          this.users[index] = user;
        }
      }

      this.closeModal();
    },

    async deleteUser() {
      this.closeModal();
      const deletedUser = this.selectedUser;
      this.users = this.users.filter(user => user.id !== deletedUser.id);
      await deletedUser.delDatabase();
    },

    deleteCheckedUsers() {
      // Get the IDs of the users to delete
      const userIdsToDelete = this.checkedUsers.map(user => user.id);

      // Uncheck the selected users in the UsersRowComponent
      this.users.forEach(user => {
        user.isChecked = false;
      });

      // Use Promise.all to delete each user and update the users array
      Promise.all(
          userIdsToDelete.map(async (userId) => {
            const deletedUser = this.users.find(user => user.id === userId);
            if (deletedUser) {
              await deletedUser.delDatabase();
            }
          })
      ).then(() => {
        // Remove the selected users from the users array based on their IDs
        this.users = this.users.filter(user => !userIdsToDelete.includes(user.id));

        // Clear the checkedUsers array
        this.checkedUsers = [];

        // Close the modal
        this.closeModal();
      });
    },
    editCheckedUsersOneByOne() {
      // TODO edit checkedUsers oneByONe
      //  Check if there are any selected users
      // if (this.checkedUsers.length > 0) {
      //   const userToEdit = this.checkedUsers[0];
      //   this.openEditModal(userToEdit);
      // this.closeDropdown();
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
      this.showCreateModal = true;
    },
    openEditModal(user) {
      this.selectedUser = user;
      this.showUpdateModal = true;
    },
    openDeleteModal(user) {
      this.selectedUser = user;
      this.showDeleteModal = true;
    },
    openDeleteMultipleUsersModal() {
      console.log("test before");
      this.showDeleteMultipleModal = true;
      console.log("test after");
      this.closeDropdown();
    },
    closeModal() {
      this.showCreateModal = false;
      this.showUpdateModal = false;
      this.showDeleteModal = false;
      this.showDeleteMultipleModal = false;
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
  }
  ,
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
<!--    TODO Edit multiple Users     <SolarDropdownMenuItem-->
<!--              text-menu-item="Edit Users"-->
<!--              @click="editCheckedUsersOneByOne">-->
<!--          </SolarDropdownMenuItem>-->
          <SolarDropdownMenuItem
              text-menu-item="Delete Users"
              @click="openDeleteMultipleUsersModal">
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

  <!-- Conditionally render modals based on boolean modal states -->
  <CreateUserModal
      v-if="showCreateModal" :on-close="closeModal"
      @create-user="createUser"
  />
  <UpdateUserModal
      v-if="showUpdateModal" :on-close="closeModal"
      :user="selectedUser" @update-user="editUser"
  />
  <DeleteUserModal
      v-if="showDeleteModal" :on-close="closeModal"
      :user="selectedUser" @delete-user="deleteUser"
  />
  <DeleteMultipleUsersModal
      v-if="showDeleteMultipleModal" :on-close="closeModal"
      :users-to-delete="checkedUsers" @delete-users="deleteCheckedUsers"
  />
</template>

<style scoped>
.header {
  flex-direction: row;
  display: flex;
  padding-left: 1rem;
  padding-top: 1rem;
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
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}
</style>
