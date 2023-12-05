<script>
// import Team from "@/models/team";

export default {
  name: "AddTeamModal",
  emits: ['addUser'],

  data() {
    return {
      team: {
        id: '',
        name: '',
        warehouse: '',
        users: [],
      },
      idAutoIncrement: 12,
    };
  },
  props: {
    onClose: {
      type: Function,
      required: true,
    },
    warehouses: Array,
  },
  methods: {
    saveProject() {
      console.log("Saving team:", this.editedTeam);
      this.onClose();
    },
    saveTeam() {
      const newTeam = {
        name: this.team.name,
        warehouse: this.team.warehouse,
      };

      this.$emit('addUser', newTeam); // emit the custom 'addUser' event
      this.onClose();
    },
  },
};
</script>


<template>
  <!-- Create user modal -->
  <div class="create-user-modal" tabindex="0">
    <div class="create-user-modal-container">
      <!-- Modal content -->
      <form @submit.prevent="saveTeam" class="create-user-form shadow ">

        <!-- Modal header -->
        <div class="create-user-modal-header">
          <h3 class="text-xl font-bold text-gray-900">Create team</h3>
          <button type="button" @click="onClose" class="close-modal-btn">
            <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
            </svg>
          </button>
        </div>

        <!-- Modal body -->
        <div class="modal-body">
          <div class="modal-grid">

            <div class="col-span-6 sm:col-span-3">
              <label for="name" class="modal-label">Name</label>
              <input
                  type="text"
                  v-model="team.name"
                  class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600"
                  placeholder="Name"
                  required>
            </div>

            <div class="col-span-6 sm:col-span-3">
              <label for="warehouse" class="modal-label">Warehouse</label>
              <select v-model="team.warehouse" class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600">
                <option v-for="warehouse in warehouses" :key="warehouse.id" :value="warehouse">
                  {{ warehouse.name }}
                </option>
              </select>
            </div>


        </div>

        <!-- Modal footer -->
        <div class="flex items-center p-6 space-x-2 border-t border-gray-200 rounded-b">
          <button type="submit" class="submit-button">Create Team</button>
        </div>
    </div>
      </form>
  </div>
  </div>>
</template>

<style scoped>
.create-user-modal {
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow-x: hidden;
  overflow-y: auto;
  height: calc(100% - 1rem);
  max-height: 100%;
  background-color: rgba(0, 0, 0, 0.1);
}

.create-user-modal-container {
  position: relative;
  margin-left: auto;
  width: calc(100% - 70px);
  max-height: 100%;
}

.create-user-modal-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 1rem;
  border-bottom-width: 1px;
  border-top-left-radius: 0.25rem;
  border-top-right-radius: 0.25rem;
}

.modal-body {
  padding: 1.5rem;
  margin-top: calc(1.5rem * calc(1 - var(--tw-space-y-reverse)));
  margin-bottom: calc(1.5rem * var(--tw-space-y-reverse));
}

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

.submit-button {
  color: white;
  background-color: rgb(29 78 216);
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
}

.submit-button:hover {
  background-color: rgb(30 64 175);
}

.close-modal-btn {
  color: rgb(156 163 175);
  background-color: transparent;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  width: 2rem;
  height: 2rem;
  margin-left: auto;
  display: inline-flex;
  justify-content: center;
  align-items: center;
}

.close-modal-btn:hover {
  background-color: rgb(229 231 235);
  color: rgb(17 24 39);
}

.create-user-form {
  position: relative;
  background-color: white;
  border-radius: 0.5rem;
}

@media (min-width: 768px) {
  .create-user-modal-container {
    width: 50%;
    padding: 4rem;
    margin-left: 0;
  }
}
</style>

