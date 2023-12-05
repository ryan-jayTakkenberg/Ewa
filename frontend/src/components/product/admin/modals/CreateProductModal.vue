<template>
  <SolarModal title="Create Product" @close-modal="close">
    <!-- Modal body -->
    <div class="grid lg:grid-cols-2 gap-6">
      <div class="grid-col">
        <label for="name">Name</label>
        <input
            type="text"
            v-model="this.productInfo.name"
            class="p-2 border-gray-300 text-sm text-gray-800 bg-gray-50 shadow focus:border-blue-600"
            placeholder="Name">
      </div>
      <div class="grid-col">
        <label for="email">Price (€)</label>
        <input
            type="number"
            v-model="this.productInfo.price"
            class="p-2 border-gray-300 text-sm text-gray-800 bg-gray-50 shadow focus:border-blue-600"
            placeholder="Price">
      </div>
    </div>
    <div class="mt-6">
      <label for="email">Description</label>
      <textarea
          type="text"
          v-model="this.productInfo.description"
          class="resize-none overflow-y-scroll p-2 border-gray-300 text-sm text-gray-800 bg-gray-50 shadow focus:border-blue-600"
          placeholder="Description">
                </textarea>
    </div>
    <!-- Modal footer -->
    <template v-slot:footer>
      <button @click="close" class="text-gray-900 bg-gray-200 font-medium text-sm px-5 py-3 hover:bg-gray-300">Cancel</button>
      <ButtonComponent :disabled="!this.canCreate" :button-text="'Create Product'" @click="create" class="ml-auto" />
    </template>
  </SolarModal>
</template>

<script>
import Product from "@/models/productInfo";

import ButtonComponent from "@/components/general/SolarButton";
import SolarModal from "@/components/general/SolarModal";


export default {
  name: "EditProductModal",
  components: {
    ButtonComponent,
    SolarModal,
  },
  data() {
    return {
      keys: Object.keys(new Product()),
      productInfo: { id: -1 },
    }
  },
  computed: {
    canCreate() {
      for (let key of this.keys) {
        if (!this.productInfo[key] || !this.productInfo[key].toString().trim()) {
          return false;
        }
      }
      return this.productInfo.price > 0;
    }
  },
  methods: {
    close() {
      this.$emit('close');
    },
    create() {
      let productClass = new Product();
      productClass.injectAttributes(this.productInfo);
      this.$emit('create', productClass);
    },
  },
}
</script>

<style scoped>

</style>
