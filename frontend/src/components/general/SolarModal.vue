<template>
  <div class="modal">
    <div class="modal-container">
      <div class="modal-content shadow">
        <div class="modal-header">
          <h3 class="text-xl font-bold text-gray-900">{{ title }}</h3>
        </div>
        <div class="modal-body p-4">
          <slot></slot>
        </div>
        <div class="modal-footer">
          <div class="flex items-center p-6 border-t border-gray-200 rounded-b">
            <slot name="footer"></slot>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SolarModal",
  emits: ["close-modal"],
  props: {
    title: {
      type: String,
      required: true,
    },
  },
  methods: {
    closeModal() {
      this.$emit("close-modal");
    },
  },
};
</script>

<style scoped>
.modal {
  z-index: 100;
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow-x: hidden;
  overflow-y: auto;
  height: 100%;
  max-height: 100%;
  background-color: rgba(0, 0, 0, 0.1);
}

.modal-container {
  position: relative;
  margin-left: auto;
  width: calc(100% - 70px);
  max-height: 100%;
}

.modal-content {
  background-color: white;
  border-radius: 0.5rem;
  height: 100vh;
}

.modal-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 1rem;
  border-bottom-width: 1px;
}

.modal-body {
  padding: 1.5rem;
  margin-top: calc(1.5rem * calc(1 - var(--tw-space-y-reverse)));
  margin-bottom: calc(1.5rem * var(--tw-space-y-reverse));
}

@media (min-width: 768px) {
  .modal-container {
    width: 100%;
    padding: 4rem;
    margin-left: 0;
  }

  .modal-content {
    height: 100%;
  }
}
</style>
