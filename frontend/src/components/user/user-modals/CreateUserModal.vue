<template>
  <form @submit.prevent="createUser" @keydown.enter.prevent="">
    <SolarModal title="Create User" @close-modal="onClose" class="modal">
      <div class="modal-grid">
        <div class="col-span-6 sm:col-span-3">
          <label for="name" class="modal-label">Name</label>
          <input
              type="text"
              v-model="user.name"
              class="modal-input shadow-sm"
              placeholder="Name"
              required>
        </div>

        <div class="col-span-6 sm:col-span-3">
          <label for="email" class="modal-label">Email</label>
          <input
              type="email"
              v-model="user.email"
              class="modal-input shadow-sm"
              placeholder="Email"
              required>
        </div>

        <div class="col-span-6 sm:col-span-3">
          <label for="user-role" class="modal-label">Function</label>
          <select v-model="user.permissionLevel" class="role-select" required>
            <option v-for="permissionLevel in PermissionLevelOptions" :key="permissionLevel" :value="permissionLevel">
              {{ permissionLevel }}
            </option>
          </select>
        </div>

        <div v-if="user.permissionLevel === User.PermissionLevel.VIEWER" class="col-span-6 sm:col-span-3">
          <label for="team" class="modal-label">Team</label>
          <select v-model="user.team" class="role-select" required>
            <option v-for="team in TeamOptions" :key="team.id" :value="team">{{ team.name }}</option>
          </select>
        </div>

        <div class="col-span-6 sm:col-span-3">
          <label for="password" class="modal-label">Password</label>
          <input
              v-model="user.password"
              type="password"
              placeholder="Password"
              class="modal-input shadow-sm"
              required>
        </div>
      </div>

      <!-- Modal footer -->
      <template v-slot:footer>
        <button @click="onClose" class="cancel-button">Cancel</button>
        <button type="submit" class="ml-auto submit-button">Create User</button>
      </template>
    </SolarModal>
  </form>
</template>

<script>
import User from "@/models/user";
import SolarModal from "@/components/general/SolarModal.vue";
import Team from "@/models/team";
import {classToObject} from "@/models/helper";

export default {
  name: "CreateUserModal",
  computed: {
    User() {
      return User
    }
  },
  components: {SolarModal},
  data() {
    return {
      PermissionLevelOptions: User.PermissionLevel,
      user: {
        email: '',
        name: '',
        permissionLevel: '',
        password: '',
        team: null,
      },
      TeamOptions: Team.teams,
      passwordRequirements: {
        minLength: 8, // Minimum password length
        hasUppercase: false, // Whether it requires an uppercase letter
        hasLowercase: false, // Whether it requires a lowercase letter
        hasNumber: false, // Whether it requires a number
        hasSpecialChar: false, // Whether it requires a special character
      },
      passwordIsValid: false,
    };
  },
  props: {
    onClose: {
      type: Function,
      required: true,
    },
  },
  methods: {
    createUser() {
      let newUser = User.createNewUser();
      newUser.injectAttributes(this.user);
      let json = classToObject(newUser);
      json.teamId = json.team?.id ?? -1;
      this.$emit('create-user', json);
    },
  }
}
</script>

<style scoped>
.modal-grid {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 1.5rem;
}

.modal-label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  font-weight: 500;
  color: rgb(17 24 39);
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

.role-select {
  display: block;
  width: 100%;
  padding: 0.75rem 1rem;
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
  opacity: 40%;
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
}

.submit-button:hover {
  background-color: #a3b825;;
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

@media (max-width: 768px) {
  .modal-grid {
    grid-template-columns: repeat(1, minmax(0, 1fr));
  }
}
</style>

