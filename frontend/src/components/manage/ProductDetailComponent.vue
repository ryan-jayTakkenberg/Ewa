<template>
  <div class="box">
    <div class="content">
      <div class="content-header"></div>
      <div class="content-box">
        <div class="content-box-text">
          <table>
            <thead>
            <tr><th>List of all the products</th></tr>
            </thead>
            <tbody>
            <tr v-for="product in productList" :key="product.productId" @click="selectProduct(product)"
                :class="{ 'selected': selectedProduct === product }">
              {{"ID: " + product.productId + ", Name: " + product.name + ", Weight: "
            + product.weight + ", Description: " + product.description}}
            </tr>
            </tbody>
          </table>
          <button @click="showEditComponent = !showEditComponent" v-if="!selectedProduct">Create a new Product</button>
          <button @click="removeProduct" v-if="selectedProduct">Remove selected Product</button>
        </div>
      </div>
    </div>
  </div>
  <ProductEditComponent
    v-if="showEditComponent"
    @cancel="handleCancel"
    @add-product="addProduct"
    @save-product="saveProduct"
    :product-id="selectedProduct ? selectedProduct.productId: ''"
    :name="selectedProduct ? selectedProduct.name: ''"
    :weight="selectedProduct ? selectedProduct.weight: ''"
    :description="selectedProduct ? selectedProduct.description: ''"
  />
</template>

<script>
import {Product} from "@/models/product";
import ProductEditComponent from "@/components/manage/ProductEditComponent";

export default {
  name: "ProductDetailComponent",
  components: {
    ProductEditComponent
  },
  data(){
    return{
      productList: [],
      selectedProduct: null,
      showEditComponent: false,
    }
  },
  created() {
    let amountOfProducts = 5

    for (let i = 0; i < amountOfProducts; i ++){
      const newProduct = Product.createSampleProduct();
      this.productList.push(newProduct);
    }
  },
  methods: {
    handleCancel(){
      this.showEditComponent = false;
      this.selectedProduct = null;
    },
    selectProduct(product){
      if (this.selectedProduct === product){
        this.handleCancel();
      } else {
        this.selectedProduct = product;
        this.showEditComponent = true;
      }
    },
    removeProduct(){
      const index = this.productList.indexOf(this.selectedProduct);
      if (index !== -1){
        this.productList.splice(index, 1);
        this.handleCancel();
      }
    },
    addProduct(newProductData){
      const productId = newProductData.productId;
      const name = newProductData.name;
      const weight = newProductData.weight;
      const description = newProductData.description;
      const newProduct = new Product(productId, name, weight, description);
      this.productList.push(newProduct)
      this.handleCancel();
    },
    saveProduct(updatedProductData){
      if (this.selectedProduct){
        this.selectedProduct.productId = updatedProductData.productId;
        this.selectedProduct.name = updatedProductData.name;
        this.selectedProduct.weight = updatedProductData.weight;
        this.selectedProduct.description = updatedProductData.description;
      }
    }
  }
}
</script>

<style scoped>
.box{
  display: flex;
  justify-content: center;
  align-content: center;
}

.content{
  text-align: center;
  height: fit-content;
  width: 60%;
  border-radius: 20px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  margin: 15px 0 30px 0;
}

.content-header{
  height: 175px;
  background: url("../../../static/images/manageDetailHeader2.jpg") center no-repeat;
  background-size: cover;
  border-radius: 20px 20px 0 0 ;
}

.content-box{
  margin: 30px 0 30px 0;
}
.content-box-text{
  margin: 0 15% 0 15%;
  text-align: left;
}

button{
  margin: 35px 20px 20px 0;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}

table{
  border-collapse: collapse;
  width: 100%;
}

tr{
  border: 1px solid black;
  padding: 10px;
}

tr:hover{
  color: #818181;
  cursor: pointer;
}

.selected{
  background: #c7d02c;
}
</style>