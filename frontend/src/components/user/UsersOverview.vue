<script>
import User from "@/models/user";

import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import ButtonComponent from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import UsersRowComponent from "@/components/user/UsersRowComponent.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import EditUserModal from "@/components/user/EditUserModal.vue";
import DeleteUserModal from "@/components/user/DeleteUserModal.vue";
import CreateUserModal from "@/components/user/CreateUserModal.vue";

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
      users: [],
      selectedUser: null,  // Track the selected user for editing / deleting
      checkedUsers: [], // A list of the selected users ID's
    };
  },
  computed: {
    filterUsers() {
      // Create a regular expression with the inputValue and the 'i' flag for case-insensitivity
      const regex = new RegExp(this.inputValue, 'i');

      // Filter users based on the regular expression
      return this.users.filter(user => {
        return regex.test(user.name);
      });
    }
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
    addUser(newUser) {
      this.users.push(newUser);
      this.closeModal();
    },
    deleteUser(userID) {
      this.users = this.users.filter(user => user.id !== userID)
      this.closeModal();
    },
    handleInputValueChange(value) {
      console.log(value);
      this.inputValue = value;  // Use this.filterValue to search in the table
    },
    openEditModal(user) {
      this.$router.push(`${this.$route.matched[0].path}/edit/${user.id}`);
      this.selectedUser = user;
    },
    openCreateModal() {
      this.$router.push(`${this.$route.matched[0].path}/create`);
    },
    openDeleteModal(user) {
      this.$router.push(`${this.$route.matched[0].path}/delete/${user.id}`);
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
    getSelectedUsers() {
      return this.users.filter(user => this.checkedUsers.includes(user.id));
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
  <div class="users-header">
    <TitleComponent page-title="Users"></TitleComponent>
  </div>

  <div class="users-body">
    <div class="users-container">
      <div class="users-action-row">
        <SolarDropdownMenuButton text-button="Action">
          <SolarDropdownMenuItem text-menu-item="Edit Users" @click="openEditModal(getSelectedUsers()[0], 'edit')">
          </SolarDropdownMenuItem>
          <SolarDropdownMenuItem text-menu-item="Delete Users" @click="openDeleteModal(getSelectedUsers()[0], 'delete')">
          </SolarDropdownMenuItem>
        </SolarDropdownMenuButton>

        <SearchBarComponent
            class="ml-auto" place-holder="Search For Users"
            @input="handleInputValueChange">
        </SearchBarComponent>

        <ButtonComponent button-text="Add User" @click="openCreateModal"></ButtonComponent>
      </div>
      <SolarTable :columns="['User', 'Function', 'Last Logged In', 'Action']">
        <UsersRowComponent
            v-for="(user, index) in filterUsers"
            :key="index"
            :user="user"
            :isChecked="user.isChecked"
            @on-click-edit-user="openEditModal"
            @on-click-delete-user="openDeleteModal"
            @toggle-checkbox="toggleCheckbox(user, $event)"> <!-- Pass user and checkbox state -->
        </UsersRowComponent>
      </SolarTable>
    </div>
  </div>

  <!-- Conditionally render modals based on route -->
  <CreateUserModal
      v-if="$route.path.includes('create')"
      :on-close="closeModal"
      :add-user="addUser"
  />
  <EditUserModal
      v-if="$route.path.includes('edit')"
      :user="selectedUser"
      :on-close="closeModal"

  />
  <DeleteUserModal
      v-if="$route.path.includes('delete')"
      :user="selectedUser"
      @delete-user="deleteUser"
      :on-close="closeModal"
  />
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
