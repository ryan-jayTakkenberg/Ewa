<script>
import User from "@/models/user";

export default {
  name: "UsersRowComponent",
  props: {
    user: {
      type: User,  // Use the UserModel as the prop type
      required: true,
    },
    isChecked: {
      type: Boolean,
      default: false,
      required: true,
    },
  },
  data() {
    return {
      checked: this.isChecked, // for two-way binding
    }
  },
  methods: {
    editUser() {
      this.$emit('click-edit-user', this.user);  // Emit the userModel directly
    },
    toggleCheckbox() {
      // Emit an event to toggle the checked value
      this.$emit('toggle-checkbox', this.checked);
    },
  }
}
</script>

<template>
  <tr class="table-row">
    <!-- Check box  -->
    <td class="w-4 p-4">
      <div class="flex items-center">
        <input
            type="checkbox"
            :checked="checked" v-model="checked" @change="toggleCheckbox"
            class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2"
        >
      </div>
    </td>

    <!--User Item-->
    <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap ">
      <div class="pl-3">
        <div class="text-base font-semibold">{{ user.name }}</div>
        <div class="font-normal text-gray-500">{{ user.email }}</div>
      </div>
    </th>
    <td class="px-6 py-4">{{ user.userRole }}</td>
    <td class="px-6 py-4">
      <div class="flex items-center">{{ user.lastLoggedIn }}</div>
    </td>

    <!-- Edit User -->
    <td class="px-6 py-4">
      <div @click="editUser" class="edit-user-btn">Edit user</div>
    </td>

  </tr>
</template>

<style scoped>
.edit-user-btn {
  font-weight: 500;
  color: rgb(37 99 235);
  cursor: pointer;
}

.edit-user-btn:hover {
  text-decoration-line: underline;
}

.table-row {
  background-color: white;
  border-bottom-width: 1px;
}

.table-row:hover {
  background-color: rgb(249 250 251);
}
</style>
