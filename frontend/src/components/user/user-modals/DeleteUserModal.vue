<template>
  <SolarModal title="Delete User" @close-modal="onClose">
    <div class="p-4 md:p-5 text-center">
      <svg class="mx-auto mb-4 text-gray-400 w-12 h-12 dark:text-gray-200" aria-hidden="true"
           xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M10 11V6m0 8h.01M19 10a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
      </svg>
      <p>Are you sure you want to delete the user:</p>
    </div>
    <p><br/><br/><strong>{{ user.name }}</strong><br/>{{ user.email }}</p>
    <!-- Modal footer -->
    <template v-slot:footer>
      <button @click="onClose" class="cancel-button">Cancel</button>
      <button @click="deleteUser" class="ml-auto delete-button">Delete User</button>
    </template>
  </SolarModal>
</template>

<script>
import User from "@/models/user";
import SolarModal from "@/components/general/SolarModal.vue";

export default {
  name: "DeleteUserModal",
  components: {SolarModal},
  props: {
    user: {
      type: User,
      required: true,
    },
    onClose: {
      type: Function,
      required: true,
    },
  },
  methods: {
    deleteUser() {
      this.$emit('delete-user', this.user.id); // Emit an event to toggle the checked value
    },
  }
};
</script>

<style scoped>
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
</style>
