<template>
  <SolarModal title="Delete Users" @close-modal="onClose">
    <div>
      <p>Are you sure you want to delete the following users:</p>
      <div v-for="user in usersToDelete" :key="user.id">
        <br/><br/><strong>{{ user.name }}</strong><br/>{{ user.email }}
      </div>
    </div>
    <template v-slot:footer>
      <button @click="onClose" class="cancel-button">Cancel</button>
      <button @click="deleteUsers" class="ml-auto delete-button">Delete Users</button>
    </template>
  </SolarModal>
</template>

<script>
import SolarModal from "@/components/general/SolarModal.vue";

export default {
  name: "DeleteMultipleUsersModal",
  components: {SolarModal},
  props: {
    usersToDelete: {
      type: Array,
      required: true,
    },
    onClose: {
      type: Function,
      required: true,
    },
  },
  methods: {
    deleteUsers() {
      this.$emit("delete-users");
    },
  },
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
