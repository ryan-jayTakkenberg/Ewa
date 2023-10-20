<template>
  <div class="box">
    <div class="content">
      <div class="content-box">
        <div class="content-box-text">
          <form>
            <h3>Product information</h3>
            <label class="fw-semibold">Product ID<span class="red fw-normal">*</span></label>
            <input type="text" placeholder="A12345" v-model="selectedProductId">
            <label class="fw-semibold">Name<span class="red fw-normal">*</span></label>
            <input type="text" placeholder="Solar panel" v-model="selectedName">
            <label class="fw-semibold">Weight in kg</label>
            <input type="text" placeholder="1,525" v-model="selectedWeight">
            <label class="fw-semibold">Description</label>
            <textarea placeholder="Description . . ." v-model="selectedDescription"></textarea>
          </form>
          <button @click="handleSave()">{{isNewProduct ? 'Add product' : 'Save'}}</button>
          <button @click="cancel()">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProductEditComponent",
  props: {
    productId: String,
    name: String,
    weight: String,
    description: String,
  },
  data(){
    return{
      selectedProductId: this.productId,
      selectedName: this.name,
      selectedWeight: this.weight,
      selectedDescription: this.description,
      isNewProduct: !this.name,
    }
  },
  methods: {
    cancel(){
      this.$emit('cancel');
    },
    handleSave() {
      if (this.isNewProduct) {
        this.$emit('add-product', {
          productId: this.selectedProductId,
          name: this.selectedName,
          weight: this.selectedWeight,
          description: this.selectedDescription
        });
      } else {
        this.$emit('save-product', {
          productId: this.selectedProductId,
          name: this.selectedName,
          weight: this.selectedWeight,
          description: this.selectedDescription
        });
      }
    }
  },
  watch: {
    productId(newProductId) {
      this.selectedProductId = newProductId;
    },
    name(newName) {
      this.selectedName = newName;
    },
    weight(newWeight) {
      this.selectedWeight = newWeight;
    },
    description(newDescription) {
      this.selectedDescription = newDescription
    }
  }
}
</script>

<style scoped>
.red {
  color: red;
}

.box{
  margin-top: 10px;
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

.content-box{
  margin: 30px 0 30px 0;
}
.content-box-text{
  margin: 0 15% 0 15%;
  text-align: left;
}

label{
  width: 100%;
  margin: 10px 0 5px 0;
}

input{
  padding: 5px;
  border-radius: 10px;
  border: 1px solid gray;
  width: 100%;
  appearance: none;
}

textarea{
  width: 100%;
  height: 125px;
  overflow: hidden;
  resize: none;
  padding: 5px;
  border-radius: 10px;
}

button{
  margin: 35px 20px 20px 0;
  padding: 10px;
  border: none;
  border-radius: 10px;
  background: #c7d02c;
  color: #fff;
}

@media (max-width: 500px) {
  .content{
    width: 70%;
  }
}
</style>