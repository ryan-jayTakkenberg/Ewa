<template>
<!--  &lt;!&ndash; Edit modal &ndash;&gt;-->
<!--  <div class="fixed inset-0 flex justify-center overflow-hidden items-center bg-black bg-opacity-10" tabindex="0">-->
<!--    <div class="relative">-->
<!--      &lt;!&ndash; Modal content &ndash;&gt;-->
<!--      <div class="bg-white rounded-2xl shadow-xl">-->
<!--        &lt;!&ndash; Modal header &ndash;&gt;-->
<!--        <div class="border-gray-200 border-b p-6">-->
<!--          <div class="flex justify-between">-->
<!--            <h3 class="text-xl font-bold text-gray-900 ">{{ title }}</h3>-->
<!--            <button type="button" @click="close" class="text-gray-500 hover:bg-gray-100 hover:text-black">-->
<!--              <svg class="w-7 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">-->
<!--                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"-->
<!--                      d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>-->
<!--              </svg>-->
<!--            </button>-->
<!--          </div>-->
<!--        </div>-->
<!--        &lt;!&ndash; Modal body &ndash;&gt;-->
<!--        <svg class="mx-auto mb-4 text-gray-400 w-12 h-12 dark:text-gray-200" aria-hidden="true"-->
<!--             xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">-->
<!--          <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"-->
<!--                d="M10 11V6m0 8h.01M19 10a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>-->
<!--        </svg>-->
<!--        <div class="p-6">-->
<!--          <p>Are you sure you want to delete the following:</p>-->
<!--          <ul class="ml-1" v-html="typeMsg"></ul>-->
<!--        </div>-->
<!--        &lt;!&ndash; Modal footer &ndash;&gt;-->
<!--        <div class="border-gray-200 border-t p-6">-->
<!--          <button class="text-white bg-red-600 py-2 px-4 font-medium hover:bg-red-700" @click="save">Delete Permanently</button>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
  <SolarModal :title="this.title" @close-modal="close">
    <div class="p-4 md:p-5 text-center">
      <svg class="mx-auto mb-4 text-gray-400 w-12 h-12 dark:text-gray-200" aria-hidden="true"
           xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M10 11V6m0 8h.01M19 10a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
      </svg>
      <p>Are you sure you want to delete the following:</p>
    </div>
    <ul class="ml-1" v-html="typeMsg"></ul>
    <!-- Modal footer -->
    <template v-slot:footer>
      <button @click="close" class="cancel-button">Cancel</button>
      <button @click="remove" class="ml-auto delete-button">Delete Product</button>
    </template>
  </SolarModal>
</template>

<script>
import SolarModal from "@/components/general/SolarModal";

export default {
  name: "EditProductModal",
  components: {
    SolarModal
  },
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
    remove() {
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
    this.title += this.title.endsWith('s') ? "'" : "s";
  },
}
</script>

<style scoped>

</style>
