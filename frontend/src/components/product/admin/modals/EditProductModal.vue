<template>
  <SolarModal title="Edit Product" @close-modal="close">
    <!-- Modal body -->
    <div class="grid lg:grid-cols-2 gap-6">
      <div class="grid-col">
        <label for="name">Name</label>
        <input
            type="text"
            v-model="this.cloned.name"
            class="p-2 border-gray-300 text-sm text-gray-800 bg-gray-50 shadow focus:border-blue-600"
            placeholder="Name">
      </div>
      <div class="grid-col">
        <label for="email">Price (€)</label>
        <input
            type="number"
            v-model="this.cloned.price"
            class="p-2 border-gray-300 text-sm text-gray-800 bg-gray-50 shadow focus:border-blue-600"
            placeholder="Price">
      </div>
    </div>
    <div class="mt-6">
      <label for="email">Description</label>
      <textarea
          type="text"
          v-model="this.cloned.description"
          class="resize-none overflow-y-scroll p-2 border-gray-300 text-sm text-gray-800 bg-gray-50 shadow focus:border-blue-600"
          placeholder="Description">
            </textarea>
    </div>
    <!-- Modal footer -->
    <template v-slot:footer>
      <button @click="close" class="text-gray-900 bg-gray-200 font-medium text-sm px-5 py-3 hover:bg-gray-300">Cancel</button>
      <button @click="save" class="ml-auto text-white bg-blue-700 font-medium text-sm px-5 py-3 hover:bg-blue-800">Save Changes</button>
    </template>
  </SolarModal>
</template>

<script>
import SolarModal from "@/components/general/SolarModal";

export default {
  name: "EditProductModal",
  components: {
    SolarModal,
  },
  data() {
    return {
      cloned: {},
    }
  },
  props: {
    productInfos: {
      type: Array,
      required: true,
    }
  },
  methods: {
    close() {
      this.$emit('close');
    },
    save() {
      this.$emit('save', this.cloned);
    },
  },
  created() {
    if (this.productInfos.length === 1) {
      this.cloned = this.productInfos[0].clone();
    } else {
      this.title += "s";
    }
  },
}
</script>

<style scoped>

</style>
