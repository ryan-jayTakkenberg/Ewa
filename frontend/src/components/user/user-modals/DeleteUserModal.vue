<template>
<!--  if multiple users title is delete users-->
  <SolarModal title="Delete User" @close-modal="onClose">
    <p>Are you sure you want to delete the user:
      <br/><br/><strong>{{ user.name }}</strong><br/>{{ user.email }}?</p>

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
