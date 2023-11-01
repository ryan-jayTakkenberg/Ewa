<script>
import User from "@/models/user";

export default {
  name: "UsersRowComponent",
  emits: ["on-click-edit-user", "on-click-delete-user", "toggle-checkbox"],
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
    onClickEditUser() {
      this.$emit('on-click-edit-user', this.user); // Emit the userModel directly
    },
    onClickDeleteUser() {
      this.$emit('on-click-delete-user', this.user); // Emit the userModel directly
    },
    toggleCheckbox() {
      this.$emit('toggle-checkbox', this.user.id);
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
            :checked="isChecked"
            @change="toggleCheckbox"
            class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded cursor-pointer">
      </div>
    </td>

    <!--User Item-->
    <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap">
      <div class="pl-3">
        <div class="text-base font-semibold">{{ user.name }}</div>
        <div class="font-normal text-gray-500">{{ user.email }}</div>
      </div>
    </th>
    <td class="px-6 py-4">{{ user.permissionLevel}}</td>
    <td class="px-6 py-4">
      <div class="flex items-center">{{ user.lastLoggedIn }}</div>
    </td>

    <!-- Edit User -->
    <td class="px-6 py-4 row">
      <div @click="onClickEditUser" class="edit-user-btn">Edit user</div>
      <div @click="onClickDeleteUser" class="delete-user-btn">Delete user</div>
    </td>

  </tr>
</template>

<style scoped>
.edit-user-btn {
  font-weight: 500;
  color: rgb(37 99 235);
  cursor: pointer;
}

.delete-user-btn {
  font-weight: 500;
  color: red;
  cursor: pointer;
}

.delete-user-btn:hover {
  text-decoration-line: underline;
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
