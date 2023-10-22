<template>
  <div class="box">
    <div class="content">
      <div class="content-box">
        <div class="content-box-text">
          <form>
            <h3>Warehouses information</h3>
            <label class="fw-semibold">Name<span class="red fw-normal">*</span></label>
            <input type="text" placeholder="Solar Sedum" v-model="selectedName">
            <label class="fw-semibold">Adres<span class="red fw-normal">*</span></label>
            <input type="text" placeholder="H.J.E. Wenckebachweg 47d-4" v-model="selectedAddress">
            <label class="fw-semibold">Postal code<span class="red fw-normal">*</span></label>
            <input type="text" placeholder="1096 AK" v-model="selectedPostalCode">
            <label class="fw-semibold">City<span class="red fw-normal">*</span></label>
            <input type="text" placeholder="Amsterdam" v-model="selectedCity">
          </form>
          <button @click="handleSave()">{{isNewWarehouse ? 'Add warehouse' : 'Save'}}</button>
          <button @click="cancel()">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "WarehouseEditComponent",
  props: {
    name: String,
    address: String,
    postalCode: String,
    city: String,
  },
  data() {
    return {
      selectedName: this.name,
      selectedAddress: this.address,
      selectedPostalCode: this.postalCode,
      selectedCity: this.city,
      isNewWarehouse: !this.name,
    }
  },
  methods: {
    cancel() {
      this.$emit('cancel');
    },
    handleSave() {
      if (this.isNewWarehouse) {
        this.$emit('add-warehouse', {
          name: this.selectedName,
          address: this.selectedAddress,
          postalCode: this.selectedPostalCode,
          city: this.selectedCity
        });
      } else {
        this.$emit('save-warehouse', {
          name: this.selectedName,
          address: this.selectedAddress,
          postalCode: this.selectedPostalCode,
          city: this.selectedCity
        });
      }
    }
  },
  watch: {
    name(newName) {
      this.selectedName = newName;
    },
    address(newAddress) {
      this.selectedAddress = newAddress;
    },
    postalCode(newPostalCode) {
      this.selectedPostalCode = newPostalCode;
    },
    city(newCity) {
      this.selectedCity = newCity;
    }
  }
}
</script>

<style scoped>
.red {
  color: red;
}

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
  font-weight: bold;
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