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
        <div class="flex">
          <SolarDropdownMenuButton text-button="Action" :disabled="!checkedProducts.length">
            <SolarDropdownMenuItem text-menu-item="Edit Users" @click="openEditModal(getSelected())" />
            <SolarDropdownMenuItem text-menu-item="Delete Users" @click="openDeleteModal(getSelected())" />
          </SolarDropdownMenuButton>
          <ButtonComponent button-text="Create New Product" @click="openCreateModal" />
        </div>
      </div>
      <SolarTable :columns="['product', 'price', '']">
        <ProductRowComponent
            ref="rowComponent"
            v-for="(product, index) in filteredProducts"
            :key="index"
            :product="product"
            @edit="openEditModal"
            @delete="openDeleteModal"
            @toggle="toggleCheckbox(product, $event)"> <!-- Pass user and checkbox state -->
        </ProductRowComponent>
      </SolarTable>
    </div>
  </div>

  <!-- Modals -->
  <CreateProductModal v-if="modal === 'create'" @close="closeModal" @create="create" />
  <EditProductModal v-if="modal === 'edit'" :products="selectedProducts" @close="closeModal" @save="edit" />
  <DeleteProductModal v-if="modal === 'delete'" :products="selectedProducts" @close="closeModal" @delete="remove" />
</template>

<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import ButtonComponent from "@/components/general/SolarButton.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import ProductRowComponent from "@/components/product/ProductRowComponent";
import DeleteProductModal from "@/components/product/DeleteProductModal";
import EditProductModal from "@/components/product/EditProductModal";
import CreateProductModal from "@/components/product/CreateProductModal";
import Product from "@/models/product";

export default {
  name: "UsersOverview",
  components: {
    ProductRowComponent,
    SolarDropdownMenuItem,
    SolarDropdownMenuButton,
    SolarTable,
    TitleComponent,
    SearchBarComponent,
    ButtonComponent,
    DeleteProductModal,
    EditProductModal,
    CreateProductModal,
  },
  data() {
    return {
      products: [...Product.products],
      modal: '',
      selectedProducts: [],  // Track the selected product
      filterValue: '', // Store the input value for searching
      checkedProducts: [], // A list of the selected ID's
    };
  },
  computed: {
    filteredProducts() {
      return this.products.filter(p => {
        for (let key of Object.keys(p)) {
          if (`${p[key]}`.toLowerCase().includes(this.filterValue)) {
            return true;
          }
        }
        return false;
      });
    },
  },
  created() {
    if (!this.products?.length) {
      // Keep updating the list if the database has not returned all the data yet
      const fetchingInterval = setInterval(() => {
        if (!Product.fetching) {
          this.products = [...Product.products];
          clearInterval(fetchingInterval);
        }
      }, 100);
    }
  },
  methods: {
    closeModal() {
      this.modal = '';
      this.checkedProducts = [];
      if (this.$refs.rowComponent instanceof Array) {
        this.$refs.rowComponent.forEach(row => row.checked = false);
      }
    },
    async edit(updated) {
      for (let edited of this.selectedProducts) {
        let index = this.products.findIndex(p => p.id === edited.id);
        edited.injectAttributes(updated);
        this.products[index] = await edited.putDatabase();
      }
      this.closeModal();
    },
    async remove() {
      this.closeModal();
      this.products = this.products.filter(product => !this.selectedProducts.find(deleted => product.id === deleted.id));
      for (let deleted of this.selectedProducts) {
        await deleted.delDatabase();
      }
    },
    async create(product) {
      this.closeModal();
      this.products.push(await product.putDatabase());
    },
    handleSearchChange(value) {
      this.filterValue = value.trim().toLowerCase();
    },
    openDeleteModal(thing) {
      this.modal = "delete";
      if (thing instanceof Array) {
        this.selectedProducts = thing;
      } else {
        this.selectedProducts = [thing];
      }
    },
    openEditModal(thing) {
      this.modal = "edit";
      if (thing instanceof Array) {
        this.selectedProducts = thing;
      } else {
        this.selectedProducts = [thing];
      }
    },
    openCreateModal() {
      this.modal = "create";
    },
    toggleCheckbox(product, isChecked) {
      if (isChecked) {
        this.checkedProducts.push(product.id);
      } else {
        this.checkedProducts = this.checkedProducts.filter(id => id !== product.id);
      }
    },
    getSelected() {
      return this.products.filter(p => this.checkedProducts.includes(p.id));
    }
  },
}
</script>

<style scoped>

</style>
