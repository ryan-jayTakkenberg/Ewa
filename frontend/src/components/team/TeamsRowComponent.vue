<script>

import Team from "@/models/team";

export default {
  name: "teamsRowComponent",
  inject: ["teamsService"],
  props: {
    teams: {
      type: Team,
      required: true,
    },
    isChecked: {
      type: Boolean,
      default: false,
      required: true,
    },
  }, data() {
    return {
      checked: this.isChecked,// for two way binding
    }
  },
  methods: {
    editTeam() {
      this.$emit('click-edit-team', this.teams);  // Emit the userModel directly
    },
    toggleCheckbox() {
      // Emit an event to toggle the isChecked value
      this.$emit('toggle-checkbox', !this.isChecked);
    },
    deleteTeam(){
      this.$emit('click-delete-team', this.teams);
    },

  }
}
</script>

<template>
  <tr class="table-row">
    <!--User Item-->
    <th scope="row" class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap ">
      <div class="pl-3">
        <div class="text-base font-semibold py-4">{{ teams.name }}</div>
        <div class="font-normal text-gray-500">{{ teams.users }}</div>
      </div>
    </th>
    <td class="px-6 py-4">{{ teams.warehouse ? teams.warehouse.name : 'Nog geen warehouse' }}</td>
    <td class="px-6 py-4">
      <div class="flex items-center">{{ teams.projectCount }}</div>
    </td>

    <!-- Edit User -->
    <td class="px-6 py-4">
      <div @click="editTeam" class="edit-team-btn">Edit team</div>
      <div @click="deleteTeam" class="delete-team-btn">Delete team</div>
    </td>

  </tr>
</template>

<style scoped>
.edit-team-btn {
  font-weight: 500;
  color: rgb(37 99 235);
  cursor: pointer;
}

.edit-team-btn:hover {
  text-decoration-line: underline;
}
.delete-team-btn {
  font-weight: 500;
  color: red;
  cursor: pointer;
}

.delete-team-btn:hover {
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
