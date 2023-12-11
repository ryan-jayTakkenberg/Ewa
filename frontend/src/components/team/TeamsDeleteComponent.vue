<template>
  <div class="delete-user-modal" tabindex="0">
    <div class="delete-user-modal-container">
      <div class="delete-user-modal-content shadow">
        <div class="delete-user-modal-header">
          <h3 class="text-xl font-bold text-gray-900">Delete Team</h3>
          <button type="button" @click="onClose" class="close-modal-btn">
            <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
              <path
                  stroke="currentColor"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"
              />
            </svg>
          </button>
        </div>

        <div class="modal-body p-4">
          <p>Are you sure you want to delete the team: <strong>{{ team.name }}</strong>?</p>
        </div>

        <div class="flex items-center p-6 border-t border-gray-200 rounded-b">
          <button @click="onClose" class="cancel-button">Cancel</button>
          <button @click="deleteTeam" class="ml-auto delete-button">Delete</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Team from "../../models/team";

export default {
  name: "DeleteUserModal",
  inject: ["teamsService"],
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
    deleteTeam() {
      this.$emit('delete-team', this.team.id); // Emit an event to toggle the checked value
    },
  }
};
</script>

<style scoped>
.delete-user-modal {
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

.delete-user-modal-container {
  position: relative;
  margin-left: auto;
  width: calc(100% - 70px);
  max-height: 100%;
}

.delete-user-modal-content {
  position: relative;
  background-color: white;
  border-radius: 0.5rem;
}

.delete-user-modal-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 1rem;
  border-bottom-width: 1px;
  border-top-left-radius: 0.25rem;
  border-top-right-radius: 0.25rem;
}

.delete-button {
  color: white;
  background-color: rgb(220 38 38);
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
}

.delete-button:hover {
  background-color: rgb(185 28 28);
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

@media (min-width: 768px) {
  .delete-user-modal-container {
    width: 50%;
    padding: 4rem;
    margin-left: 0;
  }
}
</style>
