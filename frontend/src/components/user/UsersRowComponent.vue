<script>
import User from "@/models/user";

export default {
  name: "UsersRowComponent",
  emits: ["toggle", "edit", "delete"],
  data() {
    return {
      checked: false,
    };
  },
  props: {
    user: {
      type: User,
      required:true,
    },
  },
  methods: {
    emitToggle() {
      console.log(this.user);
      this.$emit("toggle", this.user);
    },
    emitEdit() {
      this.$emit("edit", this.user);
    },
    emitDelete() {
      this.$emit("delete", this.user);
    },
  }
}
</script>

<template>
  <tr class="table-row">
    <!-- Check box  -->
    <td class="w-2 p-4">
      <div class="flex items-center ">
        <input
            type="checkbox"
            v-model="checked" @change="emitToggle"
            class="w-4 h-4 border-gray-300 rounded">
      </div>
    </td>
    <!--User Item-->
    <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap">
      <div class="pl-3">
        <div class="text-base font-semibold">{{ user.name }}</div>
        <div class="font-normal text-gray-500">{{ user.email }}</div>
      </div>
    </th>
    <td class="px-6 py-4">{{ user.permissionLevel }}</td>
    <td class="px-6 py-4">{{ user.team?.name }}</td>
    <td class="px-6 py-4">
      <div class="flex items-center">{{ user.lastLogin  == null ? "Not logged in yet" : user.lastLogin}}</div>
    </td>
    <!-- Edit User -->
    <td class="px-6 py-4 row">
      <div @click="emitEdit" class="edit-user-btn">Edit user</div>
      <div @click="emitDelete" class="delete-user-btn">Delete user</div>
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
