<script>

import Team from "@/models/team";


export default {
  name: "EditTeamModal",
  data() {
    return {
      editedTeam: null
    }
  },
  props: {
    team: {
      type: Team,
      required: true,
    },
    onClose: {
      type: Function,
      required: true,
    },
  },
  methods: {
    saveProject() {
      // Save the edited user and close the modal
      // Implement the logic to save the edited user details
      console.log("Saving team:", this.editedTeam);
      this.onClose();
    }
  },
  created() {
    this.editedTeam = this.team.clone();
    console.log(this.editedTeam);
  },

}
</script>

<template>
  <!-- Edit user modal -->
  <div class="edit-user-modal" tabindex="0">
    <div class="edit-user-modal-container">
      <!-- Modal content -->
      <form @submit.prevent="saveProject" class="edit-user-form shadow ">
        <!-- Modal header -->
        <div class="edit-user-modal-header">
          <h3 class="text-xl font-bold text-gray-900 ">Edit project</h3>
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
              <label for="name" class="modal-label ">Team name</label>
              <input type="text"
                     v-model="this.team.name"
                     class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600"
                     :placeholder="team.name" required>
            </div>
            <div class="col-span-6 sm:col-span-3">
              <label for="email" class="modal-label">warehouse</label>
              <input
                  type="text"
                  v-model="this.team.warehouse"
                  class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600"
                  required>
            </div>
            <div class="col-span-6 sm:col-span-3">
              <label for="dateOfInstallation" class="modal-label">project</label>
              <input
                  type="text"
                  v-model="this.team.project"
                  class="modal-input shadow-sm focus:ring-blue-600 focus:border-blue-600"
                  required
              >
            </div>

          </div>
        </div>
        <!-- Modal footer -->
        <div class="flex items-center p-6 space-x-2 border-t border-gray-200 rounded-b">
          <button type="submit"
                  class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center">
            Save all
          </button>
        </div>
      </form>
    </div>
  </div>
  <div class="modal-label"></div>


</template>

<style scoped>

.modal-input {
  background-color: rgb(249 250 251);
  border-width: 1px;
  border-color: rgb(209 213 219);
  color: rgb(17 24 39);
  font-size: 0.875rem /* 14px */;
  line-height: 1.25rem /* 20px */;
  border-radius: 0.5rem /* 8px */;
  display: block;
  width: 100%;
  padding: 0.625rem /* 10px */;
}

.modal-label {
  display: block;
  margin-bottom: 0.5rem /* 8px */;
  font-size: 0.875rem /* 14px */;
  line-height: 1.25rem /* 20px */;
  font-weight: 500;
  color: rgb(17 24 39);
}


.modal-body {
  padding: 1.5rem /* 24px */;
  --tw-space-y-reverse: 0;
  margin-top: calc(1.5rem * calc(1 - var(--tw-space-y-reverse)));
  margin-bottom: calc(1.5rem * var(--tw-space-y-reverse));
}

.modal-grid {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 1.5rem /* 24px */;
}

.close-modal-btn {
  color: rgb(156 163 175);
  background-color: transparent;
  border-radius: 0.5rem /* 8px */;
  font-size: 0.875rem /* 14px */;
  line-height: 1.25rem /* 20px */;
  width: 2rem /* 32px */;
  height: 2rem /* 32px */;
  margin-left: auto;
  display: inline-flex;
  justify-content: center;
  align-items: center;
}

.close-modal-btn:hover {
  background-color: rgb(229 231 235);
  color: rgb(17 24 39);
}

.edit-user-form {
  position: relative;
  background-color: white;
  border-radius: 0.5rem;
}

.edit-user-modal {
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

.edit-user-modal-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 1rem /* 16px */;
  border-bottom-width: 1px;
  border-top-left-radius: 0.25rem /* 4px */;
  border-top-right-radius: 0.25rem /* 4px */;
}

.edit-user-modal-container {
  position: relative;
  margin-left: auto;
  width: calc(100% - 70px);
  max-height: 100%;
}

@media (min-width: 768px) {
  .edit-user-modal-container {
    width: 50%;
    padding: 4rem;
    margin-left: 0;
  }
}
</style>
