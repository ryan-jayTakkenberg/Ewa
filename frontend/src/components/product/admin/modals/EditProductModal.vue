<template>
  <!-- Edit modal -->
  <div class="fixed inset-0 flex justify-center overflow-hidden items-center bg-black bg-opacity-10 ml-16 sm:ml-0" tabindex="0">
    <div class="relative">
      <!-- Modal content -->
      <div class="bg-white rounded-2xl shadow-xl">
        <div class="border-gray-200 border-b p-6">
          <!-- Modal header -->
          <div class="flex justify-between">
            <h3 class="text-xl font-bold text-gray-900 ">{{ title }}</h3>
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
        </div>
        <div class="border-gray-200 border-t p-6">
          <button class="text-white bg-blue-700 py-2 px-4 font-medium hover:bg-blue-800" @click="save">Save Changes</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "EditProductModal",
  data() {
    return {
      cloned: {},
      title: "Edit Product"
    }
  },
  props: {
    products: {
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
    if (this.products.length === 1) {
      this.cloned = this.products[0].clone();
    } else {
      this.title += "s";
    }
  },
}
</script>

<style scoped>

</style>
