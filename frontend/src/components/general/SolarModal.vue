<template>
  <div class="modal">
    <div class="modal-container">
      <div class="modal-content shadow">
        <div class="border-gray-200 border-b p-6">
          <div class="flex justify-between">
            <h3 class="text-xl font-bold text-gray-900 ">{{ title }}</h3>
            <button type="button" @click="closeModal" class="text-gray-500 hover:bg-gray-100 hover:text-black">
              <svg class="w-7 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
              </svg>
            </button>
          </div>
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
  margin-left: 0;
  width: 100%;
  padding: 1rem;
}

.modal-content {
  background-color: white;
  border-radius: 0.5rem;
}

.modal-body {
  padding: 1.5rem;
  margin-top: calc(1.5rem * calc(1 - var(--tw-space-y-reverse)));
  margin-bottom: calc(1.5rem * var(--tw-space-y-reverse));
}

@media (min-width: 768px) {
  .modal-container {
    width: 50%;
    margin-left: 0;
  }

  .modal-content {
    height: 100%;
  }
}
</style>
