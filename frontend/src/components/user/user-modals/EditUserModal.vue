<script>

import User from "@/models/user";
import SolarModal from "@/components/general/SolarModal.vue";
import Team from "@/models/team";

export default {
  name: "EditUserModal",
  components: {SolarModal},
  emits: ['edit-user'],
  data() {
    return {
      PermissionLevelOptions: User.PermissionLevel,
      clonedUser: null,
      currentPasswordVisible: false,
      newPasswordVisible: false,
      TeamOptions: Team.teams,
    }
  },

  props: {
    user: {
      type: Object,
      required: true,
    },
    onClose: {
      type: Function,
      required: true,
    },
  },
  created() {
    this.clonedUser = { ...this.user };
  },
  computed: {
    hasChanged() {
      if (!this.clonedUser) {
        return false; // No changes if clonedUser is null
      }

      for (const key in this.user) {
        if (this.user[key] !== this.clonedUser[key]) {
          return true;
        }
      }
      return false;
    },
  },

  methods: {
    editUser() {
      this.$emit('edit-user', this.clonedUser); // Emit the updated scooter
    },
  },
}
</script>

<template>
  <form @submit.prevent="editUser" @keydown.enter.prevent="">
    <SolarModal title="Edit User" @close-modal="onClose">
      <div class="modal-grid">
        <div class="col-span-6 sm:col-span-3">
          <label for="name" class="modal-label ">Name</label>
          <input
              type="text"
              v-model="this.clonedUser.name"
              class="modal-input shadow-sm"
              placeholder="Name"
              required>
        </div>

        <div class="col-span-6 sm:col-span-3">
          <label for="email" class="modal-label">Email</label>
          <input
              type="email"
              v-model="this.clonedUser.email"
              class="modal-input shadow-sm"
              placeholder="Email"
              required>
        </div>
        <div class="col-span-6 sm:col-span-3">
          <label for="user-role" class="modal-label">Function</label>
          <select v-model="this.clonedUser.permissionLevel" class="role-select">
            <option v-for="permissionLevel in PermissionLevelOptions" :key="permissionLevel"
                    :value="permissionLevel">{{ permissionLevel }}
            </option>
          </select>
        </div>

        <div v-if="user.permissionLevel === PermissionLevelOptions.VIEWER" class="col-span-6 sm:col-span-3">
          <label for="user-role" class="modal-label">Team</label>
          <select v-model="this.clonedUser.team" class="role-select">
            <option v-for="team in TeamOptions" :key="team.id" :value="team">{{ team.name }}
            </option>
          </select>
        </div>
      </div>

      <!-- Modal footer -->
      <template v-slot:footer>
        <button @click="onClose" class="cancel-button">Cancel</button>
        <button type="submit" class="submit-button" :disabled="!hasChanged">Save changes
        </button>
      </template>
    </SolarModal>
  </form>
</template>

<style scoped>
.role-select {
  display: block;
  width: 100%;
  padding: 0.75rem 1rem;;
  font-size: 1rem;
  line-height: 1.5rem;
  color: rgb(17 24 39);
  border-width: 1px;
  border-color: rgb(209 213 219);
  border-radius: 0.5rem;
  background-color: rgb(249 250 251);
  height: 42px;
  cursor: pointer;
}

.submit-button:disabled, .submit-button:disabled:hover {
  opacity: 0.4;
  cursor: not-allowed; /* Change the cursor to not-allowed */
}

.submit-button {
  color: white;
  background-color: #C7D02C;
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
  margin-left: auto;
}

.submit-button:hover {
  background-color: #a3b825;
}

.modal-input {
  background-color: rgb(249 250 251);
  border-width: 1px;
  border-color: rgb(209 213 219);
  color: rgb(17 24 39);
  font-size: 0.875rem;
  line-height: 1.25rem;
  border-radius: 0.5rem;
  display: block;
  width: 100%;
  padding: 0.625rem;
}

.modal-label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  font-weight: 500;
  color: rgb(17 24 39);
}

.modal-grid {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 1.5rem;
}

.cancel-button {
  color: rgb(17 24 39);
  background-color: rgb(229 231 235);
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
}

.cancel-button:hover {
  background-color: rgb(206 212 218);
}
</style>
