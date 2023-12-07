<template>
  <div class="p-4">
    <TitleComponent page-title="Products" />
  </div>

  <div class="">
    <div class="">
      <div class="flex justify-between mb-4 mx-4">
        <div>
          <SearchBarComponent place-holder="Search For Products" @search="handleSearchChange" />
        </div>
      </div>
      <SolarTable :columns="['product', 'price', '']">
        <ProductRowComponentViewer
            ref="rowComponent"
            v-for="(product, index) in filteredProducts"
            :key="index"
            :productInfo="product">
        </ProductRowComponentViewer>
      </SolarTable>
    </div>
  </div>
</template>

<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import ProductRowComponentViewer from "@/components/product/viewer/ProductRowComponentViewer";
import Product from "@/models/product";

export default {
  name: "UsersOverview",
  components: {
    ProductRowComponentViewer,
    SolarTable,
    TitleComponent,
    SearchBarComponent,
  },
  data() {
    return {
      modal: '',
      selectedProducts: [],  // Track the selected productInfo
      filterValue: '', // Store the input value for searching
      checkedProducts: [], // A list of the selected ID's
    };
  },
  computed: {
    filteredProducts() {
      return Product.products.filter(p => {
        for (let key of Object.keys(p)) {
          if (`${p[key]}`.toLowerCase().includes(this.filterValue)) {
            return true;
          }
        }
        return false;
      });
    },
  },
  methods: {
    closeModal() {
      this.modal = '';
      this.checkedProducts = [];
      if (this.$refs.rowComponent instanceof Array) {
        this.$refs.rowComponent.forEach(row => row.checked = false);
      }
    },
    handleSearchChange(value) {
      this.filterValue = value.trim().toLowerCase();
    },
  },
}
</script>

<style scoped>

</style>
