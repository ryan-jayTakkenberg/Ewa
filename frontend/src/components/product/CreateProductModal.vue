<template>
  <!-- Edit modal -->
  <div class="fixed inset-0 flex justify-center overflow-hidden items-center bg-black bg-opacity-10 ml-16 sm:ml-0" tabindex="0">
    <div class="relative">
      <!-- Modal content -->
      <div class="bg-white rounded-2xl shadow-xl">
        <div class="border-gray-200 border-b p-6">
          <!-- Modal header -->
          <div class="flex justify-between">
            <h3 class="text-xl font-bold text-gray-900 ">Create Product</h3>
            <button type="button" @click="close" class="text-gray-500 hover:bg-gray-100 hover:text-black">
              <svg class="w-7 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
              </svg>
            </button>
          </div>
        </div>
        <div class="p-6">
          <!-- Modal body -->
          <div class="grid lg:grid-cols-2 gap-6">
            <div class="grid-col">
              <label for="name">Name</label>
              <input
                  type="text"
                  v-model="this.product.name"
                  class="p-2 border-gray-300 text-sm text-gray-800 bg-gray-50 shadow focus:border-blue-600"
                  placeholder="Name">
            </div>
            <div class="grid-col">
              <label for="email">Price (€)</label>
              <input
                  type="number"
                  v-model="this.product.price"
                  class="p-2 border-gray-300 text-sm text-gray-800 bg-gray-50 shadow focus:border-blue-600"
                  placeholder="Price">
            </div>
          </div>
          <div class="mt-6">
            <label for="email">Description</label>
            <input
                type="text"
                v-model="this.product.description"
                class="p-2 border-gray-300 text-sm text-gray-800 bg-gray-50 shadow focus:border-blue-600"
                placeholder="Description">
          </div>
          <!-- Modal footer -->
        </div>
        <div class="border-gray-200 border-t p-6">
          <ButtonComponent button-text="Create" class="text-white py-2 px-4 font-medium hover:bg-green-700" @click="create" :disabled="!canCreate"></ButtonComponent>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Product from "@/models/product";

import ButtonComponent from "@/components/general/SolarButton";

export default {
  name: "EditProductModal",
  components: {
    ButtonComponent
  },
  data() {
    return {
      keys: Object.keys(new Product()),
      product: { id: -1 },
    }
  },
  computed: {
    canCreate() {
      for (let key of this.keys) {
        if (!this.product[key] || !this.product[key].toString().trim()) {
          return false;
        }
      }
      return true;
    }
  },
  methods: {
    close() {
      this.$emit('close');
    },
    create() {
      let productClass = new Product();
      productClass.injectAttributes(this.product);
      this.$emit('create', productClass);
    },
  },
}
</script>

<style scoped>

</style>
