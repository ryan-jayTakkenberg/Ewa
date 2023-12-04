<template>
  <SolarModal :title="this.title" @close-modal="close">
    <div class="p-4 md:p-5 text-center">
      <svg class="mx-auto mb-4 text-gray-400 w-12 h-12 dark:text-gray-200" aria-hidden="true"
           xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M10 11V6m0 8h.01M19 10a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/>
      </svg>
      <p>Are you sure you want to delete the following:</p>
      <br />
      <ul class="col ml-1" v-html="typeMsg"></ul>
    </div>
    <!-- Modal footer -->
    <template v-slot:footer>
      <button @click="close" class="text-gray-900 bg-gray-200 font-medium text-sm px-5 py-3 hover:bg-gray-300">Cancel</button>
      <button @click="remove" class="ml-auto text-white bg-red-600 font-medium text-sm px-5 py-3 hover:bg-red-700">{{ this.title }}</button>
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
.delete-button {
  color: white;
  background-color: rgb(220 38 38);
  font-weight: 500;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  line-height: 1.25rem;
  padding: 0.625rem 1.25rem;
  text-align: center;
}

.delete-button:hover {
  background-color: rgb(185 28 28);
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
