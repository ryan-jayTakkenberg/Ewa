<template>
  <SolarModal title="Confirm order" @close-modal="onClose">
    <div class="p-4 md:p-5 text-center">
      <svg class="mx-auto mb-4 text-gray-400 w-12 h-12 dark:text-gray-200" aria-hidden="true"
           xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M10 11V6m0 8h.01M19 10a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
      </svg>
      <p>Are you sure you want to confirm the following order? :</p>
    </div>
    <p><br/><br/><strong>{{ order.orderNumber }}</strong><br/>{{ order.orderedFrom }}
      <br> {{ order.products }}</p>
    <!-- Modal footer -->
    <template v-slot:footer>
      <button @click="onClose" class="cancel-button">Cancel</button>
      <button @click="completeOrder" class="ml-auto confirm-button">Confirm Order</button>
    </template>
  </SolarModal>
</template>

<script>
import SolarModal from "@/components/general/SolarModal.vue";
import Order from "@/models/order";

export default {
  name: "ConfirmOrderModal",
  emits: ['confirm'],
  components: {SolarModal},
  props: {
    order: {
      type: Order,
      required: true,
    },
    onClose: {
      type: Function,
      required: true,
    },
  },
  methods: {
    completeOrder() {
      this.$emit('confirm', this.order.id);
    },
  }
};
</script>

<style scoped>
.confirm-button {
  color: white;
  background-color: #C7D02C;
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
}

.confirm-button:hover {
  background-color: #a3b825;
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
