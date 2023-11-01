<template>
  <div class="modal">
    <div class="modal-container">
      <div class="modal-content shadow">

        <div class="modal-header">
          <h3 class="text-xl font-bold text-gray-900">{{ title }}</h3>
          <button type="button" @click="closeModal" class="close-modal-btn">
            <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
            </svg>
          </button>
        </div>

        <div class="modal-body p-4">
          <slot></slot>
        </div>

        <div class="modal-footer">
          <div class="flex items-center p-6  border-t border-gray-200 rounded-b">
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

.modal-container {
  position: relative;
  margin-left: auto;
  width: calc(100% - 70px);
  max-height: 100%;
}

.modal-content{
  background-color: white;
  border-radius: 0.25rem;
}

.modal-header {
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


@media (min-width: 768px) {
  .modal-container {
    width: 60%;
    padding: 4rem;
    margin-left: 0;
  }
}
</style>
