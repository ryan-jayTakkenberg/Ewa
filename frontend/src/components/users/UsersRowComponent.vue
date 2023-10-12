<script>
export default {
  name: "UsersRowComponent",
  props: {
    userName:{
      type: String,
      required: true,
    },
    userEmail:{
      type: String,
      required: true,
    },
    userFunction:{
      type: String,
      required: true,
    },
    userLastLoggedIn: {
      type: String,
      required: true,
    },
    onClickEditUser: {
      type: Function,
      required: true,
    },

  }
}
</script>

<template>
  <tr class="table-row">
    <td class="w-4 p-4">
      <div class="flex items-center">
        <input type="checkbox"
               class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:ring-2 ">
      </div>
    </td>

    <!--User Item-->
    <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap ">
      <div class="pl-3">
        <div class="text-base font-semibold">{{ userName }}</div>
        <div class="font-normal text-gray-500">{{ userEmail }}</div>
      </div>
    </th>

    <td class="px-6 py-4">
      {{ userFunction }}
    </td>

    <td class="px-6 py-4">
      <div class="flex items-center">
        {{userLastLoggedIn }}
      </div>
    </td>

    <td class="px-6 py-4">
      <!-- Modal toggle -->
      <div @click="onClickEditUser" type="button" class="font-medium text-blue-600 hover:underline">Edit user
      </div>
    </td>

  </tr>
</template>

<style scoped>
.table-row {
  background-color: white;
  border-bottom-width: 1px;
}

.table-row:hover{
  background-color: rgb(249 250 251);
}
</style>
