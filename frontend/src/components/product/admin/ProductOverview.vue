<template>

  <NotificationComponent ref="notificationComponent" />

  <TitleComponent class="header" page-title="Products"></TitleComponent>

  <div class="body">
    <div class="body-container">
      <div class="action-row">
        <SolarDropdownMenuButton text-button="Action" :disabled="!checkedProducts.length">
          <SolarDropdownMenuItem text-menu-item="Edit Products" @click="openEditModal(getSelected())"/>
          <SolarDropdownMenuItem text-menu-item="Delete Products" @click="openDeleteModal(getSelected())"/>
        </SolarDropdownMenuButton>
        <SearchBarComponent place-holder="Search For Products" @search="handleSearchChange"/>
        <SolarButton class="ml-auto mr-2" button-text="Create New Product" @click="openCreateModal"/>
      </div>

      <SolarTable :columns="['', 'Product', 'Price', 'Action']">
        <ProductRowComponent
            ref="rowComponent"
            v-for="(product, index) in filteredProducts"
            :key="index"
            :productInfo="product"
            @edit="openEditModal"
            @delete="openDeleteModal"
            @toggle="toggleCheckbox(product, $event)">
        </ProductRowComponent>
      </SolarTable>
    </div>
  </div>

  <!-- Modals -->
  <CreateProductModal v-if="modal === 'create'" @close="closeModal" @create="create"/>
  <EditProductModal v-if="modal === 'edit'" :productInfos="selectedProducts" @close="closeModal" @save="edit"/>
  <DeleteProductModal v-if="modal === 'delete'" :productInfos="selectedProducts" @close="closeModal" @delete="remove"/>
</template>

<script>
import TitleComponent from "@/components/general/SolarTitle.vue";
import SearchBarComponent from "@/components/general/SolarSearchbar.vue";
import SolarTable from "@/components/general/SolarTable.vue";
import SolarDropdownMenuButton from "@/components/general/SolarDropdownMenuButton.vue";
import SolarDropdownMenuItem from "@/components/general/SolarDropdownMenuItem.vue";
import ProductRowComponent from "@/components/product/admin/ProductRowComponent";
import DeleteProductModal from "@/components/product/admin/modals/DeleteProductModal";
import EditProductModal from "@/components/product/admin/modals/EditProductModal";
import CreateProductModal from "@/components/product/admin/modals/CreateProductModal";
import Product from "@/models/product";
import SolarButton from "@/components/general/SolarButton.vue";
import NotificationComponent from "@/components/general/NotificationComponent.vue";

export default {
  name: "UsersOverview",
  components: {
    NotificationComponent,
    SolarButton,
    ProductRowComponent,
    SolarDropdownMenuItem,
    SolarDropdownMenuButton,
    SolarTable,
    TitleComponent,
    SearchBarComponent,
    DeleteProductModal,
    EditProductModal,
    CreateProductModal,
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
    updateTable() {
      this.filterValue += ' ';
      this.filterValue = this.filterValue.trim();
    },
    closeModal() {
      this.modal = '';
      this.checkedProducts = [];
      if (this.$refs.rowComponent instanceof Array) {
        this.$refs.rowComponent.forEach(row => row.checked = false);
      }
    },
    async edit(updated) {
      this.closeModal();
      for (let edited of this.selectedProducts) {
        edited.injectAttributes(updated);
        if (!await edited.putDatabase()) {
          return;
        }
      }
      this.updateTable();
    },
    async remove() {
      this.closeModal();
      for (let deleted of this.selectedProducts) {
        if (!await deleted.delDatabase()) {
          return;
        }
      }
      this.updateTable();
    },
    async create(creation) {
      this.closeModal();
      if (!await creation.putDatabase()) {
        return;
      }
      this.updateTable();
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
    toggleCheckbox(productInfo, isChecked) {
      if (isChecked) {
        this.checkedProducts.push(productInfo.id);
      } else {
        this.checkedProducts = this.checkedProducts.filter(id => id !== productInfo.id);
      }
    },
    getSelected() {
      return Product.products.filter(p => this.checkedProducts.includes(p.id));
    }
  },
}
</script>

<style scoped>
.header {
  flex-direction: row;
  display: flex;
  padding-left: 1rem;
  padding-top: 1rem;
}

.body {
  position: relative;
  overflow-x: auto;
}

.body-container {
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 1rem;
  background-color: white;
}

.action-row {
  display: flex;
  margin-bottom: 0.5rem;
  margin-top: 0.5rem;
}

</style>
