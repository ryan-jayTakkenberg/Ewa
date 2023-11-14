<template>
  <!-- Edit modal -->
  <div class="fixed inset-0 flex justify-center overflow-hidden items-center bg-black bg-opacity-10" tabindex="0">
    <div class="relative">
      <!-- Modal content -->
      <div class="bg-white rounded-2xl shadow-xl">
        <!-- Modal header -->
        <div class="border-gray-200 border-b p-6">
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
        <!-- Modal body -->
        <div class="p-6">
          <p>You are about to delete:</p>
          <ul class="ml-1" v-html="typeMsg"></ul>
        </div>
        <!-- Modal footer -->
        <div class="border-gray-200 border-t p-6">
          <button class="text-white bg-red-600 py-2 px-4 font-medium hover:bg-red-700" @click="save">Delete Permanently</button>
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
      typeMsg: '',
      title: "Delete Product"
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
      this.$emit('delete');
    },
  },
  created() {
    if (this.products.length === 1) {
      this.typeMsg = `○<span class="text-red-700 font-bold ml-1">${this.products[0].name}</span><br />`;
      return;
    }

    for (let p of this.products) {
      this.typeMsg += `○<span class="text-red-700 font-bold ml-1">${p.name}</span><br />`;
    }
    this.title += "s";
  },
}
</script>

<style scoped>

</style>
